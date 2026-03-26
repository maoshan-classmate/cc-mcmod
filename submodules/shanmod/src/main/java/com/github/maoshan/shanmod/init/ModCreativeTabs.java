package com.github.maoshan.shanmod.init;

import com.github.maoshan.shanmod.ShanMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

/**
 * 创造模式标签页注册类
 */
public class ModCreativeTabs {
    // 创造模式标签页 DeferredRegister
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
        DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShanMod.MODID);

    // 自定义创造模式标签页
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> SHAN_TAB =
        CREATIVE_MODE_TABS.register("shan_tab", () ->
            CreativeModeTab.builder()
                .title(Component.translatable("itemGroup." + ShanMod.MODID))
                .withTabsBefore(CreativeModeTabs.COMBAT)
                .icon(() -> ModItems.AMULET_ITEM.get().getDefaultInstance())
                .displayItems((parameters, output) -> {
                    output.accept(ModItems.AMULET_ITEM.get());
                })
                .build()
        );
}
