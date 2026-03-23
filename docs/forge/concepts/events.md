# Events - 事件系统

## 概述

Forge 使用事件总线（Event Bus）允许模组拦截来自各种原版和模组行为的事件。

两个主要事件总线：
- `MinecraftForge#EVENT_BUS` - Forge 事件总线，用于大多数事件
- `FMLJavaModLoadingContext#getModEventBus` - Mod 特定事件总线

## 创建事件处理器

### 实例事件处理器

```java
public class MyForgeEventHandler {
    @SubscribeEvent
    public void pickupItem(EntityItemPickupEvent event) {
        System.out.println("Item picked up!");
    }
}

// 注册
MinecraftForge.EVENT_BUS.register(new MyForgeEventHandler());
```

### 静态事件处理器

```java
public class MyStaticForgeEventHandler {
    @SubscribeEvent
    public static void arrowNocked(ArrowNockEvent event) {
        System.out.println("Arrow nocked!");
    }
}

// 注册 - 传递 Class 而不是实例
MinecraftForge.EVENT_BUS.register(MyStaticForgeEventHandler.class);
```

### 使用 @Mod.EventBusSubscriber 自动注册

```java
@Mod.EventBusSubscriber(modid = "mymod", bus = Bus.FORGE, value = Dist.CLIENT)
public class MyStaticClientOnlyEventHandler {
    @SubscribeEvent
    public static void drawLast(RenderLevelStageEvent event) {
        System.out.println("Drawing!");
    }
}
```

**参数说明**：
- `modid` - 模组 ID
- `bus` - 可以是 `Bus.FORGE` 或 `Bus.MOD`
- `value` - 可选，指定在哪个物理端加载（`Dist.CLIENT` 或 `Dist.DEDICATED_SERVER`）

## 取消事件

如果事件可以取消，它会有 `@Cancelable` 注解：

```java
if (event.isCancelable()) {
    event.setCanceled(true);  // 取消
}
```

**重要**: 不是所有事件都可以取消！尝试取消不可取消的事件会导致游戏崩溃。

## 事件结果

某些事件有 `Event$Result`：

```java
event.setResult(Event.Result.DENY);   // 停止事件
event.setResult(Event.Result.DEFAULT); // 使用原版行为
event.setResult(Event.Result.ALLOW);   // 强制执行
```

## 事件优先级

事件处理器有优先级，使用 `EventPriority` 枚举：

| 优先级 | 说明 |
|--------|------|
| HIGHEST | 最先执行 |
| HIGH | |
| NORMAL | 默认 |
| LOW | |
| LOWEST | 最后执行 |

```java
@SubscribeEvent(priority = EventPriority.HIGH)
public void onEvent(MyEvent event) {
    // 高优先级
}
```

## Mod Event Bus

主要用于模组生命周期事件的监听。

**常用生命周期事件**：
- `FMLCommonSetupEvent` - 通用设置
- `FMLClientSetupEvent` - 客户端设置
- `FMLDedicatedServerSetupEvent` - 专用服务器设置
- `InterModEnqueueEvent` - InterMod 通讯入队
- `InterModProcessEvent` - InterMod 通讯处理

**其他事件**：
- `RegisterColorHandlersEvent` - 颜色处理器注册
- `ModelEvent$BakingCompleted` - 模型烘焙完成
- `TextureStitchEvent` - 纹理缝合
- `RegisterEvent` - 注册事件

## 常见事件示例

### 玩家相关事件

```java
// 玩家登录
@SubscribeEvent
public void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
    // 玩家加入游戏
}

// 玩家重生点设置
@SubscribeEvent
public void onPlayerRespawn(PlayerEvent.PlayerSpawnLocationEvent event) {
    // 设置重生点
}

// 玩家属性修改
@SubscribeEvent
public void onPlayerClone(PlayerEvent.Clone event) {
    // 从备份克隆玩家时
}
```

### 世界相关事件

```java
// 服务器启动
@SubscribeEvent
public void onServerStarting(ServerStartingEvent event) {
    // 服务器启动中
}

// 世界加载
@SubscribeEvent
public void onWorldLoad(WorldEvent.Load event) {
    // 世界加载完成
}

// 方块破坏
@SubscribeEvent
public void onBlockBreak(BlockEvent.BreakEvent event) {
    // 方块被破坏
}
```

### 实体相关事件

```java
// 实体受伤
@SubscribeEvent
public void onEntityDamaged(LivingHurtEvent event) {
    // 实体受伤
}

// 实体死亡
@SubscribeEvent
public void onEntityDeath(LivingDeathEvent event) {
    // 实体死亡
}

// 物品捡起
@SubscribeEvent
public void onItemPickup(EntityItemPickupEvent event) {
    // 实体捡起物品
}
```
