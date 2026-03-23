# ShanMod - Minecraft Forge 1.20.1 多模块模组项目

## 项目概述

**注意**：本项目已改造为多模块架构，支持管理多个 Minecraft mod。

- **父项目**: mc_mod (根项目)
- **Minecraft版本**: 1.20.1
- **Forge版本**: 47.3.0
- **Java**: JDK 17 (ms-17.0.16)
- **构建工具**: Gradle

## 多模块结构

```
mc_mod/                           # 根项目（父工程）
├── build.gradle                   # 共享插件配置
├── settings.gradle                # 多模块配置
├── gradle.properties              # 共享版本配置（Minecraft/Forge版本等）
├── gradlew / gradlew.bat
└── submodules/
    └── shanmod/                   # 子模块：shanmod
        ├── build.gradle           # 模块特定配置
        └── src/main/             # 源代码和资源
```

## 多模块注意事项

- **多模块构建命令**: `./gradlew :submodules:模块名:build` 构建单个模块，`./gradlew build` 构建全部
- **ForgeGradle插件放置**: 根项目不要应用 `net.minecraftforge.gradle`，只在子模块的 build.gradle 中应用
- **子模块属性访问**: gradle.properties 中的属性在子模块中需要用 `project.ext.属性名` 访问，不能直接用属性名
- **processResources注意**: ForgeGradle 的 ProcessResources 有内置属性验证，不要用 `inputs.properties` 绑定会触发验证的属性
- **gradle.properties 必须包含 mod_version**: 多模块迁移时必须确保 `mod_version=1.0.0` 在根 gradle.properties 中，否则 mods.toml 中 version 展开为 null 导致 mod 加载失败
- **processResources expand 闭包属性**: ForgeGradle 的 `expand()` 闭包中引用的变量必须是本地 def 变量，不能依赖 project.ext（闭包求值时机晚于 project.ext 设置）
- **Minecraft mods 文件夹不会自动更新**: 重新构建后需要手动复制 jar 到 Minecraft mods 文件夹，build/libs/ 中的 jar 不会自动同步

## 构建命令

```bash
# 构建所有模块
JAVA_HOME="/c/Users/WINDOWS/.jdks/ms-17.0.16" ./gradlew build --no-daemon

# 仅构建 shanmod 模块
JAVA_HOME="/c/Users/WINDOWS/.jdks/ms-17.0.16" ./gradlew :submodules:shanmod:build --no-daemon

# 清理
JAVA_HOME="/c/Users/WINDOWS/.jdks/ms-17.0.16" ./gradlew clean --no-daemon
```

**IDE测试**: 使用 IntelliJ IDEA 打开根项目（mc_mod），运行 `runClient` 配置启动游戏

## 子模块：shanmod

### 项目结构
```
submodules/shanmod/src/main/java/com/github/maoshan/shanmod/
├── ShanMod.java              # 模组主类，负责初始化和注册
├── command/
│   └── ModCommands.java      # 命令注册，/ms xmnxy 给予护身符
├── event/
│   └── ModEvents.java        # 玩家Tick事件，检测背包物品并应用效果
├── init/
│   ├── ModBlocks.java        # 方块注册（当前仅示例方块）
│   ├── ModCreativeTabs.java  # 创造模式标签页注册
│   └── ModItems.java         # 物品注册，包括护身符
└── item/
    └── AmuletItem.java       # 护身符物品类，处理工具提示显示
```

### 关键配置
- `submodules/shanmod/src/main/resources/META-INF/mods.toml`: 模组描述、依赖、入口类配置
- `gradle.properties`: 共享的 MC/Forge 版本配置在根目录
- 物品纹理路径: `submodules/shanmod/src/main/resources/assets/shanmod/textures/item/`
- 语言文件: `submodules/shanmod/src/main/resources/assets/shanmod/lang/`

### 模组功能

#### 猫山的庇护 (AmuletItem)
| 属性 | 值 |
|------|-----|
| 物品ID | shanmod:amulet |
| 物品名称 | 猫山的庇护 |
| 工具提示 | 得到猫山的庇护，物品在背包内即获得无敌状态。 |
| 最大堆叠 | 1 |

**效果列表**：
| 效果 | 等级 | 实现方式 |
|------|------|---------|
| 伤害抗性 | 5级 | MobEffectInstance |
| 击退抗性 | 满级 | Attributes.KNOCKBACK_RESISTANCE 属性 |
| 防火 | 满级 | MobEffectInstance |
| 夜视 | 满级 | MobEffectInstance |
| 生命恢复 | 血量不满时自动恢复 | MobEffectInstance (REGENERATION) |
| 炫酷光环 | 持续显示 | ServerLevel.sendParticles |

**触发逻辑**：
1. `ModEvents.onPlayerTick()` 每tick执行
2. `hasAmulet()` 遍历 `player.getInventory().items` 检测护身符
3. 有护身符 → 添加所有效果
4. 无护身符 → 移除所有效果

**命令**：
- `/ms xmnxy` - 给予玩家一个猫山的庇护（需要OP权限）

## 语言文件 (zh_cn.json)
```json
{
  "item.shanmod.amulet": "猫山的庇护",
  "item.shanmod.amulet.tooltip": "得到猫山的庇护，物品在背包内即获得无敌状态。",
  "itemGroup.shanmod": "Shan模组"
}
```

