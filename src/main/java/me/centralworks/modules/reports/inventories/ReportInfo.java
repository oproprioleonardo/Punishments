package me.centralworks.modules.reports.inventories;

import com.google.common.collect.Lists;
import de.exceptionflug.protocolize.inventory.Inventory;
import de.exceptionflug.protocolize.inventory.InventoryType;
import de.exceptionflug.protocolize.items.ItemStack;
import de.exceptionflug.protocolize.items.ItemType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.lib.General;
import me.centralworks.lib.InventoryBuilder;
import me.centralworks.modules.reports.enums.Permission;
import me.centralworks.modules.reports.models.ReportedPlayer;
import me.centralworks.modules.reports.models.supliers.Request;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
@RequiredArgsConstructor
public class ReportInfo {

    private ProxiedPlayer p;
    private ReportedPlayer rp;

    public ReportInfo(ProxiedPlayer p, ReportedPlayer rp) {
        this.p = p;
        this.rp = rp;
    }

    public boolean open() {
        try {
            final InventoryBuilder inv = new InventoryBuilder();
            inv.setCancellable(true);
            final Inventory i = new Inventory(InventoryType.GENERIC_9X3, new TextComponent("§8" + rp.getUser()));
            final ItemStack punish = new ItemStack(ItemType.RED_WOOL);
            punish.setDisplayName("§cPunir");
            punish.setLore(Lists.newArrayList("§7Clique para punir o jogador."));
            i.setItem(11, punish);
            final ItemStack delete = new ItemStack(ItemType.BARRIER);
            delete.setDisplayName("§cClique para deletar o reporte sem punição.");
            delete.setLore(Lists.newArrayList("§7Clique para marcar o reporte como visto."));
            i.setItem(15, delete);
            final ItemStack reportes = new ItemStack(ItemType.PLAYER_HEAD);
            reportes.setSkullOwner(rp.getUser());
            reportes.setDisplayName("§f" + rp.getUser() + " §7(" + rp.getData().size() + ")");
            reportes.setLore(Lists.newArrayList("§7Clique para acessar todos os reportes contra esse jogador."));
            i.setItem(13, reportes);
            inv.setClickEvent(e -> {
                final ItemStack item = e.getClickedItem();
                if (rp.exists() || Permission.hasPermission(p, Permission.REPORTS)) {
                    inv.close(p);
                    if (item.equals(punish)) {
                        General.getGeneralLib().sendPunishments(p, rp.getUser());
                        new Request(rp).delete();
                    } else if (item.equals(delete)) {
                        new Request(rp).delete();
                        new ReportsMain(p).open();
                    } else if (item.equals(reportes)) new ReportsPlayer(p, rp).open();
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
