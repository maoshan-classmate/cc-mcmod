# Getting Started with Forge

## 前置要求

### 必要条件

1. **Java 17 JDK** - Forge 推荐使用 Eclipse Temurin
   - 下载地址: https://adoptium.net/temurin/releases?version=17
   - **注意**: 必须使用 64-bit JVM，否则在使用 ForgeGradle 时会有问题

2. **IDE** - 推荐 IntelliJ IDEA 或 Eclipse
   - 建议使用带有 Gradle 集成的 IDE

## 从零开始

### 1. 下载 MDK

从 https://files.minecraftforge.net 下载 Mod Developer Kit (MDK)
- 点击 'Mdk' 然后等待一段时间后点击右上角的 'Skip' 按钮
- 推荐下载最新版本的 Forge

### 2. 解压 MDK

将下载的 MDK 解压到一个空目录。这个目录将成为你的模组目录，包含：
- gradle 文件
- `src` 子目录（包含示例模组）

**可复用的文件**（可在不同模组间复制）：
- `gradle/` 子目录
- `build.gradle`
- `gradlew`
- `gradlew.bat`
- `settings.gradle`

**不要复制** `src/` 子目录

### 3. 打开 IDE

Forge 仅官方支持 Eclipse 和 IntelliJ IDEA，但也可以使用 VS Code 等其他 IDE。

- Eclipse 和 IntelliJ IDEA 自带 Gradle 集成，会自动处理初始工作区设置
- VS Code 需要安装 'Gradle for Java' 插件

### 4. 生成运行配置

```bash
# Eclipse
./gradlew genEclipseRuns

# IntelliJ IDEA
./gradlew genIntellijRuns

# VS Code
./gradlew getVSCodeRuns
```

**IntelliJ IDEA 注意事项**: 如果出现 "module not specified" 错误，需要设置 `ideaModule` 属性为你的 'main' 模块（通常是 `${project.name}.main`）。

### 5. 自定义模组信息

编辑 `build.gradle` 文件：

#### Mod ID 替换

将所有 `examplemod` 替换为你的模组 ID，包括：
- `mods.toml`
- 主模组文件

同时设置 `base.archivesName`（通常设为模组 ID）：
```groovy
base.archivesName = 'mymod'
```

#### Group ID

`group` 属性应设置为你的一级包名：

```groovy
group = 'com.example'
```

包结构示例：
```
com.example.mymod/
├── MyMod.java  (主类)
└── ...
```

**常见格式**：
| 类型 | 值 | 一级包 |
|------|-----|--------|
| 域名 | example.com | com.example |
| 子域名 | example.github.io | io.github.example |
| 邮箱 | example@gmail.com | com.gmail.example |

#### Version

使用 Maven 版本格式：
```groovy
version = '1.20-1.0.0.0'
```

## 构建和测试

### 构建模组

```bash
gradlew build
```

输出文件位于 `build/libs/`，文件名为 `[archivesBaseName]-[version].jar`

### 运行测试环境

使用生成的运行配置或使用任务：
```bash
gradlew runClient   # 运行客户端
gradlew runServer   # 运行服务器
gradlew runData     # 运行数据生成器
```

### 专用服务器

运行服务器后，服务器会立即关闭。需要编辑 `run` 目录下的 `eula.txt` 接受 Minecraft EULA。

**重要**: 你应该始终在专用服务器环境中测试你的模组，包括仅客户端的模组。
