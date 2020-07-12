package me.centralworks.modules.punishments.models.punishs;

public interface Information {

    PunishmentData getData();

    void setData(PunishmentData punishmentData);

    boolean dataIsLoaded();

    String getIp();

    void setIp(String ip);

    boolean ipIsValid();

    Integer getId();

    void setId(Integer id);

    boolean idIsValid();

}
