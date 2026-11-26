# Recovering Java from a JAR

[English documentation](README.md)

1. Hash and inventory the original archive, including nested jars, signatures, assets, class versions, and loader metadata.
2. Infer a source profile and namespace from artifact evidence. Present confidence and allow corrections before resolving JDK, mappings, and dependencies.
3. Use ASM to index original classes and build symbol and provenance links. Remap where needed, run Vineflower for Java reconstruction, and compare doubtful results with CFR.
4. Rebuild an independent workspace with a pinned classpath. Compare public ABI and runtime behavior rather than relying on readable output alone.
5. Import recognized operations into the graph. Preserve source, bytecode, resources, and reasons for any gap.

The recovery corpus covers erased generics and bridge methods, records, sealed classes, lambdas and `invokedynamic`, exception tables, preview class files, stripped debug attributes, Kotlin metadata, multi-release archives, and absent dependencies. Use a matching sources archive when its identity can be checked. Imports should expose ambiguity rather than guessing behavior.

[Full recovery specification in Russian](../ru-RU/JAVA-RECOVERY.md) · [Decompiler comparison](RESEARCH.md)
