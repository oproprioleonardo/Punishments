package me.centralworks.modules.punishments.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.centralworks.Main;
import me.centralworks.modules.punishments.models.supliers.Elements;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class OfflinePunishment extends Punishment {

    private String primaryIdentifier;

    public OfflinePunishment(Elements elements, String ip, Integer id) {
        super(elements, ip, id);
    }

    public OfflinePunishment(String nickName) {
        this.primaryIdentifier = nickName;
    }

    @Override
    public String getPrimaryIdentifier() {
        return primaryIdentifier;
    }

    @Override
    public void setPrimaryIdentifier(String identifier) {
        this.primaryIdentifier = identifier;
    }

    @Override
    public boolean isOnline() {
        return Main.getInstance().getProxy().getPlayer(getPrimaryIdentifier()) != null;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return Main.getInstance().getProxy().getPlayer(getPrimaryIdentifier());
    }

}
