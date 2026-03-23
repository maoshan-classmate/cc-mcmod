# Registries - 注册系统

## 概述

注册是将模组中的对象（如物品、方块、音效等）让游戏知道的过程。没有注册，游戏将不知道这些对象的存在，会导致不可解释的行为和崩溃。

Forge 使用 `ResourceLocation` 作为键来注册对象。

## 注册方法

### DeferredRegister（推荐方式）

`DeferredRegister` 允许使用静态初始化器的便利性，同时避免相关问题。

```java
private static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

public static final RegistryObject<Block> ROCK_BLOCK =
    BLOCKS.register("rock", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)));

// 在主类构造函数中
public ExampleMod() {
    BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
}
```

### RegisterEvent

```java
@SubscribeEvent
public void register(RegisterEvent event) {
    event.register(
        ForgeRegistries.Keys.BLOCKS,
        helper -> {
            helper.register(new ResourceLocation(MODID, "example_block_1"), new Block(...));
            helper.register(new ResourceLocation(MODID, "example_block_2"), new Block(...));
        }
    );
}
```

## 引用已注册对象

### 使用 RegistryObject

```java
public static final RegistryObject<Item> BOW =
    RegistryObject.create(new ResourceLocation("minecraft:bow"), ForgeRegistries.ITEMS);
```

### 使用 @ObjectHolder

```java
class Holder {
    @ObjectHolder(registryName = "minecraft:enchantment", value = "minecraft:flame")
    public static final Enchantment flame = null;
}
```

**规则**：
- 字段必须是 `public static`
- 可以指定 `registryName` 和 `value`
- 如果类有 `@Mod` 注解，默认命名空间是 modid

## 创建自定义 Forge 注册

使用 `RegistryBuilder` 创建自定义注册：

```java
private static final DeferredRegister<MyCustomType> CUSTOM_REGISTRY =
    DeferredRegister.create(new ResourceLocation(MODID, "my_custom_registry"), MODID);

// 在类构造函数中，在注册到事件总线之前
public ExampleMod() {
    CUSTOM_REGISTRY.makeRegistry(() -> new RegistryBuilder<MyCustomType>()
        .setName(new ResourceLocation(MODID, "my_custom_registry"))
        .setType(MyCustomType.class));
    CUSTOM_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
}
```

## 处理缺失的映射

当某个注册对象因模组更新或移除而不存在时，使用 `MissingMappingsEvent`：

```java
@SubscribeEvent
public void onMissingMapping(MissingMappingsEvent event) {
    // 获取所有缺失的映射
    for (MissingMappingsEvent.Mapping<?> mapping : event.getAllMappings()) {
        // 处理方式：
        // IGNORE - 忽略
        // WARN - 生成警告
        // FAIL - 阻止世界加载
        // REMAP - 重新映射到另一个已注册对象
        mapping.setAction(MissingMappingsEvent.Action.IGNORE);
    }
}
```

## 常见注册类型

| 类型 | Registry | 备注 |
|------|----------|------|
| Block | BLOCKS | 需要 BlockItem 才能在物品栏显示 |
| Item | ITEMS | |
| BlockEntity | BLOCK_ENTITIES | 需要 BlockEntityType |
| Enchantment | ENCHANTMENTS | |
| Potion | POTIONS | |
| EntityType | ENTITY_TYPES | |
| SoundEvent | SOUND_EVENTS | |
| ParticleType | PARTICLE_TYPES | |
| PotionType | POTION_TYPES | |
