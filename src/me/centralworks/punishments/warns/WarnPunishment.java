package me.centralworks.punishments.warns;

public class WarnPunishment {

    private String id;
    private int amount;
    private String command;

    public WarnPunishment(String id, int amount, String command) {
        this.id = id;
        this.amount = amount;
        this.command = command;
    }

    public WarnPunishment() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
