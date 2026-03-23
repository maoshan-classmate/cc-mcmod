# McJty Tutorial Episode 1 - 基础

本文档是 McJty Forge 1.20 教程 Episode 1 的本地化版本。

## 原始链接

- 教程页面: https://mcjty.eu/docs/1.20/ep1
- 视频: https://youtu.be/BpUbD0NXfp8
- GitHub 示例: https://github.com/McJty/Tut4_1Basics

## 内容

### 基本项目设置

1. 从 https://files.minecraftforge.net 下载最新 Forge MDK
2. 解压到临时文件夹
3. 复制以下文件到你的模组目录：
   - `gradle/` 文件夹
   - `src/` 文件夹
   - `gradlew.bat` 和 `gradlew`
   - `settings.gradle`, `build.gradle`, `gradle.properties`
   - `.gitignore`

4. 用 IDE（如 IntelliJ）打开 `build.gradle` 作为项目
5. 确保使用 JDK 17

### modid 修改位置

- `gradle.properties`
- 主模组文件（MDK 中是 `ExampleMod`）

### gradle.properties

Forge 1.20+ 的所有配置都在 `gradle.properties` 中。

### Mappings

Minecraft 混淆发布，有两种映射方式：
- **official**: Mojang 官方映射
- **parchment**: 带额外文档的 Mojang 映射

更多信息: https://parchmentmc.org/docs/getting-started

### JEI 和 TOP 依赖

在 `build.gradle` 中添加仓库：
```groovy
repositories {
    maven { url "https://maven.blamejared.com" }  // JEI
    maven { url "https://maven.k-4u.nl" }        // TOP
}
```

### 生成运行配置

在 IntelliJ 中运行 `genIntellijRuns` 任务生成 runClient、runServer、runData。

### 主模组类

参见 [索引文档](./index.md) 中的代码示例。

### Minecraft 概念

#### 三列概念

| 列 | 说明 |
|----|------|
| Definitions | 游戏中只有一个实例（如钻石剑） |
| Inventory | 用 ItemStack 表示 |
| World | 用 BlockState 表示（如熔炉有6种方向） |

#### 方块实体 (Block Entities)

用于扩展方块，持有额外信息（如物品栏）和执行操作（如 tick）。

### Sides

Minecraft 运行在两个端：客户端和服务端。即使在单人游戏也有服务端（集成服务端）。

详见 [Forge Sides 文档](../forge/concepts/sides.md)

### Events

两种主要事件：

| 事件 | 总线 | 示例 |
|------|------|------|
| Mod events | Mod event bus | FMLCommonSetupEvent, FMLClientSetupEvent |
| Forge events | Forge event bus | ServerStartingEvent, EntityJoinLevelEvent |

### Registration and Timing

Forge 有严格的注册时序规则：
- 对象在很早的阶段注册（在配置加载之前）
- **不能在注册时依赖配置值**
- 每个注册类型都有特定的事件控制

### Data Generation

运行 `./gradlew runData` 生成模型和 JSON 文件。

详见 [Forge Data Generation 文档](../forge/datagen/index.md)
