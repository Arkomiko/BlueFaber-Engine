<picture>
  <source media="(prefers-reduced-motion: reduce)" srcset="../assets/docs-flow-still.svg">
  <img src="../assets/docs-flow.svg" width="800" height="104" alt="">
</picture>

# BlueFaber Engine · License guide

[Home](../README.md) · [Roadmap](ROADMAP.md) · [Research](RESEARCH.md)

[English](#english) · [Русский](#русский) · [Español](#español) · [हिन्दी](#हिन्दी) · [简体中文](#简体中文) · [日本語](#日本語)

**Copyright © 2026 Arkomiko** · [MIT License, original English text](../LICENSE)

## English

BlueFaber Engine is currently published under MIT. You may use, modify, share and sell copies under its terms. Keep the copyright notice and permission notice with copies or substantial portions. The software is provided as is, without warranty. This guide explains the license in six languages. The English text in [LICENSE](../LICENSE) is the applicable text.

For a project funded by voluntary donations and sponsorship, MIT is the recommended default. Apache 2.0 is an option if an explicit contributor patent grant matters. GPLv3 is relevant if keeping distributed derivatives under copyleft is a project goal. The editor, generated or ported output, third-party components and Minecraft game files each have their own licensing questions. [MIT](https://opensource.org/license/mit) · [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) · [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html)

## Русский

BlueFaber Engine сейчас опубликован под MIT. Она разрешает пользоваться программой, менять её, распространять и продавать копии при соблюдении условий. В копиях и существенных частях нужно сохранить уведомление об авторских правах и текст разрешения. Программа предоставляется «как есть», без гарантий. Для условий действует [английский текст LICENSE](../LICENSE).

### Выбор для BlueFaber

| Лицензия | Что меняется для проекта |
| --- | --- |
| [MIT](https://opensource.org/license/mit) | Разрешительная лицензия с коротким текстом. Производные можно распространять на других условиях при сохранении требуемых уведомлений. Отдельного явного патентного гранта в тексте нет. |
| [Apache 2.0](https://www.apache.org/licenses/LICENSE-2.0) | Разрешительная лицензия с явным патентным грантом от участников для описанных в лицензии случаев. При распространении действуют требования к уведомлениям, отметкам об изменённых файлах и файлу `NOTICE`, если он поставляется с исходной работой. |
| [GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html) | Copyleft для распространения подпадающих под GPL производных и соответствующих исходников. Может быть уместна, если важно сохранять открытость распространяемых форков. Состав зависимостей и код, попадающий в вывод генератора, надо разобрать по файлам. |

**Рекомендация — оставить MIT.** Для добровольных донатов и спонсорства особая лицензия не нужна. MIT даёт гибкость для собственного кода редактора, а условия конкретных зависимостей и поставляемых файлов проверяются при упаковке. Если для будущих вкладов нужен явный патентный грант, следующий кандидат — Apache 2.0. GitHub Sponsors поддерживает финансирование открытой разработки. [GitHub Sponsors](https://docs.github.com/en/sponsors/getting-started-with-github-sponsors/about-github-sponsors)

### Что можно взять из опыта MCreator

MCreator выпускает редактор под GPL и отдельно разрешает распространять работы, содержащие определённые шаблоны генератора `.ftl`, на выбранных условиях. Это разрешение описано в заголовке лицензии MCreator и относится к указанным шаблонам. Для BlueFaber такой подход полезен как пример явного договора о том, какой код попадает в мод, а какие файлы остаются частью редактора. [Заголовок лицензии MCreator](https://github.com/MCreator/MCreator/blob/master/LICENSE-header.txt) · [FAQ MCreator](https://mcreator.net/support/knowledgebase)

### Что проверять при выпуске

| Что поставляется | Что фиксировать |
| --- | --- |
| Собственные файлы BlueFaber | Действующий корневой `LICENSE`. При смене лицензии проверить права на вклады и указать, с какого выпуска действуют новые условия. У ранее выданных MIT-копий остаются их условия. |
| JVM и инструменты сборки | Java как язык, выбранный JDK и бинарная упаковка — разные уровни. При поставке JDK проверить лицензию именно дистрибутива и вложенных компонентов. [OpenJDK LICENSE](https://github.com/openjdk/jdk/blob/master/LICENSE) · [Oracle JDK FAQ](https://www.oracle.com/java/technologies/javase/jdk-faqs.html) |
| Forge, NeoForge, Fabric, Quilt | Проверить лицензии точных артефактов загрузчика, API, mappings и библиотек. Отдельно зафиксировать, запрашивает ли профиль их у пользователя, скачивает из исходного репозитория или включает в поставку. [Forge](https://github.com/MinecraftForge/MinecraftForge/blob/26.3/LICENSE.txt) · [NeoForge](https://github.com/neoforged/NeoForge/blob/26.3.x/README-LICENSE.md) · [Fabric Loader](https://github.com/FabricMC/fabric-loader) · [Quilt Loader](https://github.com/QuiltMC/quilt-loader/blob/develop/LICENSE) |
| Bukkit, Spigot, Paper, LiteLoader | Для Bukkit/Spigot/Paper проверить API и реальный сервер отдельно. Для LiteLoader сначала закрепить точную историческую версию и найти лицензию именно выбранного артефакта. Сохранять ссылки, версии и уведомления у каждого профиля. [Bukkit](https://github.com/Bukkit/Bukkit) · [Spigot BuildTools](https://www.spigotmc.org/wiki/buildtools/) · [Paper](https://github.com/PaperMC/Paper/blob/main/LICENSE.md) |
| Моды и плагины на выходе | Указать происхождение пользовательского кода, импортированного мода, зависимостей, ресурсов и шаблонов. Условия на эти части сверяются по лицензионным текстам правообладателей перед распространением. |
| Minecraft и финансирование | Поддержка развития редактора может строиться на добровольных взносах. Для условий продвижения, коммерческого использования и распространения файлов Minecraft применяются правила Mojang. При выпуске мода распространять сам мод в пределах применимых условий и получать игровые файлы из разрешённого источника. [Minecraft EULA](https://www.minecraft.net/en-us/eula) · [Minecraft Usage Guidelines](https://www.minecraft.net/en-us/usage-guidelines) |

## Español

BlueFaber Engine se publica actualmente bajo la licencia MIT. Puedes usar, modificar, redistribuir y vender copias conforme a sus condiciones. Conserva los avisos de derechos de autor y de permiso en las copias o partes sustanciales. El software se proporciona tal cual y sin garantías. La financiación prevista se basa en donaciones voluntarias y patrocinios. Las dependencias y los archivos importados conservan sus propias condiciones. Esta sección es informativa. [Texto aplicable en inglés](../LICENSE)

## हिन्दी

BlueFaber Engine फ़िलहाल MIT लाइसेंस के तहत प्रकाशित है। उसकी शर्तों के अनुसार आप सॉफ़्टवेयर का उपयोग कर सकते हैं, उसमें बदलाव कर सकते हैं और उसकी प्रतियाँ बाँट या बेच सकते हैं। प्रतियों या उनके महत्वपूर्ण हिस्सों में कॉपीराइट और अनुमति की सूचनाएँ बनाए रखें। सॉफ़्टवेयर जैसा है वैसा ही, बिना किसी वारंटी के दिया जाता है। परियोजना के लिए स्वैच्छिक दान और प्रायोजन की योजना है। बाहरी निर्भरताओं और आयात की गई फ़ाइलों की शर्तें अलग हैं। यह सारांश जानकारी के लिए है। [लागू अंग्रेज़ी पाठ](../LICENSE)

## 简体中文

BlueFaber Engine 目前采用 MIT 许可证发布。遵守许可条件即可使用、修改、分发和销售软件副本。分发副本或实质性部分时，请保留版权声明和许可声明。软件按现状提供，不附带任何保证。项目计划通过自愿捐赠与赞助获得支持。第三方依赖和导入文件仍受各自条款约束。本节仅供参考。[适用的英文原文](../LICENSE)

## 日本語

BlueFaber Engine は現在 MIT ライセンスで公開されています。ライセンスの条件に従い、ソフトウェアの使用、改変、再配布、複製物の販売ができます。複製物や実質的な部分には、著作権表示と許諾文を残してください。ソフトウェアは現状のまま提供され、保証はありません。開発資金は任意の寄付やスポンサーによる支援を予定しています。外部の依存関係や取り込んだファイルには、それぞれの利用条件が適用されます。この節は参考訳です。[適用される英語原文](../LICENSE)
