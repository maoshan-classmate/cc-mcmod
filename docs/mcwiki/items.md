# Minecraft 物品参考

本文档是 Minecraft Wiki 物品页面的本地摘要。

## 原始链接

https://minecraft.wiki/w/Item

## Item.Properties 常用配置

```java
new Item.Properties()
    .tab(CreativeModeTabs.BUILDING_BLOCKS)  // 创造模式标签页
    .stacksTo(64)                           // 最大堆叠数
    .durability(100)                        // 耐久度（非堆叠物品）
    .rarity(Rarity.RARE)                   // 稀有度
    .craftRemainder(Item.getAsh())         // 合成后剩余物品
    .fireResistant()                       // 防火
```

## CreativeModeTabs 常用标签页

| 标签页 | 描述 |
|--------|------|
| BUILDING_BLOCKS | 建筑方块 |
| COLORED_BLOCKS | 有色方块 |
| NATURAL_BLOCKS | 自然方块 |
| functional_blocks | 功能方块 |
| REDSTONE_BLOCKS | 红石方块 |
| MISC | 杂项 |
| FOOD_AND_DRINKS | 食物和饮料 |
| INGREDIENTS | 材料 |
| TOOLS | 工具 |
| COMBAT | 战斗 |
| BREWING | 酿造 |
| SPAWN_EGGS | 刷怪蛋 |

## Rarity 稀有度

| Rarity | 颜色 |
|--------|------|
| COMMON | 白色 |
| UNCOMMON | 黄色 |
| RARE | 蓝色 |
| EPIC | 紫色 |

## 注册物品

```java
public static final DeferredRegister<Item> ITEMS =
    DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

public static final RegistryObject<Item> MY_ITEM =
    ITEMS.register("my_item", () -> new Item(
        new Item.Properties()
            .tab(CreativeModeTabs.MISC)
            .stacksTo(64)
    ));

// 自定义物品类
public static final RegistryObject<Item> MY_CUSTOM_ITEM =
    ITEMS.register("my_custom_item", MyCustomItem::new);
```

## 自定义物品类

```java
public class MyCustomItem extends Item {
    public MyCustomItem() {
        super(new Item.Properties()
            .tab(CreativeModeTabs.MISC)
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
        );
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable("item.mymod.my_custom_item");
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.mymod.my_custom_item.tooltip"));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    @Override
    public InteractionResult useOn(ItemUseContext context) {
        // 右键点击方块时调用
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player,
                                                   InteractionHand hand) {
        // 右键使用时调用
        return super.use(level, player, hand);
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        // 返回 true 使物品有附魔光效
        return true;
    }
}
```

## ToolItem 工具类

### 工具材料

使用 `Tier` 接口或 `TagKey<Tier>` 定义工具材料：

```java
public static final Tier MY_TIER = TierSortingRegistry.registerTier(
    new Tier() {
        @Override
        public int getUses() {
            return 500;
        }

        @Override
        public float getSpeed() {
            return 6.0f;
        }

        @Override
        public float getAttackDamageBonus() {
            return 2.0f;
        }

        @Override
        public int getLevel() {
            return 2;
        }

        @Override
        public int getEnchantmentValue() {
            return 14;
        }

        @Override
        public Ingredient getRepairIngredient() {
            return Ingredient.of(Items.IRON_INGOT);
        }
    },
    new ResourceLocation(MODID, "my_tier"),
    List.of(Tiers.IRON),
    List.of(Tiers.DIAMOND)
);
```

### 创建工具

```java
public static final RegistryObject<SwordItem> MY_SWORD =
    ITEMS.register("my_sword", () ->
        new SwordItem(MY_TIER, 3, -2.4f, new Item.Properties().tab(CreativeModeTabs.COMBAT)));

public static final RegistryObject<PickaxeItem> MY_PICKAXE =
    ITEMS.register("my_pickaxe", () ->
        new PickaxeItem(MY_TIER, 1, -2.8f, new Item.Properties().tab(CreativeModeTabs.TOOLS)));
```

### 工具类型参数

| 工具 | 伤害加成 | 攻击速度惩罚 |
|------|----------|--------------|
| SwordItem | 3 | -2.4 |
| PickaxeItem | 1 | -2.8 |
| AxeItem | 6 | -3.2 |
| ShovelItem | 1.5 | -3.0 |
| HoeItem | 0 | -1.0 |

## 食物 Item

```java
public static final RegistryObject<Item> MY_FOOD =
    ITEMS.register("my_food", () ->
        new Item(new Item.Properties()
            .tab(CreativeModeTabs.FOOD_AND_DRINKS)
            .food(new Food.Builder()
                .nutrition(4)        // 饱食度
                .saturationMod(2.0f) // 饱和度
                .meat()              // 可用于狗
                .alwaysEat()         // 总是可以吃（即使饱食）
                .effect(() -> new MobEffectInstance(MobEffects.JUMP_BOOST, 200), 1.0f)
                .build())
        ));
```

## 盔甲 Item

```java
public static final RegistryObject<ArmorItem> MY_HELMET =
    ITEMS.register("my_helmet", () ->
        new ArmorItem(MY_ARMOR_MATERIAL, ArmorItem.Type.HELMET,
            new Item.Properties().tab(CreativeModeTabs.COMBAT)));

public static final RegistryObject<ArmorItem> MY_CHESTPLATE =
    ITEMS.register("my_chestplate", () ->
        new ArmorItem(MY_ARMOR_MATERIAL, ArmorItem.Type.CHESTPLATE,
            new Item.Properties().tab(CreativeModeTabs.COMBAT)));

// 注册盔甲材料
public static final ArmorMaterial MY_ARMOR_MATERIAL =
    TierSortingRegistry.registerArmorMaterial(
        "my_armor_material",
        new ResourceLocation(MODID, "textures/model/armor/my_armor_layer_1.png"),
        new int[]{13, 15, 16, 11},  // 护甲值: 头盔, 胸甲, 护腿, 靴子
        new int[]{2, 5, 6, 2},      // 韧性
        () -> Ingredient.of(Items.IRON_INGOT),
        List.of(new ArmorMaterial.Layer(
            new ResourceLocation(MODID, "my_armor"))),
        0.0f
    );
```

## 盾牌 Shield

```java
public static final RegistryObject<ShieldItem> MY_SHIELD =
    ITEMS.register("my_shield", () ->
        new ShieldItem(new Item.Properties()
            .tab(CreativeModeTabs.COMBAT)
            .durability(336)));
```
