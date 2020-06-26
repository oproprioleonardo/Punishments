package me.centralworks.punishments.punishs;

import java.util.List;

public interface DAO {

    Punishment require();

    boolean exists();

    Punishment requireById();

    boolean existsById();

    List<Punishment> requireAll();

    Punishment update();

    void save();

    Punishment especialRequire();

}
