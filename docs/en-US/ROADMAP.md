# Development roadmap

[English documentation](README.md)

BlueFaber Engine is planned around moving Minecraft Java Edition mods and plugins between versions and platforms. Each release claim is tied to a reproduced result in a pinned target environment.

| Phase | Milestone |
| --- | --- |
| P00 | Test corpus and an early bidirectional Fabric profile check. |
| P01–P03 | Project format, initial generation and visual authoring. |
| P04–P06 | Inspect an input JAR, recover a source project, and lift supported operations into nodes. |
| P07–P08 | Retarget Mixin and other transformations. Expand version rules in both directions. |
| P09 | Add Forge, NeoForge and the Bukkit family. Verify cross-loader and mod-to-plugin routes. |
| P10 | Expand editable template graphs through content, network, and client behavior. |
| P11 | Test legacy versions with isolated toolchains, mappings, and archives. |
| P12 | Diagnostics, reproducible builds, packaging and release evidence. |

The first validation slice moves a recipe and a model between Fabric 1.21.1 and 1.21.4 before the full content catalog. A route needs a source baseline, independently rebuilt target artifact, and a runtime comparison.

[Detailed milestone criteria in Russian](../ru-RU/ROADMAP.md) · [Developer backlog](BACKLOG.md) · [First verified port](PORTING-FIRST.md)
