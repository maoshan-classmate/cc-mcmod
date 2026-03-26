package com.github.maoshan.shanmod.command;

import com.github.maoshan.shanmod.init.ModItems;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.common.NeoForge;

public class ModCommands {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        // Register /ms command with subcommands
        dispatcher.register(Commands.literal("ms")
            .requires(source -> source.hasPermission(0)) // Anyone can use
            .then(Commands.literal("xmnxy")
                .executes(ModCommands::giveAmulet)
            )
        );
    }

    private static int giveAmulet(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null) {
            context.getSource().sendFailure(Component.literal("This command can only be used by a player!"));
            return 0;
        }

        // Create the amulet item
        ItemStack amuletStack = new ItemStack(ModItems.AMULET_ITEM.get());

        // Give the amulet to the player
        if (!player.getInventory().add(amuletStack)) {
            // If inventory full, drop at player's position
            player.drop(amuletStack, false);
        }

        // Broadcast to all players
        player.server.getPlayerList().broadcastSystemMessage(
            Component.literal(player.getName().getString() + " 受到了猫山的庇护")
                .withStyle(ChatFormatting.AQUA),
            false
        );

        return 1;
    }
}
