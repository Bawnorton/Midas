package com.bawnorton.midas.command;

import com.bawnorton.midas.api.MidasApi;
import com.bawnorton.midas.entity.GoldPlayerEntity;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
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
            registerCreateGoldPlayer(dispatcher);
        }));
    }

    private static void registerCurseCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("curse")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(CommandHandler::curse)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(CommandHandler::curse));
        dispatcher.register(builder);
    }

    private static void registerCleanseCommand(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("cleanse")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(CommandHandler::cleanse)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(CommandHandler::cleanse));
        dispatcher.register(builder);
    }

    private static void registerCreateGoldPlayer(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> builder = CommandManager.literal("creategoldentity")
                .requires(source -> source.hasPermissionLevel(2))
                .executes(CommandHandler::createGoldPlayer)
                .then(CommandManager.argument("player", EntityArgumentType.player())
                        .executes(CommandHandler::createGoldPlayer));
        dispatcher.register(builder);
    }

    private static int cleanse(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player;
        try {
            player = EntityArgumentType.getPlayer(context, "player");
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            player = context.getSource().getPlayerOrThrow();
        }
        MidasApi.cleansePlayer(player);
        player.sendMessage(Text.translatable("midas.cleanse"), false);
        return 1;
    }

    private static int curse(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player;
        try {
            player = EntityArgumentType.getPlayer(context, "player");
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            player = context.getSource().getPlayerOrThrow();
        }
        MidasApi.cursePlayer(player);
        player.sendMessage(Text.translatable("midas.curse"), false);
        return 1;
    }

    private static int createGoldPlayer(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ServerPlayerEntity player;
        try {
            player = EntityArgumentType.getPlayer(context, "player");
        } catch (CommandSyntaxException | IllegalArgumentException e) {
            player = context.getSource().getPlayerOrThrow();
        }
        GoldPlayerEntity goldPlayerEntity = GoldPlayerEntity.create(player);
        if(goldPlayerEntity == null) {
            context.getSource().sendFeedback(Text.translatable("midas.create.gold.error"), false);
            return 0;
        }
        goldPlayerEntity.copyPositionAndRotation(player);
        context.getSource().getWorld().spawnEntity(goldPlayerEntity);
        context.getSource().sendFeedback(Text.translatable("midas.create.gold"), false);
        return 1;
    }
}
