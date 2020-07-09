package me.centralworks.lib;

import de.exceptionflug.protocolize.inventory.Inventory;
import de.exceptionflug.protocolize.inventory.InventoryModule;
import de.exceptionflug.protocolize.inventory.InventoryType;
import de.exceptionflug.protocolize.inventory.event.InventoryClickEvent;
import me.centralworks.Main;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.function.Consumer;

public class InventoryBuilder implements Listener {

    private Inventory inventory = new Inventory(InventoryType.GENERIC_9X4, new TextComponent("Null"));
    private Consumer<InventoryClickEvent> clickEventConsumer;
    private boolean cancellable = true;

    public InventoryBuilder(Inventory inventory, Consumer<InventoryClickEvent> clickEventConsumer, boolean cancellable) {
        this.inventory = inventory;
        this.clickEventConsumer = clickEventConsumer;
        this.cancellable = cancellable;
        Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), this);
    }

    public InventoryBuilder() {
        Main.getInstance().getProxy().getPluginManager().registerListener(Main.getInstance(), this);
    }

    public boolean isCancellable() {
        return cancellable;
    }

    public void setCancellable(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public Consumer<InventoryClickEvent> getClickEventConsumer() {
        return clickEventConsumer;
    }

    public void setClickEvent(Consumer<InventoryClickEvent> clickEventConsumer) {
        this.clickEventConsumer = clickEventConsumer;
    }

    public void open(ProxiedPlayer proxiedPlayer) {
        InventoryModule.sendInventory(proxiedPlayer, getInventory());
    }

    public void close(ProxiedPlayer proxiedPlayer) {
        InventoryModule.closeAllInventories(proxiedPlayer);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (this.inventory == null || event.getInventory() == null) return;
        if (this.inventory.equals(event.getInventory())) {
            if (this.cancellable) event.setCancelled(true);
            if (clickEventConsumer != null) {
                if (event.getClickedItem() == null) return;
                clickEventConsumer.accept(event);
            }
        }
    }
}
