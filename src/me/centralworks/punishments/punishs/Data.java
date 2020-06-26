package me.centralworks.punishments.punishs;

public interface Data {

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
