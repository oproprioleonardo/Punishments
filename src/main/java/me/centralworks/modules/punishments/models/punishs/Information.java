package me.centralworks.modules.punishments.models.punishs;

import me.centralworks.modules.punishments.models.punishs.supliers.Elements;

public interface Information {

    Elements getData();

    void setData(Elements elements);

    boolean dataIsLoaded();

    String getIp();

    void setIp(String ip);

    boolean ipIsValid();

    Integer getId();

    void setId(Integer id);

    boolean idIsValid();

}
