# Version and platform porting

[English documentation](README.md)

A profile specifies Minecraft, loader or server, mappings, target JDK, dependencies, and execution side. A route links one concrete source profile to one concrete target profile. Each direction receives its own test.

| Route | Coverage in the plan |
| --- | --- |
| Version upgrades and downgrades | Separate directions within Forge, NeoForge, Fabric, Bukkit/CraftBukkit, Spigot and Paper. |
| Mod loader changes | Forge, NeoForge and Fabric in both directions on a shared Minecraft version. |
| Server platform changes | Bukkit, Spigot and Paper in both directions on a shared Minecraft version. |
| Mods ↔ plugins | Each mod loader paired with each server platform on a shared version. Verify shared server behavior and report gaps in registries, networking, or client features. |

Mixin and related import work checks configurations, refmaps, selectors, descriptors, injection anchors, priorities, slices, locals and MixinExtras against the actual target bytecode. Missing, ambiguous, and conflicting injections have separate outcomes. Record native rebuilds separately from compatibility-layer tests.

[Full route matrix and issue links](../ru-RU/PORTING.md#задачи-на-каждое-направление) · [First verified port](PORTING-FIRST.md)
