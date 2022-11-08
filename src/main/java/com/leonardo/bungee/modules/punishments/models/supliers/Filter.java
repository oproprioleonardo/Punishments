package com.leonardo.bungee.modules.punishments.models.supliers;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentState;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class Filter {

    private List<Punishment> punishments;

    public Filter(List<Punishment> punishments) {
        this.punishments = punishments;
    }

    public List<Punishment> getAllActive() {
        return getByState(PunishmentState.ACTIVE);
    }

    public List<Punishment> getAllFinished() {
        return getByState(PunishmentState.FINISHED);
    }

    public List<Punishment> getAllRevoked() {
        return getByState(PunishmentState.REVOKED);
    }

    public List<Punishment> getAllMutedPActive() {
        setPunishments(getAllActive());
        return getAllMuteP();
    }

    public List<Punishment> getAllBannedPActive() {
        setPunishments(getAllActive());
        return getAllBannedP();
    }

    public List<Punishment> getByState(PunishmentState punishmentState) {
        return punishments.stream().filter(punishment -> punishment.getData().getPunishmentState() == punishmentState).collect(Collectors.toList());
    }

    public List<Punishment> getByStates(PunishmentState... punishmentStates) {
        return punishments.stream().filter(punishment -> Lists.newArrayList(punishmentStates).contains(punishment.getData().getPunishmentState())).collect(Collectors.toList());
    }

    public List<Punishment> getByStates(List<PunishmentState> punishmentStates) {
        return punishments.stream().filter(punishment -> punishmentStates.contains(punishment.getData().getPunishmentState())).collect(Collectors.toList());
    }

    public List<Punishment> getAllBannedP() {
        return getAllByTypes(PunishmentType.BAN, PunishmentType.TEMPBAN);
    }

    public List<Punishment> getAllMuteP() {
        return getAllByTypes(PunishmentType.MUTE, PunishmentType.TEMPMUTE);
    }

    public List<Punishment> getAllByType(PunishmentType punishmentType) {
        return punishments.stream().filter(punishment -> punishment.getData().getPunishmentType() == punishmentType).collect(Collectors.toList());
    }

    public List<Punishment> getAllByTypes(PunishmentType... punishmentTypes) {
        final ArrayList<PunishmentType> types = Lists.newArrayList(punishmentTypes);
        return punishments.stream().filter(punishment -> types.contains(punishment.getData().getPunishmentType())).collect(Collectors.toList());
    }

    public List<Punishment> getAllByTypes(List<PunishmentType> types) {
        return punishments.stream().filter(punishment -> types.contains(punishment.getData().getPunishmentType())).collect(Collectors.toList());
    }

}
