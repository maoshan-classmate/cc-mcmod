package com.github.maoshan.shanmod.init;

import com.github.maoshan.shanmod.ShanMod;
import com.github.maoshan.shanmod.item.AmuletItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * 物品注册类
 * 使用 DeferredRegister 模式注册所有物品
 */
public class ModItems {
    // 物品 DeferredRegister
    public static final DeferredRegister<Item> ITEMS =
        DeferredRegister.create(Registries.ITEM, ShanMod.MODID);

    // 猫山的庇护 - 放在背包即生效
    public static final RegistryObject<Item> AMULET_ITEM = ITEMS.register(
        "amulet",
        AmuletItem::new
    );
}
