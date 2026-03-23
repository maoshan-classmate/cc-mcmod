# Minecraft Forge 1.20.1 模组开发环境搭建计划

> **给 Claude 的说明：** 必须使用 superpowers:executing-plans 技能来逐任务执行此计划。

**目标：** 搭建完整的 Minecraft Forge 1.20.1 模组开发环境，包含完整的文档和项目结构。

**架构：** Java 17 + Forge MDK + Gradle 8.x。项目将遵循 Forge 官方最佳实践，使用 DeferredRegister 模式进行注册，采用正确的包结构，并使用数据生成来处理资源文件。

**技术栈：** Java 17, Minecraft Forge 1.20.1, Gradle, IntelliJ IDEA（推荐）

---

## 前置条件检查

开始之前，请确保你已具备：
- 已安装 Java 17 JDK（推荐 Eclipse Temurin）
- IntelliJ IDEA 并已安装 Gradle 插件（或 Eclipse/VS Code）
- 已安装 Git
- 约 4GB+ 可用磁盘空间（用于 Gradle 缓存和 Minecraft 资源）

---

## 任务 1：创建 Forge 开发文档参考手册

**文件：**
- 创建：`docs/forge-development-guide.md`

**步骤 1：创建文档目录和文件**

创建 `docs/forge-development-guide.md`，包含完整的 Forge 开发文档，内容涵盖：
- 官方文档链接
- 项目结构最佳实践
- 注册模式
- 事件处理
- 常用代码模板

**步骤 2：验证文档结构**

运行：`cat docs/forge-development-guide.md`
预期：文件包含完整的 Forge 开发参考内容

---

## 任务 2：下载并解压 Forge MDK

**文件：**
- 创建：`gradle/wrapper/*`（来自 MDK）
- 创建：`build.gradle`
- 创建：`gradle.properties`
- 创建：`settings.gradle`
- 创建：`gradlew`、`gradlew.bat`

**步骤 1：下载 Forge 1.20.1 MDK**

从以下地址下载：https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html
点击 "Mdk"，等待后点击 "Skip"。

**步骤 2：解压 MDK 文件**

解压 MDK zip 文件，将以下文件复制到项目根目录：
- `gradle/` 文件夹
- `src/` 文件夹
- `build.gradle`
- `gradle.properties`
- `settings.gradle`
- `gradlew` 和 `gradlew.bat`
- `.gitignore`

**步骤 3：验证解压结果**

运行：`ls -la`
预期：所有 MDK 文件都已存在于项目根目录

---

## 任务 3：配置项目设置

**文件：**
- 修改：`gradle.properties`
- 修改：`build.gradle`

**步骤 1：配置 gradle.properties**

更新 `gradle.properties` 中的模组信息：

```properties
# 模组属性
mod_id=yourmodid
mod_name=你的模组名称
mod_license=All Rights Reserved
mod_version=1.0.0
mod_group_id=com.yourname.yourmod
mod_authors=你的名字
mod_description=一个 Minecraft Forge 模组

# Forge 和 Minecraft 版本
minecraft_version=1.20.1
minecraft_version_range=[1.20.1,1.21)
forge_version=47.3.0
forge_version_range=[47,48)
loader_version_range=[47,)

# 映射通道
mapping_channel=official
mapping_version=1.20.1
```

**步骤 2：验证 Gradle 配置**

运行：`./gradlew --version`
预期：显示 Gradle 版本，检测到 Java 17

---

## 任务 4：设置项目包结构

**文件：**
- 创建：`src/main/java/com/yourname/yourmod/YourMod.java`
- 创建：`src/main/java/com/yourname/yourmod/init/ModBlocks.java`
- 创建：`src/main/java/com/yourname/yourmod/init/ModItems.java`
- 创建：`src/main/resources/META-INF/mods.toml`

**步骤 1：创建包结构**

```bash
mkdir -p src/main/java/com/yourname/yourmod/init
mkdir -p src/main/java/com/yourname/yourmod/block
mkdir -p src/main/java/com/yourname/yourmod/item
mkdir -p src/main/java/com/yourname/yourmod/client
mkdir -p src/main/resources/assets/yourmodid/textures
mkdir -p src/main/resources/assets/yourmodid/models
mkdir -p src/main/resources/assets/yourmodid/lang
mkdir -p src/main/resources/data/yourmodid/recipes
```

