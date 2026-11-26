package bluefaber.content;

import com.google.gson.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

/** Saved project data. Resource IDs connect node inputs to the outputs of other nodes. */
record ContentGraph(int schema, String namespace, String name, List<Node> nodes) {
    record Node(String kind, String id, JsonObject properties) {
        String text(String key) { return properties.get(key).getAsString(); }
        int number(String key) { return properties.get(key).getAsInt(); }
        JsonArray values(String key) { return properties.getAsJsonArray(key); }
    }

    static ContentGraph read(Path file) throws IOException {
        try (var reader = Files.newBufferedReader(file)) {
            var gson = new GsonBuilder().setStrictness(Strictness.STRICT).create();
            var graph = gson.fromJson(reader, ContentGraph.class);
            if (graph == null || graph.schema != 1 || graph.namespace == null ||
                    !graph.namespace.matches("[a-z][a-z0-9_-]{1,63}") ||
                    graph.namespace.equals("minecraft") || graph.name == null ||
                    graph.name.isBlank() || graph.nodes == null || graph.nodes.isEmpty()) {
                throw new IllegalArgumentException("Invalid graph header or unsupported schema");
            }
            return graph;
        }
    }

    void validate(Path root, String target) throws IOException {
        if (!Set.of("fabric-1.21.1", "paper-1.21.1").contains(target)) {
            throw new IllegalArgumentException("Unknown target: " + target);
        }
        var blocks = new HashSet<String>();
        var items = new HashSet<String>();
        var keys = new HashSet<String>();
        for (var node : nodes) {
            require(node != null && node.kind != null && node.properties != null &&
                    node.id != null && node.id.matches("[a-z0-9_]+"), "Invalid node");
            String id = namespace + ":" + node.id;
            require(keys.add(node.kind + ":" + node.id), "Duplicate node: " + node.id);
            if ("block".equals(node.kind)) blocks.add(id);
            if (Set.of("block", "item").contains(node.kind)) {
                require(!target.startsWith("paper"), node.id + ": custom registry entries require a mod loader");
                require(items.add(id), "Duplicate item or block item: " + id);
                for (String locale : List.of("en_us", "ru_ru")) {
                    require(!node.text(locale).isBlank(), node.id + ": empty localization " + locale);
                }
                texture(root, node);
            }
        }
        for (var node : nodes) {
            try {
                switch (node.kind) {
                    case "block" -> {
                        require(node.number("hardness") > 0, "Expected positive hardness");
                        require(node.properties.get("requiresTool").getAsJsonPrimitive().isBoolean(),
                                "Expected requiresTool boolean");
                        reference(node.text("drop"), items);
                    }
                    case "item" -> { /* Registration and visual assets were checked above. */ }
                    case "recipe" -> {
                        reference(node.text("ingredient"), items);
                        reference(node.text("result"), items);
                        require(node.number("count") >= 1 && node.number("count") <= 64, "Invalid output count");
                        require(Set.of("compact", "unpack", "smelting", "blasting").contains(node.text("method")),
                                "Unknown recipe method");
                    }
                    case "block_tag", "item_tag" -> {
                        resourceId(node.text("tag"));
                        require(!node.values("entries").isEmpty(), "Empty tag");
                        for (var entry : node.values("entries")) {
                            reference(entry.getAsString(), node.kind.equals("block_tag") ? blocks : items);
                        }
                    }
                    case "ore" -> {
                        require(node.number("size") >= 1 && node.number("size") <= 64, "Invalid vein size");
                        require(node.number("count") > 0 && node.number("minY") >= -64 &&
                                node.number("maxY") <= 319 && node.number("minY") <= node.number("maxY"),
                                "Invalid ore placement");
                        require(!node.values("targets").isEmpty(), "Missing ore replacement targets");
                        for (var entry : node.values("targets")) {
                            var rule = entry.getAsJsonObject();
                            resourceId(rule.get("host").getAsString());
                            reference(rule.get("block").getAsString(), blocks);
                        }
                    }
                    default -> throw new IllegalArgumentException("Unknown node kind " + node.kind);
                }
            } catch (RuntimeException error) {
                throw new IllegalArgumentException(node.id + ": " + error.getMessage(), error);
            }
        }
    }

    static Path texture(Path root, Node node) throws IOException {
        Path folder = root.resolve("textures").resolve(node.kind).toAbsolutePath().normalize();
        Path file = folder.resolve(node.id + ".png");
        require(Files.isRegularFile(file) && file.toRealPath().startsWith(root.toRealPath()),
                node.id + ": missing texture in textures/" + node.kind);
        return file;
    }

    private void reference(String id, Set<String> defined) {
        resourceId(id);
        require(defined.contains(id) || id.startsWith("minecraft:"), "Unresolved reference: " + id);
    }

    private static void resourceId(String id) {
        require(id.matches("[a-z0-9_-]+:[a-z0-9_-]+(?:/[a-z0-9_-]+)*"), "Invalid resource ID: " + id);
    }

    private static void require(boolean condition, String message) {
        if (!condition) throw new IllegalArgumentException(message);
    }
}
