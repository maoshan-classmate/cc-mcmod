# Minecraft 附魔参考

本文档是 Minecraft Wiki 附魔页面的本地摘要。

## 原始链接

https://minecraft.wiki/w/Enchanting

## 原版附魔 ID

| 附魔 | ID |
|------|-----|
| 保护 | minecraft:protection |
| 火焰保护 | minecraft:fire_protection |
| 摔落保护 | minecraft:feather_falling |
| 爆炸保护 | minecraft:blast_protection |
| 投射物保护 | minecraft:projectile_protection |
| 水中呼吸 | minecraft:respiration |
| 水下速掘 | minecraft:aqua_affinity |
| 荆棘 | minecraft:thorns |
| 深海探索 | minecraft:depth_strider |
| 冰霜行者 | minecraft:frost_walker |
| 绑定诅咒 | minecraft:binding_curse |
| 锋利 | minecraft:sharpness |
| 亡灵杀手 | minecraft:smite |
| 节肢杀手 | minecraft:bane_of_arthropods |
| 击退 | minecraft:knockback |
| 火焰附加 | minecraft:fire_aspect |
| 抢夺 | minecraft:looting |
| 横扫 | minecraft:sweeping_edge |
| 效率 | minecraft:efficiency |
| 精准采集 | minecraft:silk_touch |
| 耐久 | minecraft:unbreaking |
| 时运 | minecraft:fortune |
| 力量 | minecraft:power |
| 冲击 | minecraft:punch |
| 火矢 | minecraft:flame |
| 无限 | minecraft:infinity |
| 海之眷顾 | minecraft:luck_of_the_sea |
| 饵钓 | minecraft:lure |
| 忠诚 | minecraft:loyalty |
| 穿透 | minecraft:piercing |
| 多重射击 | minecraft:multishot |
| 快速装填 | minecraft:quick_charge |
| 消失诅咒 | minecraft:vanishing_curse |
| 经验修补 | minecraft:mending |
| 激流 | minecraft:riptide |
| 引雷 | minecraft:channeling |
| 激涌 | minecraft:impaling |
| 灵魂疾行 | minecraft:soul_speed |
| 锋利 Valkyrie | minecraft:swift_sneak |

## 在代码中创建附魔

```java
public static final DeferredRegister<Enchantment> ENCHANTMENTS =
    DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);

public static final RegistryObject<Enchantment> MY_ENCHANTMENT =
    ENCHANTMENTS.register("my_enchantment",
        () -> new Enchantment(
            Rarity.RARE,           // 稀有度
            EnchantmentCategory.WEAPON,  // 分类
            new EquipmentSlot[]{EquipmentSlot.MAINHAND}
        ) {
            @Override
            public int getMaxLevel() {
                return 5;
            }

            @Override
            public float getDamageBonus(int level, LivingEntity attacker, Entity target) {
                return level * 0.5f;
            }
        });
```

## 附魔属性

### Rarity（稀有度）

| Rarity | 颜色 | 权重 |
|--------|------|------|
| COMMON | 白色 | 1 |
| UNCOMMON | 绿色 | 2 |
| RARE | 蓝色 | 4 |
| VERY_RARE | 紫色 | 8 |

### EnchantmentCategory（分类）

| Category | 可附魔物品 |
|----------|-----------|
| ALL | 所有物品 |
| ARMOR | 盔甲 |
| ARMOR_HEAD | 头盔 |
| ARMOR_CHEST | 胸甲 |
| ARMOR_LEGS | 护腿 |
| ARMOR_FEET | 靴子 |
| WEAPON | 武器 |
| DIGGER | 工具 |
| FISHING_ROD | 钓鱼竿 |
| TRIDENT | 三叉戟 |
| BREAKABLE | 可损坏物品 |
| BOW | 弓 |
| CROSSBOW | 弩 |
| WEARABLE | 可穿戴物品 |

### EquipmentSlot（装备槽位）

| Slot | 描述 |
|------|------|
| MAINHAND | 主手 |
| OFFHAND | 副手 |
| HEAD | 头部 |
| CHEST | 胸部 |
| LEGS | 腿部 |
| FEET | 脚部 |

## 附魔效果

### 在事件中应用附魔

```java
@SubscribeEvent
public void onLivingHurt(LivingHurtEvent event) {
    if (event.getSource().isProjectile()) {
        // 添加护甲保护附魔效果
        event.setAmount(event.getAmount() * 0.9f);
    }
}
```

### 检测实体是否有附魔

```java
// 检测玩家是否有保护附魔
Player player = event.getPlayer();
int protectionLevel = EnchantmentHelper.getEnchantmentLevel(
    Enchantments.PROTECTION, player);
// 或者
Map<Enchantment, Integer> enchantments =
    EnchantmentHelper.getEnchantments(player.getMainHandItem());
```

### 应用附魔到物品

```java
ItemStack stack = new ItemStack(Items.DIAMOND_SWORD);
// 添加锋利 V
EnchantmentHelper.setEnchantments(
    Map.of(Enchantments.SHARPNESS, 5), stack);
```

## 附魔徽章（EnchantmentGlint）

使物品有附魔光效：
```java
stack.hideTooltip();  // 隐藏所有信息
stack.setEnchantment(Enchantments.SHARPNESS, 1);  // 添加附魔显示
```

## 附魔属性加成计算

### 保护附魔

| 等级 | 伤害减少 |
|------|----------|
| I | 4% |
| II | 8% |
| III | 12% |
| IV | 16% |

### 锋利/亡灵杀手/节肢杀手

| 等级 | 额外伤害 |
|------|----------|
| I | +3 |
| II | +5 |
| III | +7 |
| IV | +9 |
| V | +11 |

### 效率

| 等级 | 挖掘速度加成 |
|------|--------------|
| I | +30% |
| II | +69% |
| III | +118% |
| IV | +177% |
| V | +256% |
