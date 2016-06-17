package com.hrkalk.zetapower.client.input;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class InputHandler {
    public static KeyBinding ping;
    public static KeyBinding pong;

    public static void init() {
        // Define the "ping" binding, with (unlocalized) name "key.ping" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 24 ("O", LWJGL constant: Keyboard.KEY_O)
        ping = new KeyBinding("key.ping", Keyboard.KEY_O, "key.categories.zetapower");

        // Define the "pong" binding, with (unlocalized) name "key.pong" and
        // the category with (unlocalized) name "key.categories.mymod" and
        // key code 25 ("P", LWJGL constant: Keyboard.KEY_P)
        pong = new KeyBinding("key.pong", Keyboard.KEY_P, "key.categories.zetapower");

        // Register both KeyBindings to the ClientRegistry
        ClientRegistry.registerKeyBinding(ping);
        ClientRegistry.registerKeyBinding(pong);
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if (ping.isPressed())
            System.out.println("ping");
        if (pong.isPressed())
            System.out.println("pong");
    }
}
