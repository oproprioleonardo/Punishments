package me.centralworks.punishments.punishs.supliers.enums;

import java.util.Arrays;

public enum PunishmentType {

    BAN("Banimento", false),
    TEMPBAN("Banimento temporário", true),
    MUTE("Silenciamento", false),
    TEMPMUTE("Silenciamento temporário", true),
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
