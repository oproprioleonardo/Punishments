package me.centralworks.bungee.modules.reports.inventories;

import com.google.common.collect.Lists;
import de.exceptionflug.protocolize.inventory.Inventory;
import de.exceptionflug.protocolize.inventory.InventoryType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.InventoryBuilder;
import me.centralworks.bungee.modules.reports.enums.Permission;
import me.centralworks.bungee.modules.reports.models.Report;
import me.centralworks.bungee.modules.reports.models.ReportedPlayer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ReportsPlayer {

    private ProxiedPlayer p;
    private ReportedPlayer rp;

    public ReportsPlayer(ProxiedPlayer p, ReportedPlayer rp) {
        this.p = p;
        this.rp = rp;
    }

    public boolean open() {
        try {
            final InventoryBuilder inv = new InventoryBuilder();
            inv.setCancellable(true);
            final Inventory i = new Inventory(InventoryType.GENERIC_9X5, new TextComponent("§8" + rp.getUser()));
            final ItemStack back = new ItemStack(ItemType.ARROW);
            back.setDisplayName("§cVoltar");
            back.setLore(Lists.newArrayList("§7Clique para voltar ao menu do jogador."));
            i.setItem(36, back);
            final ItemStack ph = new ItemStack(ItemType.PLAYER_HEAD);
            ph.setDisplayName("§eInformações: ");
            ph.setLore(Lists.newArrayList("§fTotal de denúncias: §7" + rp.getData().size()));
            i.setItem(4, ph);
            final List<Report> collect = rp.getData().subList(0, Math.min(rp.getData().size(), 21));
            if (collect.isEmpty()) return false;
            final ArrayList<Integer> slots = Lists.newArrayList(10, 11, 12, 13, 14, 15, 16, 19, 20, 21, 23, 24, 25, 28, 29, 30, 31, 32, 33, 34);
            for (int count = 0; count < collect.size(); count++) {
                final Report r = collect.get(count);
                final Integer slot = slots.get(count);
                final ItemStack book = new ItemStack(ItemType.BOOK);
                book.setDisplayName("§fID: §7#" + r.getId());
                book.setLore(Lists.newArrayList(
                        "§fData: §7" + new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(r.getDate()).replace("-", " às "),
                        "§fVítima: §7" + r.getVictim(),
                        "§fMotivo: §7" + r.getReason(),
                        "§fProvas: ",
                        General.getGeneralLib().formatEvidences(r.getEvidences())
                ));
                i.setItem(slot, book);
            }
            inv.setClickEvent(e -> {
                final ItemStack item = e.getClickedItem();
                if (rp.exists() || Permission.hasPermission(p, Permission.REPORTS)) {
                    if (item.equals(back)) new ReportInfo(p, rp).open();
                } else inv.close(p);
            });
            inv.setInventory(i);
            inv.open(p);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
