package com.bawnorton.midas.command;

import com.bawnorton.midas.access.PlayerEntityAccess;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class CommandHandler {
    public static void init() {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> {
            registerCurseCommand(dispatcher);
            registerCleanseCommand(dispatcher);
        }));
    }

    private static void registerCurseCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("curse").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                    if(player instanceof PlayerEntityAccess playerEntityAccess) {
                        playerEntityAccess.setCursed(true);
                        player.sendMessage(Text.translatable("midas.curse"), false);
                    } else {
                        context.getSource().sendFeedback(Text.translatable("midas.curse.error"), false);
                    }
                    return 1;
                }));
        dispatcher.register(builder);
    }

    private static void registerCleanseCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("cleanse").requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.argument("player", EntityArgumentType.player()).executes(context -> {
                    ServerPlayerEntity player = EntityArgumentType.getPlayer(context, "player");
                    if(player instanceof PlayerEntityAccess playerEntityAccess) {
                        playerEntityAccess.setCursed(false);
                        player.sendMessage(Text.translatable("midas.cleanse"), false);
                    } else {
                        context.getSource().sendFeedback(Text.translatable("midas.cleanse.error"), false);
                    }
                    return 1;
                }));
        dispatcher.register(builder);
    }

}
