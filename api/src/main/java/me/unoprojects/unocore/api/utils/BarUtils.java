package me.unoprojects.unocore.api.utils;

public class BarUtils {

    private static final int BAR_LENGTH = 5;

    public static String buildBar(double value, double max) {
        int filled = (int) Math.round((value / max) * BAR_LENGTH);
        filled = Math.clamp(filled, 0, BAR_LENGTH);

        String filledColor = switch (filled) {
            case 5, 4 -> "<green>";
            case 3 -> "<yellow>";
            case 2 -> "<gold>";
            case 1 -> "<red>";
            default -> "<gray>";
        };

        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < BAR_LENGTH; i++) {
            if (i < filled) bar.append(filledColor).append("●");
            else bar.append("<gray>●");
        }

        return bar.toString();
    }
}
