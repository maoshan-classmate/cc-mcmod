package com.github.maoshan.shanmod;

import com.mojang.logging.LogUtils;
import com.github.maoshan.shanmod.command.ModCommands;
import com.github.maoshan.shanmod.event.ModEvents;
import com.github.maoshan.shanmod.init.ModBlocks;
import com.github.maoshan.shanmod.init.ModItems;
import com.github.maoshan.shanmod.init.ModCreativeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

/**
 * Shan Mod - NeoForge 1.21.1 模组主类
 *
 * @author maoshan
 */
@Mod(ShanMod.MODID)
public class ShanMod {
    // 模组 ID - 必须与 gradle.properties 中的 mod_id 一致
    public static final String MODID = "shanmod";

    // 日志记录器
    public static final Logger LOGGER = LogUtils.getLogger();

    public ShanMod(IEventBus modBus) {
        // 注册 DeferredRegister 到模组事件总线
        ModBlocks.BLOCKS.register(modBus);
        ModItems.ITEMS.register(modBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modBus);

        // 注册事件到 NeoForge 事件总线（用于游戏事件如 PlayerTickEvent）
        NeoForge.EVENT_BUS.register(ModEvents.class);
        // 注册命令到 NeoForge 事件总线（RegisterCommandsEvent 在 GAME 总线上）
        NeoForge.EVENT_BUS.register(ModCommands.class);

        LOGGER.info("Shan Mod 正在加载...");
    }
}
