package me.centralworks.modules.punishments.models.punishs.supliers;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentState;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;

import java.util.List;

@Data
@RequiredArgsConstructor
public class CheckUp {

    private List<Punishment> punishments;

    public CheckUp(List<Punishment> punishments) {
        this.punishments = punishments;
    }

    public boolean hasActivePunishment() {
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentState() == PunishmentState.ACTIVE);
    }

    public boolean hasPunishmentBan() {
        return hasPunishmentType(PunishmentType.BAN, PunishmentType.TEMPBAN);
    }

    public boolean hasPunishmentMute() {
        return hasPunishmentType(PunishmentType.MUTE, PunishmentType.TEMPMUTE);
    }

    public boolean hasPunishmentType(PunishmentType... punishmentTypes) {
        return punishments.stream().anyMatch(punishment -> Lists.newArrayList(punishmentTypes).contains(punishment.getData().getPunishmentType()));
    }
}
