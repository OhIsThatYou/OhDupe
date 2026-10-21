package com.example.anarsidupe;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public final class AnarsiDupe implements ModInitializer {
    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                Commands.literal("dupe")
                    .then(Commands.argument("adet", IntegerArgumentType.integer(1, 64))
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            ItemStack held = player.getMainHandItem();

                            if (held.isEmpty()) {
                                context.getSource().sendFailure(
                                    Component.literal("Elinde bir eşya tutmalısın.")
                                );
                                return 0;
                            }

                            ItemStack copy = held.copy();
                            int amount = IntegerArgumentType.getInteger(context, "adet");
                            copy.setCount(Math.min(amount, copy.getMaxStackSize()));

                            if (!player.getInventory().add(copy)) {
                                player.drop(copy, false);
                            }

                            context.getSource().sendSuccess(
                                () -> Component.literal("Eşya kopyalandı."),
                                false
                            );
                            return 1;
                        })
                    )
            );
        });
    }
}