**步骤 2：重命名并更新主模组类**

将 `ExampleMod.java` 重命名为你的模组名称，并更新：
- 包声明
- @Mod 注解值
- MODID 常量
- Logger 名称

**步骤 3：验证结构**

运行：`find src -type d`
预期：包结构符合最佳实践

---

## 任务 5：配置 IDE 运行配置

**文件：**
- 生成：IDE 运行配置

**步骤 1：生成 IntelliJ 运行配置**

运行：`./gradlew genIntellijRuns`
预期：任务成功完成，运行配置已创建

**步骤 2：验证运行配置存在**

运行：`ls .idea/runConfigurations/` 或检查 IDE 运行下拉菜单
预期：存在 runClient、runServer、runData 配置

---

## 任务 6：测试开发环境

**文件：**
- 无新文件

**步骤 1：运行初始 Gradle 设置**

运行：`./gradlew setupDecompWorkspace`
预期：Minecraft 已反编译并映射成功

**步骤 2：构建项目**

运行：`./gradlew build`
预期：BUILD SUCCESSFUL，jar 文件在 `build/libs/` 中

**步骤 3：测试运行客户端**

运行：`./gradlew runClient`
预期：Minecraft 启动并加载了你的模组

---

## 任务 7：创建额外文档

**文件：**
- 创建：`docs/project-structure.md`
- 创建：`docs/common-patterns.md`

**步骤 1：创建项目结构文档**

记录完整的项目结构，并解释每个目录和文件的用途。

**步骤 2：创建常用模式文档**

记录常用模式，包括：
- 使用 DeferredRegister 注册方块
- 注册物品
- 创建 BlockEntity
- 事件处理示例
- 数据生成设置

**步骤 3：验证文档**

运行：`ls docs/`
预期：所有文档文件都存在

---

## 任务 8：设置版本控制

**文件：**
- 创建：`.gitignore`
- 创建：`.gitattributes`（如需要）

**步骤 1：初始化 Git 仓库**

运行：`git init`
预期：空 Git 仓库已初始化

**步骤 2：验证 .gitignore**

确保 `.gitignore` 包含：
- `/build/`
- `/run/`
- `/*.class`
- `*.jar`
- `*.log`
- `.gradle/`
- IDE 特定文件

**步骤 3：初始提交**

```bash
git add .
git commit -m "初始提交：Forge 1.20.1 模组开发环境"
```

预期：所有文件成功提交

---

## 关键资源

### 官方文档
- [Forge 文档 1.20.1](https://docs.minecraftforge.net/en/1.20.1/)
- [Forge 入门指南](https://docs.minecraftforge.net/en/1.20.1/gettingstarted/)
- [Forge 模组结构](https://docs.minecraftforge.net/en/1.20.1/gettingstarted/structuring/)
- [Forge 注册表](https://docs.minecraftforge.net/en/1.20.1/concepts/registries/)
- [Forge 事件](https://docs.minecraftforge.net/en/1.20.1/concepts/events/)

### 教程资源
- [McJty 的 Forge 1.20 教程](https://mcjty.eu/docs/1.20/ep1)
- [Kaupenjoe YouTube 系列](https://www.youtube.com/watch?v=55qUIf3GMss)

### 中文资源
- [MC百科 Forge教程](https://www.mcmod.cn/post/3993.html)
- [Forge中文文档](https://mcforge-cn.readthedocs.io/zh/latest/gettingstarted/)

---

## 设置完成后的下一步

1. **创建你的第一个方块** - 添加自定义方块，包含模型和纹理
2. **创建你的第一个物品** - 添加自定义物品，正确注册
3. **设置数据生成** - 自动生成 JSON 文件
4. **添加创造模式标签页** - 创建自定义创造模式标签页
5. **实现配方** - 为你的物品添加合成配方

---

## 常见问题及解决方案

| 问题 | 解决方案 |
|------|----------|
| `java.lang.UnsupportedClassVersionError` | 确保在 IDE 中配置了 Java 17 |
| `Could not resolve dependencies` | 检查网络连接，尝试 `./gradlew --refresh-dependencies` |
| `Module not specified` 错误 | 设置 `ideaModule` 属性为 `${project.name}.main` |
| MDK 下载慢 | 使用镜像或等待倒计时 |
| 内存不足 | 在 `gradle.properties` 中增加 Gradle 内存 |
