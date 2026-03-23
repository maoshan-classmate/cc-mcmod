# ShanMod - Minecraft Forge 模组

![Minecraft](https://img.shields.io/badge/Minecraft-1.20.1-brightgreen)
![Forge](https://img.shields.io/badge/Forge-47.3.0-orange)
![Java](https://img.shields.io/badge/Java-JDK%2017-blue)

> 本模组完全由 **Claude Code** AI 助手开发。

## 简介

ShanMod 是一个基于 Minecraft Forge 1.20.1 的模组，使用 Java 17 开发。本项目的所有代码、文档及构建配置均由 Claude Code AI 助手编写和整理。

## 功能特性

### 猫山的庇护 (AmuletItem)

当玩家背包中携带「猫山的庇护」护身符时，自动获得以下效果：

| 效果 | 等级 | 说明 |
|------|------|------|
| 伤害抗性 | 5级 | 减少受到的伤害 |
| 击退抗性 | 满级 | 免疫击退效果 |
| 防火 | 满级 | 完全免疫火焰伤害 |
| 夜视 | 满级 | 黑暗中清晰视物 |
| 生命恢复 | 自动 | 血量不满时持续恢复 |
| 炫酷光环 | 持续 | 角色周围显示粒子特效 |

## 快速开始

### 环境要求

- JDK 17 (ms-17.0.16 或更高)
- Minecraft 1.20.1
- Minecraft Forge 47.3.0

### 构建

```bash
# 构建所有模块
./gradlew build

# 仅构建 shanmod 模块
./gradlew :submodules:shanmod:build
```

### 运行

使用 IntelliJ IDEA 打开项目，运行 `runClient` 配置启动游戏。

### 安装

构建完成后，将生成的 jar 文件复制到 Minecraft mods 文件夹：

```
submodules/shanmod/build/libs/shanmod-1.0.0.jar
```

## 指令

| 指令 | 权限 | 说明 |
|------|------|------|
| `/ms xmnxy` | OP | 获得一个猫山的庇护 |

## 项目结构

```
mc_mod/                           # 根项目
├── build.gradle                   # 共享插件配置
├── settings.gradle                # 多模块配置
├── gradle.properties              # 版本配置
└── submodules/
    └── shanmod/                   # 主模组
        └── src/main/
            ├── java/              # Java 源代码
            └── resources/         # 资源文件
```

## 技术栈

- **Minecraft**: 1.20.1
- **Forge**: 47.3.0
- **Java**: JDK 17
- **构建工具**: Gradle 8.8

## 许可证

本项目仅供学习交流，作者保留所有权利。
