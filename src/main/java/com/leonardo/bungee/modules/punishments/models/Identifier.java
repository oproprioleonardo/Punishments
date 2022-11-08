package com.leonardo.bungee.modules.punishments.models;

public interface Identifier {

    String getPrimaryIdentifier();

    void setPrimaryIdentifier(String identifier);

    String getSecondaryIdentifier();

    void setSecondaryIdentifier(String identifier);
}
