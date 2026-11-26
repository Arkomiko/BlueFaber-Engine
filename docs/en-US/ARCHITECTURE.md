# Engine architecture

[English documentation](README.md)

BlueFaber is designed around one workspace with three linked representations. The archive index records the original JAR and its entries. The Java and JVM layer records symbols, source, and bytecode. The node graph models recognized operations that can be edited. Links between these layers let a user follow a diagnostic back to its origin.

| Component | Responsibilities |
| --- | --- |
| Importer | Inventory metadata and resources, detect the source profile, resolve dependencies, and track unresolved inputs. |
| JVM worker | Remap and inspect bytecode, reconstruct Java, index symbols, and produce diagnostics without loading imported classes. |
| Graph core | Validate types, control flow, execution side, schema versions, and stable identifiers. Preserve unknown nodes and fields. |
| Port planner | Calculate directed changes, show a preview, and report the required changes or capabilities missing in the target. |
| Target adapter | Generate independent Java and resource trees, apply target build tooling, and relate failures back to graph nodes. |
| Desktop interface | Display the canvas, palette, source provenance, compatibility preview, and build results. |

The original artifact stays available for comparison. Generated code and hand-written source have distinct owners, and regeneration records exactly which files are safe to replace. The JVM and interface can evolve independently behind a versioned protocol. Tool versions are pinned per profile.

[Detailed design in Russian](../ru-RU/ARCHITECTURE.md) · [Java recovery](JAVA-RECOVERY.md) · [Porting](PORTING.md)
