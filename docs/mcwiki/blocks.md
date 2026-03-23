# Minecraft 方块参考

本文档是 Minecraft Wiki 方块页面的本地摘要。

## 原始链接

https://minecraft.wiki/w/Block

## BlockBehaviour.Properties 常用配置

```java
BlockBehaviour.Properties.of()
    .mapColor(MapColor.STONE)           // 地图颜色
    .strength(3.0f, 3.0f)              // 硬度和爆炸抗性
    .requiresCorrectToolForDrops()      // 需要正确工具掉落
    .sound(SoundType.STONE)             // 音效
    .noOcclusion()                     // 不阻挡光照
    .isRedstoneConductor(...)          // 红石导体
    .isSuffocating(...)                // 是否窒息
    .isViewBlocking(...)               // 是否阻挡视野
```

## 常用 MapColor

| MapColor | 描述 |
|----------|------|
| AIR | 空气（透明） |
| GRASS | 草地颜色 |
| DIRT | 泥土颜色 |
| STONE | 石头颜色 |
| WOOD | 木材颜色 |
| METAL | 金属颜色 |
| GOLD | 金色 |
| DIAMOND | 钻石颜色 |
| LAPIS | 青金石颜色 |
| EMERALD | 绿宝石颜色 |
| NETHER | 下界颜色 |
| ICE | 冰颜色 |
| SNOW | 雪颜色 |
| SAND | 沙子颜色 |

## 常用 SoundType

| SoundType | 描述 |
|-----------|------|
| WOOD | 木质音效 |
| STONE | 石质音效 |
| GRAVEL | 沙砾音效 |
| GRASS | 草地音效 |
| METAL | 金属音效 |
| GLASS | 玻璃音效 |
| WOOL | 羊毛音效 |
| SAND | 沙子音效 |
| SNOW | 雪音效 |
| POWDER_SNOW | 粉雪音效 |

## 方块属性速查

### 硬度 (Hardness)

| 方块 | 硬度 |
|------|------|
| 石头 | 1.5f |
| 圆石 | 2.0f |
| 泥土 | 0.5f |
| 木材 | 2.0f |
| 石头台阶 | 1.5f |

### 爆炸抗性 (Resistance)

| 方块 | 爆炸抗性 |
|------|----------|
| 石头 | 6.0f |
| 黑曜石 | 6000.0f |
| 铁块 | 6.0f |
| 钻石块 | 30.0f |

## 方块状态 (BlockState)

方块状态是方块的具体配置，例如：
- 熔炉的方向（6种）
- 箱子的连接状态
- 楼梯的上下翻转

在代码中获取方块状态：
```java
BlockState state = level.getBlockState(pos);
boolean isPowered = state.getValue(BlockStateProperties.POWERED);
```

## 注册方块

```java
public static final DeferredRegister<Block> BLOCKS =
    DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

public static final RegistryObject<Block> MY_BLOCK =
    BLOCKS.register("my_block", () -> new Block(
        BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0f, 3.0f)
    ));

// 注册对应的 BlockItem（使方块可以拿在手里）
public static final RegistryObject<Item> MY_BLOCK_ITEM =
    ITEMS.register("my_block", () ->
        new BlockItem(MY_BLOCK.get(), new Item.Properties()));
```

## 自定义方块类

```java
public class MyBlock extends Block {
    public MyBlock() {
        super(BlockBehaviour.Properties.of()
            .mapColor(MapColor.WOOD)
            .strength(2.0f, 3.0f)
            .sound(SoundType.WOOD)
        );
    }

    // 可选：重写方法实现自定义行为
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        // 邻居方块变化时调用
    }
}
```

## 方块实体 (BlockEntity)

对于需要存储额外数据或执行 tick 的方块，需要创建 BlockEntity：

```java
public class MyBlockEntity extends BlockEntity {
    public MyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MY_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        // 每 tick 执行
    }
}
```

注册 BlockEntity：
```java
public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
    DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

public static final RegistryObject<BlockEntityType<MyBlockEntity>> MY_BLOCK_ENTITY =
    BLOCK_ENTITIES.register("my_block_entity",
        () -> BlockEntityType.Builder.of(MyBlockEntity::new, MY_BLOCK.get())
            .build(null));
```
