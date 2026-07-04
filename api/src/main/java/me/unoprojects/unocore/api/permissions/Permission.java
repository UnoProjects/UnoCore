package me.unoprojects.unocore.api.permissions;

public enum Permission {

    COMMAND("command"),
    COMMAND_LIST("command.list"),
    COMMAND_ENABLE("command.enable"),
    COMMAND_DISABLE("command.disable"),
    COMMAND_RELOAD("command.reload"),
    ;

    private final String permission;

    Permission(String permission) {
        this.permission = "unocore." + permission;
    }

    public String getPermission() {
        return permission;
    }
}
