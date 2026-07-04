package me.unoprojects.unocore.api.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ColorUtils {

    private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

    public static Component parse(String text) {
        return Component.text("").decoration(TextDecoration.ITALIC, false).append(MINI_MESSAGE.deserialize(text));
    }
}
