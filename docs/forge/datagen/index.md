# Data Generation - 数据生成

## 概述

数据生成器是以编程方式生成模组资源和数据的系统。允许在代码中定义文件内容并自动生成。

## 运行数据生成

```bash
./gradlew runData
```

## 数据生成模式

| 模式 | 命令行参数 | 说明 |
|------|------------|------|
| Client Assets | `--client` | 生成客户端文件：block/item 模型、blockstate JSON、语言文件等 |
| Server Data | `--server` | 生成服务端文件：配方、进度、标签等 |
| Development Tools | `--dev` | 开发工具：SNBT 转 NBT 等 |
| Reports | `--reports` | 转储所有已注册的 blocks、items、commands 等 |
| All | `--all` | 生成所有数据 |

## GatherDataEvent

```java
@Mod.EventBusSubscriber(modid = "mymod", bus = Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();

        // 客户端资源
        event.includeClient();
        generator.addProvider(event.includeClient(),
            new MyLanguageProvider(output, "mymod", "en_us"));

        // 服务端数据
        event.includeServer();
        generator.addProvider(event.includeServer(),
            new MyRecipeProvider(output));
        generator.addProvider(event.includeServer(),
            new MyLootTableProvider(output));
        generator.addProvider(event.includeServer(),
            new MyTagProvider(output, event.getLookupProvider(), helper));
    }
}
```

## 客户端资源提供器

### LanguageProvider - 语言文件

```java
public class MyLanguageProvider extends LanguageProvider {
    public MyLanguageProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        add("item.mymod.example_item", "Example Item");
        add("block.mymod.example_block", "Example Block");
    }
}
```

### BlockStateProvider - 方块状态

```java
public class MyBlockStateProvider extends BlockStateProvider {
    public MyBlockStateProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, "mymod", helper);
    }

    @Override
    public void registerStatesAndModels() {
        // 定义方块状态和模型
    }
}
```

### ItemModelProvider - 物品模型

```java
public class MyItemModelProvider extends ItemModelProvider {
    public MyItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, "mymod", helper);
    }

    @Override
    public void registerModels() {
        // 定义物品模型
    }
}
```

## 服务端数据提供器

### RecipeProvider - 配方

```java
public class MyRecipeProvider extends RecipeProvider {
    public MyRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.DIAMOND)
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .define('#', Items.EMERALD)
            .unlockedBy("has_emerald", has(Items.EMERALD))
            .save(writer);
    }
}
```

### TagsProvider - 标签

```java
public class MyTagProvider extends TagsProvider<Block> {
    public MyTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                         ExistingFileHelper helper) {
        super(output, ForgeRegistries.Keys.BLOCKS, lookupProvider, "mymod", helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ExampleBlocks.MY_BLOCK.get());
    }
}
```

### LootTableProvider - 战利品表

```java
public class MyLootTableProvider extends LootTableProvider {
    public MyLootTableProvider(PackOutput output) {
        super(output, Collections.emptySet(),
            List.of(new LootTableProvider.SubProviderEntry(MyLootTables::new, LootTableParameterSet.BLOCK)));
    }

    @Override
    public void buildTables() {
        // 创建战利品表
    }
}
```

### AdvancementProvider - 进度

```java
public class MyAdvancementProvider extends AdvancementProvider {
    public MyAdvancementProvider(PackOutput output, ExistingFileHelper helper,
                                Consumer<Advancement> writer) {
        super(output, helper, List.of(), writer);
    }
}
```

## ExistingFileHelper

用于验证引用文件是否存在：
```java
// 添加模组资源用于验证
// --existing-mod mymod
```

## 自定义数据生成器

实现 `DataProvider` 接口：
```java
public class MyCustomProvider implements DataProvider {
    @Override
    public void run(HolderLookup.Provider caches, PackOutput output) {
        // 生成数据
    }

    @Override
    public String getName() {
        return "my_custom_data";
    }
}
```
