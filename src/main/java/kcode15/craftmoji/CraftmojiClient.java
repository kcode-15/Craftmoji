package dev.kcode15.craftmoji;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;

public final class CraftmojiClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        EmojiCatalog.load();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommandManager.literal("craftmoji")
                        .executes(context -> {
                            MinecraftClient.getInstance().setScreen(new EmojiPickerScreen());
                            return 1;
                        })));
    }
}
