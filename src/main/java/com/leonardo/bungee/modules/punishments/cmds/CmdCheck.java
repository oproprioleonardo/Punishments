package com.leonardo.bungee.modules.punishments.cmds;

import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.GeoIP;
import com.leonardo.bungee.lib.LongMessage;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.AddressIP;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class CmdCheck extends Command {

    public CmdCheck() {
        super("check", "", "scanner");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!Permission.hasPermission(s, Permission.CHECK)) return;
            final Configuration msg = PunishmentPlugin.getMessages();
            final String id = args[0];
            final AddressIP cache = AddressIP.getInstance();
            final AddressIP.AddressIPObject obj;
            if (Functionalities.isAddressIP(id)) {
                if (cache.existsIP(id)) obj = cache.getByAddress(id);
                else {
                    new Message(msg.getString("Messages.invalid-address")).send(s);
                    return;
                }
            } else if (cache.existsPlayer(id)) obj = cache.getByAccount(id);
            else return;
            CompletableFuture.supplyAsync(() -> obj).thenAcceptAsync(u -> {
                final List<String> accounts = u.getAccounts();
                final String accs = accounts.get(0).concat("§f, §7" + accounts.stream().filter(s1 -> !s1.equals(accounts.get(0))).collect(Collectors.joining("§f, §7")));
                final LongMessage lm = new LongMessage("Runners.check");
                lm.setStringList(lm.getColorfulList().stream().map(s1 -> s1
                        .replace("{target}", args[0])
                        .replace("{ip}", u.getHostName())
                        .replace("{country}", GeoIP.get().getCountry(u.getHostName()))
                        .replace("{response}", u.isOnline() ? "§aOnline" : "§cOffline")
                        .replace("{accounts}", accs)).collect(Collectors.toList()));
                lm.send(s);
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.check")).send(s);
        }
    }
}
