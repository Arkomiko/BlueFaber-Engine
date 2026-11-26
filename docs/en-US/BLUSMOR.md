# BLUSMOR ore and processing

[English documentation](README.md)

This starter graph makes it possible to test registration, recipes, loot, translation, and world generation together. Its first proposed full profile is Fabric for Minecraft Java Edition 1.21.1.

| Resource ID | Expected behavior |
| --- | --- |
| `blusmor:blusmor_ore` | Replaces eligible stone during generation. Drops raw Blusmor with a stone pickaxe or better. |
| `blusmor:deepslate_blusmor_ore` | Uses the same drop and tool tier on profiles with deepslate. |
| `blusmor:raw_blusmor` | Smelts into one ingot in a furnace or blast furnace. |
| `blusmor:raw_blusmor_block` | Packs nine raw items and unpacks into nine. |
| `blusmor:blusmor_ingot` | Result of processing raw Blusmor. |
| `blusmor:blusmor_block` | Packs nine ingots and unpacks into nine. |

The graph owns models, textures, block states, loot tables, tool tags and localization keys. Pin a test seed, use newly generated chunks, and record separate checks for both ores. Validate the graph again after changing the recipe output. For a Paper profile, verify a vanilla recipe independently and capture the diagnostic for a custom registry entry.

[Full fixture in Russian](../ru-RU/BLUSMOR.md) · [Expanded BLUSMOR and BLUSTREE](TEMPLATES.md)
