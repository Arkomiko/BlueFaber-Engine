package bluefaber.content;

import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/** Maps content nodes to the registry calls and resource formats of one target. */
final class Fabric1211Exporter {
    private final ContentGraph graph;
    private final Path input;
    private final String namespace;

    Fabric1211Exporter(ContentGraph graph, Path input) {
        this.graph = graph;
        this.input = input;
        this.namespace = graph.namespace();
    }

    void export(Path output) throws IOException {
        if (Files.exists(output, LinkOption.NOFOLLOW_LINKS)) {
            throw new FileAlreadyExistsException(output.toString());
        }
        Files.createDirectories(output.getParent());
        // Stage the complete tree beside its destination before exposing it to the caller.
        Path staging = Files.createTempDirectory(output.getParent(), ".bluefaber-");
        try {
            writeProject(staging);
            Files.move(staging, output);
        } finally {
            if (Files.exists(staging)) {
                try (var paths = Files.walk(staging)) {
                    for (var path : paths.sorted(Comparator.reverseOrder()).toList()) Files.delete(path);
                }
            }
        }
    }

    private void writeProject(Path out) throws IOException {
        var english = new LinkedHashMap<String, String>();
        var russian = new LinkedHashMap<String, String>();
        var tags = new LinkedHashMap<String, Set<String>>();
        var registration = new StringBuilder();
        for (var node : graph.nodes()) {
            switch (node.kind()) {
                case "block", "item" -> {
                    renderContent(out, node, registration);
                    String key = node.kind() + "." + namespace + "." + node.id();
                    english.put(key, node.text("en_us"));
                    russian.put(key, node.text("ru_ru"));
                }
                case "recipe" -> renderRecipe(out, node);
                case "block_tag", "item_tag" -> {
                    String[] key = node.text("tag").split(":", 2);
                    String registry = node.kind().equals("block_tag") ? "block" : "item";
                    // Vanilla incorporates needs_stone_tool into its wood and gold exclusions.
                    var values = tags.computeIfAbsent("data/" + key[0] + "/tags/" + registry +
                            "/" + key[1] + ".json", ignored -> new LinkedHashSet<>());
                    node.values("entries").forEach(entry -> values.add(entry.getAsString()));
                }
                case "ore" -> renderOre(out, node, registration);
                default -> throw new IllegalStateException("Unexpected validated node " + node.kind());
            }
        }
        for (var tag : tags.entrySet()) {
            json(out, "src/main/resources/" + tag.getKey(),
                    Map.of("replace", false, "values", tag.getValue()));
        }
        json(out, asset("lang/en_us.json"), english);
        json(out, asset("lang/ru_ru.json"), russian);
        json(out, "src/main/resources/fabric.mod.json", Map.of(
                "schemaVersion", 1, "id", namespace, "name", graph.name(), "version", "1.0.0",
                "entrypoints", Map.of("main", List.of("bluefaber.generated.ContentMod")),
                "depends", Map.of("fabricloader", ">=0.16.14", "fabric-api", "*", "minecraft", "1.21.1", "java", ">=21")));
        write(out, "src/main/java/bluefaber/generated/ContentMod.java", resource("fabric-1.21.1/ContentMod.java.txt")
                .replace("@NAMESPACE@", namespace).replace("@REGISTRATION@", registration.toString().stripTrailing()));
        for (String file : List.of("build.gradle", "settings.gradle")) {
            write(out, file, resource("fabric-1.21.1/" + file).replace("@NAMESPACE@", namespace));
        }
        for (String file : List.of("gradlew", "gradlew.bat", "gradle/wrapper/gradle-wrapper.properties",
                "gradle/wrapper/gradle-wrapper.jar")) {
            Path dest = outputFile(out, file);
            try (var stream = Fabric1211Exporter.class.getResourceAsStream("/export-wrapper/" + file)) {
                if (stream == null) throw new IOException("Missing bundled wrapper: " + file);
                Files.copy(stream, dest, REPLACE_EXISTING);
            }
            if (file.equals("gradlew") && Files.getFileStore(dest).supportsFileAttributeView("posix")) {
                dest.toFile().setExecutable(true, false);
            }
        }
    }

