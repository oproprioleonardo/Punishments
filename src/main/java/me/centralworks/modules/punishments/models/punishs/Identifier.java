package me.centralworks.modules.punishments.models.punishs;

public interface Identifier {

    String getPrimaryIdentifier();

    void setPrimaryIdentifier(String identifier);

    String getSecondaryIdentifier();

    void setSecondaryIdentifier(String identifier);
}
