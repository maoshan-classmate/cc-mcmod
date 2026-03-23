# 模组文件结构

## 主要文件

### build.gradle

项目的构建配置。定义：
- 模组 ID 和版本
- 依赖管理
- 任务配置

### settings.gradle

Gradle 项目设置。**不要轻易修改**，除非你清楚自己在做什么。

### gradle.properties

Gradle 属性配置：
```properties
# Mod 信息
mod_id=examplemod
mod_name=Example Mod
mod_version=1.0.0
mod_group_id=com.example.examplemod

# Minecraft 和 Forge 版本
minecraft_version=1.20.1
forge_version=47.3.0

# Java
org.gradle.java.home=C:\\Users\\WINDOWS\\.jdks\\ms-17.0.16
```

### src/main/java/

Java 源代码目录。

### src/main/resources/

资源文件目录：
- `META-INF/mods.toml` - 模组元数据
- `assets/` - 资源文件（纹理、模型、语言等）

## mods.toml

模组元数据文件：
```toml
modLoader="javafml"
loaderVersion="[47,)"
license="MIT"

[[mods]]
modId="examplemod"
version="1.0.0"
displayName="Example Mod"
description='''
A description of your mod.
'''

[[dependencies.examplemod]]
modId="forge"
mandatory=true
versionRange="[47,)"
ordering="NONE"
side="BOTH"

[[dependencies.examplemod]]
modId="minecraft"
mandatory=true
versionRange="[1.20.1,1.21)"
ordering="NONE"
side="BOTH"
```

**displayTest** 配置：
- `MATCH_VERSION` - 客户端和服务端版本必须匹配（默认）
- `IGNORE_SERVER_VERSION` - 服务端缺少此模组时不显示红X
- `IGNORE_ALL_VERSION` - 不检查任何版本
- `NONE` - 自定义测试

## 资源目录结构

```
resources/
├── META-INF/
│   └── mods.toml
└── assets/
    └── examplemod/
        ├── blockstates/
        ├── models/
        │   ├── block/
        │   └── item/
        ├── textures/
        │   ├── block/
        │   └── item/
        ├── lang/
        │   ├── en_us.json
        │   └── zh_cn.json
        └── sounds/
            └── definitions.json
```
