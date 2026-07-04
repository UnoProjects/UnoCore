package me.unoprojects.unocore.commands.sub;

import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.StringArgument;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.SubCommand;
import me.unoprojects.unocore.api.modules.UnoPlugin;
import org.bukkit.Bukkit;

import me.unoprojects.unocore.api.permissions.Permission;

import static me.unoprojects.unocore.api.utils.ColorUtils.parse;

import java.util.Optional;

public class EnableSubCommand extends SubCommand<UnoCore> {

    private static final String PREFIX = " <gradient:#FA982A:#FFC57A><b>UnoCore</b></gradient> <dark_gray>» <gray>";

    public EnableSubCommand(UnoCore plugin) {
        super(plugin, "enable");
    }

    @Override
    protected void setup() {
        withPermission(Permission.COMMAND_ENABLE.getPermission());
        withArguments(new StringArgument("module").replaceSuggestions(ArgumentSuggestions.strings(info ->
                plugin.getModuleManager().getModules().stream()
                        .filter(m -> !m.isEnabled())
                        .map(UnoPlugin::getName)
                        .toArray(String[]::new)
        )));
        executes((sender, args) -> {
            String moduleName = (String) args.get("module");
            Optional<UnoPlugin> moduleOpt = plugin.getModuleManager().getModule(moduleName);
            if (moduleOpt.isEmpty()) {
                sender.sendMessage(parse(PREFIX + "<red>Il modulo <yellow><b>" + moduleName + "</b></yellow> non è stato trovato."));
                return;
            }

            UnoPlugin module = moduleOpt.get();
            if (module.isEnabled()) {
                sender.sendMessage(parse(PREFIX + "<red>Il modulo <yellow><b>" + moduleName + "</b></yellow> è già abilitato."));
                return;
            }

            Bukkit.getPluginManager().enablePlugin(module);
            sender.sendMessage(parse(PREFIX + "Il modulo <yellow><b>" + moduleName + "</b></yellow> è stato <green>abilitato</green> con successo."));
        });
    }
}
