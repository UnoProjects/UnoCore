package me.unoprojects.template;

import me.unoprojects.template.api.PluginTemplate;

public class DefaultPluginTemplate extends PluginTemplate {

    @Override
    public void onEnable() {
        getLogger().info("Template plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Template plugin disabled.");
    }
}
