package com.totempopchams;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class TotemPopChamsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.level == null) {
                GhostManager.clear();
            } else if (!client.isPaused()) {
                GhostManager.tick();
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> GhostManager.clear());
    }
}
