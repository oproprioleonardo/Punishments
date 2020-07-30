package me.centralworks.bungee.modules.reports.inventories;

import com.google.common.collect.Lists;
import de.exceptionflug.protocolize.inventory.Inventory;
import de.exceptionflug.protocolize.inventory.InventoryType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.InventoryBuilder;
import me.centralworks.bungee.modules.reports.dao.ReportDAO;
import me.centralworks.bungee.modules.reports.enums.Permission;
import me.centralworks.bungee.modules.reports.models.ReportedPlayer;
import me.centralworks.bungee.modules.reports.models.supliers.ToggleAnnouncement;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class ReportsMain {

    private ProxiedPlayer p;

    public ReportsMain(ProxiedPlayer p) {
        this.p = p;
    }

    public boolean open() {
        try {
            final InventoryBuilder inv = new InventoryBuilder();
            inv.setCancellable(true);
            final Inventory i = new Inventory(InventoryType.GENERIC_9X6, new TextComponent("§8Reportes"));
            final ToggleAnnouncement ta = ToggleAnnouncement.getInstance();
            final ItemStack it = new ItemStack(ItemType.PLAYER_HEAD);
            it.setSkullOwner(p.getName());
            it.setDisplayName("§eInformações: ");
            it.setLore(Lists.newArrayList("§fAlerta de reportes: " + (ta.get(p.getName()) ? "§a§l• §aAtivado" : "§c§l• §cDesativado")));
            i.setItem(10, it);
            final List<Integer> slots = Lists.newArrayList(12, 13, 14, 15, 16, 21, 22, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34, 37, 38, 39, 40, 41, 42, 43);
            final ReportDAO dao = ReportDAO.getInstance();
            final ProxyServer proxy = Main.getInstance().getProxy();
            final List<ReportedPlayer> collect = dao.get().stream().filter(rp -> proxy.getPlayer(rp.getUser()) != null).collect(Collectors.toList());
            if (collect.isEmpty()) return false;
            final List<ReportedPlayer> rps = collect.subList(0, Math.min(collect.size(), 24));
            for (int count = 0; count < rps.size(); count++) {
                final ReportedPlayer rp = rps.get(count);
                final Integer slot = slots.get(count);
                final ItemStack item = new ItemStack(ItemType.PLAYER_HEAD);
                item.setSkullOwner(rp.getUser());
                item.setDisplayName("§f" + rp.getUser() + " §7(" + rp.getData().size() + ")");
                item.setLore(Lists.newArrayList("§7Clique para mais informações."));
                i.setItem(slot, item);
            }
            inv.setClickEvent(e -> {
                if (Permission.hasPermission(p, Permission.REPORTS)) {
                    if (e.getClickedItem().equals(it)) ToggleAnnouncement.getInstance().update(p.getName());
                    if (slots.contains(e.getSlot())) new ReportInfo(p, rps.get(slots.indexOf(e.getSlot()))).open();
                } else inv.close(p);
            });
            inv.setInventory(i);
            inv.open(p);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
