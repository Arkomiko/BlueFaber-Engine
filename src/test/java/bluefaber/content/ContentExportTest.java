package bluefaber.content;

import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.*;

import static org.junit.jupiter.api.Assertions.*;

class ContentExportTest {
    @TempDir Path temporary;
    private final Path template = Path.of("templates/blusmor/project.json");

    @Test
    void exportsRegistrationsResourcesAndWorldGenerationFromTheGraph() throws Exception {
        var graph = ContentGraph.read(template);
        graph.validate(template.getParent(), "fabric-1.21.1");
        Path out = temporary.resolve("fabric");
        var exporter = new Fabric1211Exporter(graph, template.getParent());
        exporter.export(out);

        Path data = out.resolve("src/main/resources/data/blusmor");
        try (var lootTables = Files.list(data.resolve("loot_table/blocks"))) {
            assertEquals(4, lootTables.count());
        }
        assertEquals("blusmor:raw_blusmor", json(data.resolve("loot_table/blocks/blusmor_ore.json"))
                .getAsJsonArray("pools").get(0).getAsJsonObject().getAsJsonArray("entries")
                .get(0).getAsJsonObject().get("name").getAsString());
        assertEquals(9, json(data.resolve("recipe/unpack_blusmor_ingot.json")).getAsJsonObject("result").get("count").getAsInt());
        assertEquals(2, json(data.resolve("worldgen/configured_feature/blusmor_vein.json"))
                .getAsJsonObject("config").getAsJsonArray("targets").size());
        assertTrue(Files.readString(out.resolve("src/main/resources/data/minecraft/tags/block/needs_stone_tool.json"))
                .contains("blusmor:deepslate_blusmor_ore"));
        assertTrue(Files.isRegularFile(out.resolve("gradle/wrapper/gradle-wrapper.jar")));
        assertThrows(FileAlreadyExistsException.class, () -> exporter.export(out));
    }

    @Test
    void exportsAnotherNamespaceAndRejectsUnsupportedRegistries() throws Exception {
        Path other = temporary.resolve("material.json");
        Files.writeString(other, Files.readString(template).replace("blusmor:", "othermaterial:")
                .replace("\"namespace\": \"blusmor\"", "\"namespace\": \"othermaterial\""));
        var graph = ContentGraph.read(other);
        graph.validate(template.getParent(), "fabric-1.21.1");
        Path out = temporary.resolve("renamed");
        new Fabric1211Exporter(graph, template.getParent()).export(out);
        assertEquals("othermaterial", json(out.resolve("src/main/resources/fabric.mod.json")).get("id").getAsString());
        assertTrue(Files.exists(out.resolve("src/main/resources/data/othermaterial/recipe/blasting_blusmor.json")));
        assertTrue(assertThrows(IllegalArgumentException.class,
                () -> graph.validate(template.getParent(), "paper-1.21.1")).getMessage().contains("custom registry"));
    }

    private static com.google.gson.JsonObject json(Path path) throws Exception {
        return JsonParser.parseString(Files.readString(path)).getAsJsonObject();
    }
}
