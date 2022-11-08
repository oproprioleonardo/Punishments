package com.leonardo.bungee.modules.punishments.models.supliers;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentState;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

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
