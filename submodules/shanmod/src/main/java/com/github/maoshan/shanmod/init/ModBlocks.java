package com.github.maoshan.shanmod.init;

import com.github.maoshan.shanmod.ShanMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 方块注册类
 * 使用 DeferredRegister 模式注册所有方块
 */
public class ModBlocks
{
    // 方块 DeferredRegister
    public static final DeferredRegister<Block> BLOCKS =
        DeferredRegister.create(ForgeRegistries.BLOCKS, ShanMod.MODID);

    // 示例方块 - 可以替换为自定义方块类
    public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register(
        "example_block",
        () -> new Block(BlockBehaviour.Properties.of()
            .mapColor(MapColor.STONE)
            .strength(3.0f, 3.0f)
            .requiresCorrectToolForDrops())
    );

    // 在这里添加更多方块...
    // public static final RegistryObject<Block> YOUR_BLOCK = BLOCKS.register("your_block", YourBlock::new);
}
