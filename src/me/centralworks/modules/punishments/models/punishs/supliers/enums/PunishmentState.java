package me.centralworks.modules.punishments.models.punishs.supliers.enums;

import java.util.Arrays;

public enum PunishmentState {

    FINISHED("Finalizada"),
    ACTIVE("Ativa"),
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
