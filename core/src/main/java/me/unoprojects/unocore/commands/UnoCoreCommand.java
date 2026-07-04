package me.unoprojects.unocore.commands;

import dev.jorel.commandapi.CommandAPICommand;
import me.unoprojects.unocore.api.UnoCore;
import me.unoprojects.unocore.api.commands.CommandSupplier;
import me.unoprojects.unocore.api.permissions.Permission;
import me.unoprojects.unocore.commands.sub.DisableSubCommand;
import me.unoprojects.unocore.commands.sub.EnableSubCommand;
import me.unoprojects.unocore.commands.sub.ListSubCommand;
import me.unoprojects.unocore.commands.sub.ReloadSubCommand;

import static me.unoprojects.unocore.api.utils.ColorUtils.parse;
import static net.kyori.adventure.text.Component.empty;

public class UnoCoreCommand extends CommandSupplier {

    public UnoCoreCommand() {
        super("unocore");
    }

    @Override
    public CommandAPICommand[] get() {
        UnoCore plugin = UnoCore.getInstance();

        return new CommandAPICommand[]{
                create(name)
                        .withPermission(Permission.COMMAND.getPermission())
                        .withSubcommands(
                                new ListSubCommand(plugin),
                                new EnableSubCommand(plugin),
                                new DisableSubCommand(plugin),
                                new ReloadSubCommand(plugin)
                        )
                        .executes((sender, args) -> {
                    sender.sendMessage(empty());
                    sender.sendMessage(parse(" <gradient:#FA982A:#FFC57A:#FA982A><b>ᴜɴᴏ ᴄᴏʀᴇ</b></gradient> <dark_gray>- <gray>ɢᴜɪᴅᴀ ᴄᴏᴍᴀɴᴅɪ"));
                    sender.sendMessage(empty());
                    sender.sendMessage(parse(" <dark_gray>» <white>/unocore list <dark_gray>- <gray>Mostra la lista dei moduli"));
                    sender.sendMessage(parse(" <dark_gray>» <white>/unocore enable <modulo> <dark_gray>- <gray>Abilita un modulo"));
                    sender.sendMessage(parse(" <dark_gray>» <white>/unocore disable <modulo> <dark_gray>- <gray>Disabilita un modulo"));
                    sender.sendMessage(parse(" <dark_gray>» <white>/unocore reload <modulo> <dark_gray>- <gray>Ricarica un modulo"));
                    sender.sendMessage(empty());
                })
        };
    }
}
