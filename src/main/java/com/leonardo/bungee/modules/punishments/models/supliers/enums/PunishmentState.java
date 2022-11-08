package com.leonardo.bungee.modules.punishments.models.supliers.enums;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public enum PunishmentState {

    @SerializedName("FINISHED")
    FINISHED("Finalizada"),
    @SerializedName("ACTIVE")
    ACTIVE("Ativa"),
    @SerializedName("REVOKED")
    REVOKED("Revogada");

    private final String identifier;

    PunishmentState(String identifier) {
        this.identifier = identifier;
    }

    public static PunishmentState getByIdentifier(String identifier) {
        return Arrays.stream(PunishmentState.values()).filter(punishmentState -> punishmentState.getIdentifier().equals(identifier)).findFirst().get();
    }

    public String getIdentifier() {
        return identifier;
    }

}