## 添加新模块

1. 在 `submodules/` 下创建新目录，如 `submodules/newmod/`
2. 复制 `submodules/shanmod/` 的结构作为模板
3. 在 `settings.gradle` 中添加 `include 'submodules:newmod'`
4. 更新新模块的 `build.gradle` 中的模块名称
5. 更新 `submodules/newmod/src/main/resources/META-INF/mods.toml` 中的 mod id 和描述

## 注意事项

### 物品注册（重要）
```java
// ✅ 正确：注册自定义类以启用appendHoverText
public static final RegistryObject<Item> AMULET_ITEM = ITEMS.register(
    "amulet",
    AmuletItem::new
);

// ❌ 错误：自定义方法失效
() -> new Item(new Item.Properties().stacksTo(1))
```

### 背包物品检测
```java
@SubscribeEvent
public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
    if (event.phase != TickEvent.Phase.START) return;
    if (player.level().isClientSide()) return;  // 服务端执行

    boolean hasAmulet = false;
    for (ItemStack stack : player.getInventory().items) {
        if (stack.getItem() == ModItems.AMULET_ITEM.get()) {
            hasAmulet = true;
            break;
        }
    }
}
```

### 永久效果
- 使用 `MobEffectInstance(effect, Integer.MAX_VALUE, level, ...)`
- 不要用短时间（如100 tick），否则效果会频繁过期闪烁

### 物品纹理
- 路径: `submodules/shanmod/src/main/resources/assets/shanmod/textures/item/amulet.png`
- 模型引用: `shanmod:item/amulet`
- Minecraft会自动在 `textures/item/` 子目录查找纹理

### Forge事件注册
```java
// 在主类的构造函数中注册
MinecraftForge.EVENT_BUS.register(new ModEvents());
MinecraftForge.EVENT_BUS.register(new ModCommands());
```

## 历史问题记录

### 事件总线注册（重要）
带有 `@SubscribeEvent` 注解的主类方法必须显式注册到 `MinecraftForge.EVENT_BUS`：
```java
// 在主类构造函数中注册
MinecraftForge.EVENT_BUS.register(this);
```
**症状**：命令不存在、`ServerStartingEvent` 不触发、服务端事件完全不生效
**原因**：主类有 `@SubscribeEvent` 方法但未调用 `register(this)`

### Curios 饰品系统（已移除）
- 原计划使用Curios让物品可装备到项链槽位
- 因版本兼容性问题（5.14.1与5.11.0 API差异）放弃
- 改用原生背包检测方案，更简单稳定

### 已解决
- ✅ `getTagsTooltip` 返回类型必须是 `List<Component>` 不是 `void`
- ✅ `ModItems` 必须注册 `AmuletItem` 类而非直接 `new Item()`
- ✅ 效果持续时间必须足够长，否则会闪烁

## 粒子效果（ModEvents）
```java
// 服务端发送粒子（需要转换为ServerLevel）
((ServerLevel) player.level()).sendParticles(
    ParticleTypes.END_ROD, x, y, z, count, vx, vy, vz, speed);
```
- 常用粒子: `END_ROD`(光柱拖尾)、`DRAGON_BREATH`(龙息)、`MOB_SPELL`(魔法光环)、`SOUL`(灵魂火)
- 炫酷组合: 外圈END_ROD + 中圈DRAGON_BREATH + 内圈MOB_SPELL + 顶部SOUL

## 全服广播（ModCommands）
```java
player.server.getPlayerList().broadcastSystemMessage(
    Component.literal("消息内容").withStyle(ChatFormatting.AQUA), false);
```
- `ChatFormatting.AQUA` = 青蓝色鲜明显示
- 第二个参数false表示不仅发送给执行者

## 击退抗性实现
`KNOCKBACK_RESISTANCE` 不是 `MobEffect`，无法用 `addEffect` 添加
必须通过 `Attributes.KNOCKBACK_RESISTANCE` 属性实现：
```java
private static final UUID KB_UUID = UUID.fromString("a1b2c3d4-e5f6-7890-abcd-ef1234567890");
// 添加
player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
    new AttributeModifier(KB_UUID, "Amulet KB Resistance", 1.0, AttributeModifier.Operation.ADDITION));
// 检查
if (player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getModifier(KB_UUID) == null) { ... }
// 移除
player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(KB_UUID);
```

## 随机数使用
```java
import java.util.Random;
private static final Random RANDOM = new Random();
```

## 网络访问
- `WebFetch` 可能对部分域名失败（如 mcjty.eu, minecraft.wiki）
- 如遇失败，使用 Playwright `browser_navigate` 工具代替
- Forge 文档正确路径: `/concepts/events/` 而非 `/events/`

## 本地文档
- 完整开发文档位于 `docs/` 目录
- 主索引: `docs/forge-development-guide.md`
- Forge 官方文档: `docs/forge/`
- McJty 教程: `docs/mcjty/`
- Minecraft Wiki 参考: `docs/mcwiki/`
- 视频教程总结: `docs/video-summaries/`

## 构建输出
- 开发构建: `submodules/shanmod/build/libs/shanmod-1.0.0.jar`
- 混淆重打包: `submodules/shanmod/build/reobfJar/output.jar`
