package me.centralworks.punishments.punishs;

import java.util.List;

public interface DAO {

    Punishment requireByPrimaryIdentifier();

    boolean existsPrimaryIdentifier();

    Punishment requireBySecondaryIdentifier();

    boolean existsSecondaryIdentifier();

    Punishment requireById();

    boolean existsById();

    List<Punishment> requireAllByPrimaryIdentifier();

    List<Punishment> requireAllBySecondaryIdentifier();

    Punishment update();

    void save();

    Punishment requireByInstance();

    List<Punishment> requireAllByAddress();

    boolean existsByAddress();

    void delete();

}
