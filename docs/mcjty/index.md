# McJty Forge 1.20 教程

McJty 的 Forge 1.20 教程系列，基于 https://mcjty.eu/docs/1.20/ep1

## 教程索引

- [Episode 1: 基础](./ep1.md) - 项目设置、模组类、第一个方块

## Episode 1 内容概要

### 项目设置

1. 从 Forge 下载站点下载最新 MDK
2. 复制以下文件到新目录：
   - `gradle/` 文件夹
   - `src/` 文件夹
   - `gradlew.bat` 和 `gradlew`
   - `settings.gradle`, `build.gradle`, `gradle.properties`
   - `.gitignore`

### gradle.properties

Forge 1.20+ 的所有配置都可在 `gradle.properties` 中设置。

### Mappings

Minecraft 以混淆形式分发。有两种流行的映射方式：
- **official**: Mojang 的官方映射
- **parchment**: 带额外参数的 Mojang 映射

### 主模组类结构

```java
@Mod(Tutorial1Basics.MODID)
public class Tutorial1Basics {
    public static final String MODID = "tut1basics";

    // 使用 DeferredRegister
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    // 注册块和物品
    public static final RegistryObject<Block> EXAMPLE_BLOCK =
        BLOCKS.register("example_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));
    public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM =
        ITEMS.register("example_block",
            () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));

    public Tutorial1Basics() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 通用设置代码
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            event.accept(EXAMPLE_BLOCK_ITEM);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // 服务器启动时
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // 客户端设置代码
        }
    }
}
```

### Minecraft 核心概念

| 概念 | 说明 |
|------|------|
| Definitions | 游戏中只有一份实例（如钻石剑） |
| Inventory | 用 ItemStack 表示 |
| World | 用 BlockState 表示（方块状态） |

### 事件总线

| 总线 | 用途 |
|------|------|
| Mod | 生命周期事件（FMLCommonSetupEvent, FMLClientSetupEvent 等） |
| Forge | 游戏事件（ServerStartingEvent, EntityJoinLevelEvent 等） |

### 注册时机

- 注册发生在很早的阶段，在配置加载之前
- **不能在注册时依赖配置值**
- **不要条件注册**

## 相关链接

- 视频: https://youtu.be/BpUbD0NXfp8
- GitHub: https://github.com/McJty/Tut4_1Basics
