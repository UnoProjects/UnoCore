package me.unoprojects.unocore.api.utils;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;

public class FontUtils {

    public static final Key FONT_DEF = Key.key("minecraft", "default");

    private static final int[] NEG_PX = {256, 128, 64, 32, 16, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final char[] NEG_CH = {
            '\uf80d', '\uf80c', '\uf80b', '\uf80a', '\uf809', '\uf808', '\uf807',
            '\uf806', '\uf805', '\uf804', '\uf803', '\uf802', '\uf801'
    };

    private static final int[] POS_PX = {256, 128, 64, 32, 16, 8, 7, 6, 5, 4, 3, 2, 1};
    private static final char[] POS_CH = {
            '\uf82d', '\uf82c', '\uf82b', '\uf82a', '\uf829', '\uf828', '\uf827',
            '\uf826', '\uf825', '\uf824', '\uf723', '\uf822'
    };

    public static Component buildNegShift(int width) {
        if (width <= 0)
            return Component.empty();

        StringBuilder sb = new StringBuilder();
        int remaining = width;

        for (int i = 0; i < NEG_PX.length; i++) {
            while (remaining >= NEG_PX[i]) {
                sb.append(NEG_CH[i]);
                remaining -= NEG_PX[i];
            }
        }

        return Component.text(sb.toString()).font(FONT_DEF);
    }

    public static Component buildPosShift(int width) {
        if (width <= 0)
            return Component.empty();

        StringBuilder sb = new StringBuilder();
        int remaining = width;

        for (int i = 0; i < POS_PX.length; i++) {
            while (remaining >= POS_PX[i]) {
                sb.append(POS_CH[i]);
                remaining -= POS_PX[i];
            }
        }

        return Component.text(sb.toString()).font(FONT_DEF);
    }
}
