# The first verified port

[English documentation](README.md)

The first candidate is **Fabric 1.21.1 ↔ 1.21.4**. The fixture has one observable recipe and one item model. Each direction starts with an independently built source JAR.

| Checkpoint | Evidence |
| --- | --- |
| Source baseline | Exact game and loader, JDK, dependency versions, archive hash, and expected behavior. |
| Import | The recipe appears as a node and links back to its resource in the original JAR. |
| Edit and export | A changed result becomes a separate target project with stable identifiers and explicit unresolved cases. |
| Game test | The target loads, the recipe yields the expected item, and the model displays on the selected client. |
| Reverse and negative cases | Test the reverse direction separately. An unsupported model or registry operation reports a specific blocker. |

Paper is a separate boundary test using a vanilla recipe and a failed custom registration. Larger candidates such as IC², Thaumcraft, Affinity, and Moldonyan Industry follow once a reliable corpus and rights checks are in place.

[Detailed first-port plan in Russian](../ru-RU/PORTING-FIRST.md) · [BFE-1001](https://github.com/Arkomiko/BlueFaber-Engine/issues/1002) · [BFE-1002](https://github.com/Arkomiko/BlueFaber-Engine/issues/1003)