    private void renderContent(Path out, ContentGraph.Node node, StringBuilder java) throws IOException {
        String id = node.id();
        boolean block = node.kind().equals("block");
        Files.copy(ContentGraph.texture(input, node), outputFile(out, asset("textures/" + node.kind() + "/" + id + ".png")));
        if (block) {
            json(out, asset("blockstates/" + id + ".json"), Map.of("variants", Map.of("", Map.of("model", namespace + ":block/" + id))));
            json(out, asset("models/block/" + id + ".json"), Map.of(
                    "parent", "minecraft:block/cube_all", "textures", Map.of("all", namespace + ":block/" + id)));
            json(out, asset("models/item/" + id + ".json"), Map.of("parent", namespace + ":block/" + id));
            json(out, data("loot_table/blocks/" + id + ".json"), Map.of(
                    "type", "minecraft:block", "pools", List.of(Map.of("rolls", 1,
                    "conditions", List.of(Map.of("condition", "minecraft:survives_explosion")),
                    "entries", List.of(Map.of("type", "minecraft:item", "name", node.text("drop")))))));
            java.append("        registerBlock(\"%s\", %df, %s); // block:%s%n"
                    .formatted(id, node.number("hardness"), node.text("requiresTool"), id));
        } else {
            json(out, asset("models/item/" + id + ".json"), Map.of(
                    "parent", "minecraft:item/generated", "textures", Map.of("layer0", namespace + ":item/" + id)));
            java.append("        registerItem(\"%s\"); // item:%s%n".formatted(id, id));
        }
    }

    private void renderRecipe(Path out, ContentGraph.Node node) throws IOException {
        var ingredient = Map.of("item", node.text("ingredient"));
        var result = Map.of("id", node.text("result"), "count", node.number("count"));
        Map<String, ?> recipe = switch (node.text("method")) {
            case "compact" -> Map.of("type", "minecraft:crafting_shaped",
                    "pattern", List.of("XXX", "XXX", "XXX"), "key", Map.of("X", ingredient), "result", result);
            case "unpack" -> Map.of("type", "minecraft:crafting_shapeless", "ingredients", List.of(ingredient), "result", result);
            case "smelting", "blasting" -> Map.of("type", "minecraft:" + node.text("method"),
                    "ingredient", ingredient, "result", result, "experience", 0.7,
                    "cookingtime", node.text("method").equals("smelting") ? 200 : 100);
            default -> throw new IllegalStateException("Unexpected recipe method");
        };
        json(out, data("recipe/" + node.id() + ".json"), recipe);
    }

    private void renderOre(Path out, ContentGraph.Node node, StringBuilder java) throws IOException {
        var targets = new ArrayList<Map<String, ?>>();
        for (var entry : node.values("targets")) {
            var match = entry.getAsJsonObject();
            targets.add(Map.of("target", Map.of("predicate_type", "minecraft:tag_match",
                    "tag", match.get("host").getAsString()), "state", Map.of("Name", match.get("block").getAsString())));
        }
        json(out, data("worldgen/configured_feature/" + node.id() + ".json"), Map.of(
                "type", "minecraft:ore", "config", Map.of("discard_chance_on_air_exposure", 0, "size", node.number("size"), "targets", targets)));
        json(out, data("worldgen/placed_feature/" + node.id() + ".json"), Map.of(
                "feature", namespace + ":" + node.id(), "placement", List.of(
                        Map.of("type", "minecraft:count", "count", node.number("count")),
                        Map.of("type", "minecraft:in_square"),
                        Map.of("type", "minecraft:height_range", "height", Map.of("type", "minecraft:trapezoid",
                                "min_inclusive", Map.of("absolute", node.number("minY")),
                                "max_inclusive", Map.of("absolute", node.number("maxY")))),
                        Map.of("type", "minecraft:biome"))));
        java.append("        addOverworldOre(\"%s\"); // ore:%s%n".formatted(node.id(), node.id()));
    }

    private String asset(String path) { return "src/main/resources/assets/" + namespace + "/" + path; }
    private String data(String path) { return "src/main/resources/data/" + namespace + "/" + path; }

    private static void json(Path out, String path, Object value) throws IOException {
        // Stable key order keeps exports diffable across separate JVM processes.
        var gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        write(out, path, gson.toJson(sorted(value)) + "\n");
    }

    private static Object sorted(Object value) {
        if (value instanceof Map<?, ?> map) {
            var ordered = new TreeMap<String, Object>();
            map.forEach((key, entry) -> ordered.put((String) key, sorted(entry)));
            return ordered;
        }
        if (value instanceof Collection<?> entries) return entries.stream().map(Fabric1211Exporter::sorted).toList();
        return value;
    }

    private static String resource(String name) throws IOException {
        try (var stream = Fabric1211Exporter.class.getResourceAsStream("/" + name)) {
            if (stream == null) throw new IOException("Missing target template: " + name);
            return new String(stream.readAllBytes(), UTF_8);
        }
    }

    private static void write(Path out, String relative, String value) throws IOException {
        Files.writeString(outputFile(out, relative), value);
    }

    private static Path outputFile(Path out, String relative) throws IOException {
        Path path = out.resolve(relative);
        Files.createDirectories(path.getParent());
        return path;
    }
}
