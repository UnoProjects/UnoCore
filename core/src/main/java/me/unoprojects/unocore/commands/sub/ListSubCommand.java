package me.unoprojects.unocore.commands.sub;

import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.SubCommand;
import me.unoprojects.unocore.api.modules.UnoPlugin;

import me.unoprojects.unocore.api.permissions.Permission;

import static me.unoprojects.unocore.api.utils.ColorUtils.parse;
import static net.kyori.adventure.text.Component.empty;

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
                sender.sendMessage(parse(" <gradient:#FA982A:#FFC57A><b>UnoCore</b></gradient> <dark_gray>» <red>Nessun modulo trovato sul server."));
                return;
            }

            sender.sendMessage(empty());
            sender.sendMessage(parse(" <gradient:#FA982A:#FFC57A:#FA982A><b>ᴜɴᴏ ᴄᴏʀᴇ</b></gradient> <dark_gray>| <gray>Moduli Registrati"));
            sender.sendMessage(empty());

            for (UnoPlugin module : modules) {
                String name = module.getName();
                String version = module.getDescription().getVersion();
                String statusDot = module.isEnabled() ? "<green>● <gray>Abilitato" : "<red>● <gray>Disabilitato";
                String toggleAction = module.isEnabled() ? "disable" : "enable";
                String actionLabel = module.isEnabled() ? "<red>Clicca per Disattivare" : "<green>Clicca per Attivare";

                sender.sendMessage(parse(
                        "  <dark_gray>» <yellow><b>" + name + "</b> <gray>v" + version + " <dark_gray>• " + statusDot + "   " +
                        "<dark_gray>[<gray><click:run_command:/unocore " + toggleAction + " " + name + "><hover:show_text:'" + actionLabel + "'>⚙ Cambia</hover></click><dark_gray>] " +
                        "<dark_gray>[<gray><click:run_command:/unocore reload " + name + "><hover:show_text:'<yellow>Clicca per Ricaricare il modulo'><gray>↻ Ricarica</hover></click><dark_gray>]"
                ));
            }
            sender.sendMessage(empty());
        });
    }
}
