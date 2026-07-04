package me.unoprojects.unocore.commands.sub;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.SubCommand;
import me.unoprojects.unocore.api.modules.UnoPlugin;
import me.unoprojects.unocore.api.utils.ColorUtils;

import me.unoprojects.unocore.api.permissions.Permission;

import java.util.Collection;

public class ListSubCommand extends SubCommand<UnoCore> {

    public ListSubCommand(UnoCore plugin) {
        super(plugin, "list");
    }

    @Override
    protected void setup() {
        withPermission(Permission.COMMAND_LIST.getPermission());
        executes((sender, args) -> {
            Collection<UnoPlugin> modules = plugin.getModuleManager().getModules();
            if (modules.isEmpty()) {
                sender.sendMessage(ColorUtils.parse("<red>No modules found on the server."));
                return;
            }

            sender.sendMessage(ColorUtils.parse("<gold>=== UnoCore Modules ==="));
            for (UnoPlugin module : modules) {
                String status = module.isEnabled() ? "<green>[Enabled]" : "<red>[Disabled]";
                sender.sendMessage(ColorUtils.parse(status + " <yellow>" + module.getName() + " <gray>(v" + module.getDescription().getVersion() + ")"));
            }
        });
    }
}
