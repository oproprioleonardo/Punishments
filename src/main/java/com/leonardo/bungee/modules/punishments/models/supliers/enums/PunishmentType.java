package com.leonardo.bungee.modules.punishments.models.supliers.enums;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public enum PunishmentType {

    @SerializedName("BAN")
    BAN("Banimento", false),
    @SerializedName("TEMPBAN")
    TEMPBAN("Banimento temporário", true),
    @SerializedName("MUTE")
    MUTE("Silenciamento", false),
    @SerializedName("TEMPMUTE")
    TEMPMUTE("Silenciamento temporário", true),
    @SerializedName("KICK")
    KICK("Suspensão", false);

    private final String identifier;
    private final Boolean temp;

    PunishmentType(String identifier, Boolean temp) {
        this.identifier = identifier;
        this.temp = temp;
    }

    public static PunishmentType getByIdentifier(String identifier) {
        return Arrays.stream(PunishmentType.values()).filter(punishmentType -> punishmentType.getIdentifier().equalsIgnoreCase(identifier)).findFirst().get();
    }

    public String getIdentifier() {
        return identifier;
    }

    public Boolean isTemp() {
        return temp;
    }
}
