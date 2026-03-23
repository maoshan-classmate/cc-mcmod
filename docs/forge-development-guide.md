# Minecraft Forge 1.20.1 开发指南

本文档整理自 Forge 官方文档和社区最佳实践，作为 MC mod 开发的参考手册。

---

## 目录

1. [官方资源链接](#官方资源链接)
2. [本地文档索引](#本地文档索引)
3. [环境配置](#环境配置)
4. [项目结构](#项目结构)
5. [核心概念](#核心概念)
6. [注册系统](#注册系统)
7. [事件系统](#事件系统)
8. [常用代码模板](#常用代码模板)
9. [数据生成](#数据生成)
10. [本地化](#本地化)
11. [常见问题](#常见问题)

---

## 官方资源链接

### 英文资源
| 资源 | 链接 |
|------|------|
| Forge 官方文档 | https://docs.minecraftforge.net/en/1.20.1/ |
| Forge 下载页面 | https://files.minecraftforge.net/ |
| McJty 教程 | https://mcjty.eu/docs/1.20/ep1 |
| Kaupenjoe YouTube | https://www.youtube.com/watch?v=55qUIf3GMss |
| Forge Discord | https://discord.gg/UvedJ9m |

### 中文资源
| 资源 | 链接 |
|------|------|
| MC百科 Forge教程 | https://www.mcmod.cn/post/3993.html |
| Forge中文文档 | https://mcforge-cn.readthedocs.io/zh/latest/ |
| 正山小种开发指南 | https://www.teacon.cn/xiaozhong/1.19.x |

---

## 本地文档索引

本文档已整理到本地，方便离线查阅：

### Forge 官方文档（本地）
- [Forge 文档首页](./forge/index.md)
- [Getting Started](./forge/getting-started/index.md)
- [核心概念](./forge/concepts/index.md)
  - [Registries 注册](./forge/concepts/registries.md)
  - [Events 事件](./forge/concepts/events.md)
  - [Sides 端](./forge/concepts/sides.md)
- [数据生成](./forge/datagen/index.md)

### McJty 教程（本地）
- [McJty 索引](./mcjty/index.md)
- [Episode 1 基础](./mcjty/ep1.md)

### Minecraft Wiki 参考（本地）
- [Wiki 索引](./mcwiki/index.md)
- [方块参考](./mcwiki/blocks.md)
- [物品参考](./mcwiki/items.md)
- [附魔参考](./mcwiki/enchantments.md)

### 视频教程总结（本地）
- [视频索引](./video-summaries/index.md)
- [Kaupenjoe 1.20.1](./video-summaries/kaupenjoe-1.20.1.md)

---

## 环境配置

### 系统要求
- **Java**: JDK 17 (推荐 Eclipse Temurin)
- **IDE**: IntelliJ IDEA (推荐) / Eclipse / VS Code
- **Gradle**: 通过 wrapper 自动管理
- **内存**: 建议 8GB+ RAM

### 初始设置步骤

1. **下载 MDK**
   ```
   https://files.minecraftforge.net/net/minecraftforge/forge/index_1.20.1.html
   点击 "Mdk" 下载
   ```

2. **解压并导入 IDE**
   - 解压 MDK 到空目录
   - 用 IntelliJ IDEA 打开 `build.gradle` 作为项目

3. **生成运行配置**
   ```bash
   # IntelliJ
   ./gradlew genIntellijRuns

   # Eclipse
   ./gradlew genEclipseRuns

   # VS Code
   ./gradlew genVSCodeRuns
   ```

4. **首次运行**
   ```bash
   ./gradlew runClient
   ```

### gradle.properties 配置

```properties
# Mod 信息
mod_id=examplemod
mod_name=Example Mod
mod_version=1.0.0
mod_group_id=com.example.examplemod
mod_authors=YourName
mod_description=Example mod

# Minecraft 和 Forge 版本
minecraft_version=1.20.1
minecraft_version_range=[1.20.1,1.21)
forge_version=47.3.0
forge_version_range=[47,48)
loader_version_range=[47,)

# 映射通道
mapping_channel=official
mapping_version=1.20.1
```

---

## 项目结构

### 推荐目录结构

```
src/
├── main/
│   ├── java/
│   │   └── com/example/examplemod/
│   │       ├── ExampleMod.java          # 主类
│   │       ├── init/                     # 注册类
│   │       │   ├── ModBlocks.java
│   │       │   ├── ModItems.java
│   │       │   ├── ModBlockEntities.java
│   │       │   └── ModCreativeTabs.java
│   │       ├── block/                    # 方块类
│   │       │   └── CustomBlock.java
│   │       ├── item/                     # 物品类
│   │       │   └── CustomItem.java
│   │       ├── blockentity/              # 方块实体
│   │       │   └── CustomBlockEntity.java
│   │       ├── menu/                     # 菜单/容器
│   │       ├── client/                   # 客户端专用代码
│   │       │   ├── renderer/
│   │       │   └── screen/
│   │       └── util/                     # 工具类
│   └── resources/
│       ├── META-INF/
│       │   └── mods.toml                 # Mod 元数据
│       └── assets/
│           └── examplemod/
│               ├── textures/             # 纹理
│               ├── models/               # 模型 JSON
│               ├── sounds/               # 音效
│               └── lang/                 # 语言文件
│                   ├── en_us.json
│                   └── zh_cn.json
├── generated/
│   └── resources/                        # 数据生成输出
└── test/
    └── java/                             # 测试代码
```

### 包命名规范

使用反向域名格式:
```
com.yourdomain.modid
```

示例:
| 类型 | 值 | 顶级包 |
|------|-----|--------|
| 域名 | example.com | `com.example` |
| 子域名 | example.github.io | `io.github.example` |
| 邮箱 | your@gmail.com | `com.gmail.your` |

### 类命名规范

常用后缀:
- 方块: `XXXBlock`
- 物品: `XXXItem`
- 方块实体: `XXXBlockEntity`
- 菜单: `XXXMenu`
- 屏幕: `XXXScreen`

---

## 核心概念

### 游戏端 (Sides)

Minecraft 运行在两个端:
- **客户端 (Client)**: 渲染、用户输入、声音
- **服务端 (Server)**: 游戏逻辑、世界保存

单人游戏也有服务端 (集成服务器)。

```java
// 检查当前端
if (FMLLoader.getDist() == Dist.CLIENT) {
    // 客户端代码
}

// 在事件中
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public static class ClientEvents {
    // 仅客户端注册
}
```

### 物品和方块的关系

```
定义 (Definition)     库存 (Inventory)      世界 (World)
     ↓                     ↓                    ↓
   Block               BlockItem            BlockState
(只有一个实例)       (ItemStack引用)      (放置后的状态)
```

**重要**: 方块要能在物品栏显示,必须注册对应的 BlockItem。

---

## 注册系统

### DeferredRegister (推荐方式)

```java
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, "examplemod");

    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register(
        "example_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0f, 3.0f)
            .requiresCorrectToolForDrops())
    );

    // 在主类构造函数中注册
    // BLOCKS.register(modEventBus);
}
```

### 物品注册

```java
public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, "examplemod");

    // 普通物品
    public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register(
        "example_item",
        () -> new Item(new Item.Properties()
            .tab(CreativeModeTabs.MISC))
    );

    // 方块物品 (让方块能在物品栏显示)
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register(
        "example_block",
        () -> new BlockItem(ModBlocks.EXAMPLE_BLOCK.get(),
            new Item.Properties().tab(CreativeModeTabs.BUILDING_BLOCKS))
    );
}
```

### 方块实体注册

```java
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
        DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, "examplemod");

    public static final RegistryObject<BlockEntityType<ExampleBlockEntity>> EXAMPLE_BE =
        BLOCK_ENTITIES.register(
            "example_block_entity",
            () -> BlockEntityType.Builder.of(
                ExampleBlockEntity::new,
                ModBlocks.EXAMPLE_BLOCK.get()
            ).build(null)
        );
}
```

### 注册时机

注册发生在游戏启动早期,早于配置加载。

**重要规则**:
- 不能依赖配置值进行条件注册
- 所有注册对象使用 `RegistryObject` 引用
- 不要在注册时创建实际对象,使用 Supplier

---

## 事件系统

### 两种事件总线

| 总线 | 用途 | 注册方式 |
|------|------|----------|
| Mod Bus | 模组生命周期事件 | `modEventBus` |
| Forge Bus | 游戏运行时事件 | `MinecraftForge.EVENT_BUS` |

### Mod 事件总线

```java
@Mod("examplemod")
public class ExampleMod {
    public ExampleMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册监听器
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::addCreative);

        // 注册 DeferredRegister
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 通用设置
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // 客户端设置
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(ModItems.EXAMPLE_BLOCK_ITEM);
        }
    }
}
```

### Forge 事件总线

```java
@Mod.EventBusSubscriber(modid = "examplemod")
public class CommonEvents {
    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        // 服务器启动
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        // 玩家加入
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        // 方块破坏
    }
}
```

### 静态 vs 非静态方法

| 情况 | 说明 |
|------|------|
| 静态方法 | @SubscribeEvent 自动注册 |
| 非静态方法 | 需要手动注册实例到事件总线 |

---

## 常用代码模板

### 自定义方块

```java
public class CustomBlock extends Block {
    public CustomBlock() {
        super(Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0f, 3.0f)
            .sound(SoundType.WOOD)
            .noOcclusion()  // 透明渲染
        );
    }
}
```

### 自定义物品

```java
public class CustomItem extends Item {
    public CustomItem() {
        super(new Properties()
            .tab(CreativeModeTabs.MISC)
            .stacksTo(64)
            .rarity(Rarity.UNCOMMON)
        );
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("tooltip.examplemod.custom_item"));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
```

### 自定义创造模式标签页

```java
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "examplemod");

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB =
        CREATIVE_MODE_TABS.register("example_tab",
            () -> CreativeModeTab.builder()
                .title(Component.translatable("itemGroup.examplemod"))
                .icon(() -> new ItemStack(ModItems.EXAMPLE_ITEM.get()))
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.EXAMPLE_ITEM.get());
                    output.accept(ModBlocks.EXAMPLE_BLOCK_ITEM.get());
                })
                .build());
}
```

### 方块实体

```java
public class ExampleBlockEntity extends BlockEntity {
    public ExampleBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.EXAMPLE_BE.get(), pos, state);
    }

    public void tick(Level level, BlockPos pos, BlockState state, ExampleBlockEntity entity) {
        // 每tick执行的逻辑
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos,
                                                     BlockState state, T entity) {
        if (entity instanceof ExampleBlockEntity be) {
            be.tick(level, pos, state, be);
        }
    }
}
```

---

## 数据生成

数据生成可以自动创建 JSON 文件,避免手动编写错误。

### 设置数据生成

```java
@Mod("examplemod")
public class ExampleMod {
    public ExampleMod() {
        // ... 其他设置

        // 注册数据生成器
        modEventBus.addListener(GatherDataEvent.class, this::gatherData);
    }

    private void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // 客户端数据
        generator.addProvider(event.includeClient(),
            new ModBlockStateProvider(output, "examplemod", helper));
        generator.addProvider(event.includeClient(),
            new ModItemModelProvider(output, "examplemod", helper));
        generator.addProvider(event.includeClient(),
            new ModLanguageProvider(output, "examplemod", "en_us"));

        // 服务端数据
        generator.addProvider(event.includeServer(),
            new ModRecipeProvider(output));
        generator.addProvider(event.includeServer(),
            new ModLootTableProvider(output));
        generator.addProvider(event.includeServer(),
            new ModBlockTagsProvider(output, event.getLookupProvider(), "examplemod", helper));
    }
}
```

### 运行数据生成

```bash
./gradlew runData
```

生成的文件在 `src/generated/resources/` 目录。

---

## 本地化

### 语言文件位置

```
src/main/resources/assets/yourmodid/lang/
├── en_us.json    # 英语
└── zh_cn.json    # 简体中文
```

### 语言文件格式

```json
{
  "block.examplemod.example_block": "Example Block",
  "item.examplemod.example_item": "Example Item",
  "itemGroup.examplemod": "Example Mod",
  "tooltip.examplemod.custom_item": "This is a custom item",
  "message.examplemod.welcome": "Welcome, %s!"
}
```

### 在代码中使用

```java
// 直接翻译
Component.translatable("block.examplemod.example_block")

// 带参数翻译
Component.translatable("message.examplemod.welcome", player.getName())

// 物品提示
tooltip.add(Component.translatable("tooltip.examplemod.custom_item"));
```

---

## 常见问题

### 1. 启动崩溃: Mod loading error

**原因**: 模组依赖缺失或版本不兼容

**解决**:
- 检查 `mods.toml` 中的依赖版本范围
- 确保所有前置模组已安装

### 2. 方块/物品没有材质

**原因**: 模型或纹理文件缺失或路径错误

**解决**:
- 检查 `assets/modid/models/` 目录
- 确保文件名与注册名匹配
- 使用数据生成避免手动错误

### 3. No model for layer

**原因**: 图层模型未正确注册

**解决**:
- 检查客户端事件是否正确注册
- 确保在 `FMLClientSetupEvent` 中设置渲染器

### 4. 内存不足

**解决**:
在 `gradle.properties` 中增加:
```properties
org.gradle.jvmargs=-Xmx3G
```

### 5. 类加载错误

**原因**: 客户端代码在服务端执行

**解决**:
- 使用 `@Mod.EventBusSubscriber(value = Dist.CLIENT)`
- 使用 `DistExecutor` 或代理模式
- 将客户端代码放在 `client` 子包

---

## 快速参考

### 常用导入

```java
// 注册
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// 方块和物品
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.item.CreativeModeTabs;

// 事件
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;

// 工具
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
```

### ResourceLocation 格式

```java
// 模组内资源
new ResourceLocation("examplemod", "example_block")  // examplemod:example_block

// 原版资源
new ResourceLocation("minecraft", "diamond")         // minecraft:diamond
```

### 调试技巧

```java
// 日志
private static final Logger LOGGER = LogUtils.getLogger();
LOGGER.info("Debug message: {}", value);

// 运行时检查
Validate.notNull(value, "Value cannot be null");
```

---

## 更新日志

- 2026-03-10: 初始版本,基于 Forge 1.20.1 文档整理
- 2026-03-22: 添加本地文档镜像（Forge官方文档、McJty教程、Minecraft Wiki参考、视频教程总结）
