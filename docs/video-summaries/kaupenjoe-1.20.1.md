# Kaupenjoe Forge 1.20.1 教程总结

原始视频: https://www.youtube.com/watch?v=55qUIf3GMss

## 教程概述

这是 Kaupenjoe 的 Forge 1.20.1 模组开发完整教程，适合新手入门级开发者。

## 章节内容

### 第1部分：项目设置

1. **下载 Forge MDK**
   - 访问 https://files.minecraftforge.net
   - 选择 1.20.1 版本
   - 下载 MDK

2. **IntelliJ IDEA 导入**
   - File -> Open -> 选择 build.gradle
   - 等待 Gradle 同步完成
   - 运行 `./gradlew genIntellijRuns`

3. **运行游戏**
   - 使用 runClient 运行测试

### 第2部分：模组基础

```java
@Mod("modid")
public class MyMod {
    public MyMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // 通用设置
    }
}
```

### 第3部分：DeferredRegister

**方块注册**
```java
public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

public static final RegistryObject<Block> EXAMPLE_BLOCK =
    BLOCKS.register("example_block", () -> new Block(
        BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
    ));
```

**物品注册**
```java
public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

public static final RegistryObject<Item> EXAMPLE_ITEM =
    ITEMS.register("example_item", () -> new Item(
        new Item.Properties().tab(CreativeModeTabs.MISC)
    ));

// 方块物品
public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM =
    ITEMS.register("example_block", () ->
        new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties()));
```

### 第4部分：事件处理

```java
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent event) {
        // 玩家 tick 时执行
    }
}
```

### 第5部分：自定义方块

```java
public class CustomBlock extends Block {
    public CustomBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0f)
            .sound(SoundType.WOOD)
        );
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        // 方块放置时
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        // 邻居方块变化时
    }
}
```

### 第6部分：创造模式标签页

```java
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<CreativeModeTab> MY_TAB =
        CREATIVE_TABS.register("my_tab", () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + MODID))
                .icon(() -> new ItemStack(ModItems.EXAMPLE_ITEM.get()))
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.EXAMPLE_ITEM.get());
                    output.accept(ModBlocks.EXAMPLE_BLOCK_ITEM.get());
                })
                .build());
}
```

### 第7部分：数据生成

```java
@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // 客户端资源
        generator.addProvider(event.includeClient(),
            new ModBlockStateProvider(output, MODID, helper));
        generator.addProvider(event.includeClient(),
            new ModItemModelProvider(output, MODID, helper));
        generator.addProvider(event.includeClient(),
            new ModLanguageProvider(output, MODID, "en_us"));

        // 服务端数据
        generator.addProvider(event.includeServer(),
            new ModRecipeProvider(output));
        generator.addProvider(event.includeServer(),
            new ModLootTableProvider(output));
    }
}
```

### 第8部分：发布模组

1. 运行 `./gradlew build`
2. JAR 文件生成在 `build/libs/`
3. 上传到 CurseForge 或 Modrinth

## 关键要点

1. **始终使用 DeferredRegister** 注册方块和物品
2. **方块物品是必须的** 否则方块无法在物品栏显示
3. **注意端区分** 客户端代码不能直接在服务端运行
4. **使用数据生成** 减少手写 JSON 错误
5. **参考官方文档** https://docs.minecraftforge.net/en/1.20.1/
