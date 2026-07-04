package me.unoprojects.unocore.commands.sub;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.SubCommand;
import me.unoprojects.unocore.api.modules.UnoPlugin;
import me.unoprojects.unocore.api.utils.ColorUtils;
import org.bukkit.Bukkit;

import me.unoprojects.unocore.api.permissions.Permission;

import java.util.Optional;

public class DisableSubCommand extends SubCommand<UnoCore> {

    public DisableSubCommand(UnoCore plugin) {
        super(plugin, "disable");
    }

    @Override
    protected void setup() {
        withPermission(Permission.COMMAND_DISABLE.getPermission());
        withArguments(new StringArgument("module").replaceSuggestions(ArgumentSuggestions.strings(info ->
                plugin.getModuleManager().getModules().stream()
                        .filter(UnoPlugin::isEnabled)
                        .map(UnoPlugin::getName)
                        .toArray(String[]::new)
        )));
        executes((sender, args) -> {
            String moduleName = (String) args.get("module");
            Optional<UnoPlugin> moduleOpt = plugin.getModuleManager().getModule(moduleName);
            if (moduleOpt.isEmpty()) {
                sender.sendMessage(ColorUtils.parse("<red>Module '" + moduleName + "' not found."));
                return;
            }

            UnoPlugin module = moduleOpt.get();
            if (!module.isEnabled()) {
                sender.sendMessage(ColorUtils.parse("<red>Module '" + moduleName + "' is already disabled."));
                return;
            }

            Bukkit.getPluginManager().disablePlugin(module);
            sender.sendMessage(ColorUtils.parse("<green>Module '" + moduleName + "' has been disabled."));
        });
    }
}
