package com.github.maoshan.shanmod.event;

import com.github.maoshan.shanmod.init.ModItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent.Post;

import java.util.Random;
import java.util.random.RandomGenerator;

public class ModEvents {
    private static final int EFFECT_DURATION = Integer.MAX_VALUE; // 永久效果
    private static final RandomGenerator RANDOM = new Random();
    private static final ResourceLocation KNOCKBACK_RESISTANCE_ID = ResourceLocation.fromNamespaceAndPath("shanmod", "amulet_knockback_resistance");

    /**
     * 检查玩家背包是否含有护身符
     */
    private static boolean hasAmulet(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() == ModItems.AMULET_ITEM.get()) {
                return true;
            }
        }
        return false;
    }

    @SubscribeEvent
    public static void onPlayerTick(Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        boolean hasAmulet = hasAmulet(player);

        if (hasAmulet) {
            // 伤害抗性（5级）
            if (!player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                player.addEffect(new MobEffectInstance(
                    MobEffects.DAMAGE_RESISTANCE,
                    EFFECT_DURATION,
                    4,
                    false,
                    false,
                    true
                ));
            }

            // 击退抗性（通过属性实现）
            if (player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getModifier(KNOCKBACK_RESISTANCE_ID) == null) {
                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).addPermanentModifier(
                    new AttributeModifier(
                        KNOCKBACK_RESISTANCE_ID,
                        1.0,  // 100% 击退抗性
                        AttributeModifier.Operation.ADD_VALUE
                    )
                );
            }

            // 防火（满级）
            if (!player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                player.addEffect(new MobEffectInstance(
                    MobEffects.FIRE_RESISTANCE,
                    EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
                ));
            }

            // 夜视（满级）
            if (!player.hasEffect(MobEffects.NIGHT_VISION)) {
                player.addEffect(new MobEffectInstance(
                    MobEffects.NIGHT_VISION,
                    EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
                ));
            }

            // 生命恢复逻辑
            if (player.getHealth() < player.getMaxHealth()) {
                // 血没满，添加恢复效果
                if (!player.hasEffect(MobEffects.REGENERATION)) {
                    player.addEffect(new MobEffectInstance(
                        MobEffects.REGENERATION,
                        EFFECT_DURATION,
                        0, // 1级恢复，每秒恢复0.5颗心
                        false,
                        false,
                        true
                    ));
                }
            } else {
                // 血已满，移除恢复效果
                if (player.hasEffect(MobEffects.REGENERATION)) {
                    player.removeEffect(MobEffects.REGENERATION);
                }
            }

            // 超炫酷光环效果
            ServerLevel serverLevel = (ServerLevel) player.level();
            long gameTime = player.level().getGameTime();
            double angle = gameTime * 0.15; // 快速旋转

            // 外圈：END_ROD 光柱（白色拖尾）- 半径1.5
            for (int i = 0; i < 4; i++) {
                double a = angle + (i * Math.PI / 2);
                double yOffset = Math.sin(gameTime * 0.1 + i) * 0.3; // 上下浮动
                serverLevel.sendParticles(ParticleTypes.END_ROD,
                    player.getX() + Math.cos(a) * 1.5,
                    player.getY() + 1.0 + yOffset,
                    player.getZ() + Math.sin(a) * 1.5,
                    1, 0.02, 0.02, 0.02, 0.02);
            }

            // 中圈：DRAGON_BREATH 龙息（紫红渐变）- 半径1.0
            for (int i = 0; i < 3; i++) {
                double a = angle + (i * Math.PI * 2 / 3) + 0.5;
                double yOffset = Math.cos(gameTime * 0.08 + i) * 0.2;
                serverLevel.sendParticles(ParticleTypes.DRAGON_BREATH,
                    player.getX() + Math.cos(a) * 1.0,
                    player.getY() + 0.8 + yOffset,
                    player.getZ() + Math.sin(a) * 1.0,
                    1, 0.01, 0.01, 0.01, 0.01);
            }

            // 内圈：WITCH 魔法光环（紫色旋转）- 半径0.5
            for (int i = 0; i < 2; i++) {
                double a = angle * 1.5 + (i * Math.PI);
                serverLevel.sendParticles(ParticleTypes.WITCH,
                    player.getX() + Math.cos(a) * 0.5,
                    player.getY() + 0.5,
                    player.getZ() + Math.sin(a) * 0.5,
                    1, 0, 0, 0, 0);
            }

            // 顶部灵魂火焰向上飘散
            if (gameTime % 5 == 0) { // 每5tick生成一次，节省性能
                serverLevel.sendParticles(ParticleTypes.SOUL,
                    player.getX() + (RANDOM.nextDouble() - 0.5) * 2,
                    player.getY() + 1.5,
                    player.getZ() + (RANDOM.nextDouble() - 0.5) * 2,
                    2, 0, 0.05, 0, 0);
            }
        } else {
            // 如果没有护身符，移除效果
            if (player.hasEffect(MobEffects.DAMAGE_RESISTANCE)) {
                player.removeEffect(MobEffects.DAMAGE_RESISTANCE);
            }
            if (player.hasEffect(MobEffects.REGENERATION)) {
                player.removeEffect(MobEffects.REGENERATION);
            }
            if (player.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                player.removeEffect(MobEffects.FIRE_RESISTANCE);
            }
            if (player.hasEffect(MobEffects.NIGHT_VISION)) {
                player.removeEffect(MobEffects.NIGHT_VISION);
            }
            // 移除击退抗性属性
            if (player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).getModifier(KNOCKBACK_RESISTANCE_ID) != null) {
                player.getAttribute(Attributes.KNOCKBACK_RESISTANCE).removeModifier(KNOCKBACK_RESISTANCE_ID);
            }
        }
    }
}
