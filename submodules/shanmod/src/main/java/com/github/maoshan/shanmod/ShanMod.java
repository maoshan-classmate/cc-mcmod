package com.github.maoshan.shanmod;

import com.mojang.logging.LogUtils;
import com.github.maoshan.shanmod.command.ModCommands;
import com.github.maoshan.shanmod.event.ModEvents;
import com.github.maoshan.shanmod.init.ModBlocks;
import com.github.maoshan.shanmod.init.ModItems;
import com.github.maoshan.shanmod.init.ModCreativeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

/**
 * Shan Mod - Minecraft Forge 1.20.1 模组主类
 *
 * @author maoshan
 */
@Mod(ShanMod.MODID)
public class ShanMod {
    // 模组 ID - 必须与 gradle.properties 中的 mod_id 一致
    public static final String MODID = "shanmod";

    // 日志记录器
    public static final Logger LOGGER = LogUtils.getLogger();

    public ShanMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 注册通用设置方法
        modEventBus.addListener(this::commonSetup);

        // 注册 DeferredRegister 到模组事件总线
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        // 注册命令到 Forge 事件总线
        MinecraftForge.EVENT_BUS.register(new ModCommands());
        MinecraftForge.EVENT_BUS.register(new ModEvents());
        MinecraftForge.EVENT_BUS.register(this); // 注册主类以接收 ServerStartingEvent
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 通用设置代码
        LOGGER.info("Shan Mod 正在加载...");
    }

    // 服务器启动事件
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("Shan Mod: 服务器正在启动");
    }

    // 客户端事件 - 使用 EventBusSubscriber 自动注册
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = net.minecraftforge.api.distmarker.Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // 客户端设置代码
            LOGGER.info("Shan Mod: 客户端设置完成");
        }
    }
}
