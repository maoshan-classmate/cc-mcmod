# Sides - 客户端/服务端概念

## 四种 Side 类型

### Physical Client（物理客户端）
整个程序，当从启动器启动 Minecraft 时运行的所有线程、进程和服务。

### Physical Server（物理服务端）
专用服务器，整个程序，当启动任何不显示可玩 GUI 的 `minecraft_server.jar` 时运行。

### Logical Server（逻辑服务端）
运行游戏逻辑的地方：生物生成、天气、更新物品栏、生命、AI 等。存在于物理服务端内，但也可以在物理客户端内与逻辑客户端一起运行（单人游戏）。

### Logical Client（逻辑客户端）
接受玩家输入并将其传递给逻辑服务端。同时也从逻辑服务端接收信息并以图形方式展示给玩家。

## 判断方法

### Level#isClientSide

最常用的判断方式，判断**逻辑**端：
```java
if (level.isClientSide) {
    // 逻辑客户端
} else {
    // 逻辑服务端
}
```

**注意**: 单人游戏（逻辑服务端 + 逻辑客户端在同一物理客户端内）此字段为 `false`。

### DistExecutor

处理**物理**端的代码执行：
```java
// 仅在物理客户端运行
DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> ExampleClass::clientMethod);

// 仅在物理服务端运行
DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> ExampleClass::serverMethod);
```

**注意**: 单人游戏的物理端始终是 `Dist.CLIENT`！

### Thread Groups

```java
if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER) {
    // 可能在逻辑服务端
}
```

### FMLEnvironment#dist

获取物理端：
```java
if (FMLEnvironment.dist == Dist.CLIENT) {
    // 物理客户端
}
```

### @OnlyIn 注解

Minecraft 代码中使用，不建议模组直接使用：
```java
// 不推荐
@OnlyIn(Dist.CLIENT)
public void clientOnlyMethod() { }
```

## 常见错误

### 跨越逻辑端

**禁止**：直接从逻辑服务端传递数据到逻辑客户端。

常见错误是通过静态字段：
```java
// 错误示例 - 单人游戏会出问题
public static int sharedValue;  // 不要这样用！

// 错误示例 - 调用客户端专用类
if (!player.level().isClientSide) {
    Minecraft.getInstance().doSomething();  // 会在服务器崩溃！
}
```

**正确做法**：使用网络包（Network Packets）进行跨端通信。

## 单侧模组

单侧模组（只在客户端或服务端运行的模组）应该：
1. 使用 `DistExecutor` 在正确的端注册事件处理器
2. 不注册的端应该什么都不做
3. 设置 `displayTest` 属性

```toml
# mods.toml
[[mods]]
# ...
displayTest="IGNORE_ALL_VERSION"  # 不检查版本匹配
```

自定义显示测试：
```java
ModLoadingContext.get().registerExtensionPoint(
    IExtensionPoint.DisplayTest.class,
    () -> new IExtensionPoint.DisplayTest(
        () -> NetworkConstants.IGNORESERVERONLY,
        (a, b) -> true
    )
);
```
