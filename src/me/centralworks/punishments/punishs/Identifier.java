package me.centralworks.punishments.punishs;

public interface Identifier {

    /**
     * @return Retorna UUID ou Nickname, depende do modo de jogo do servidor.
     */
    String getPrimaryIdentifier();

    /**
     * @param identifier Define o UUID ou Nickname, depende do modo de jogo do servidor.
     */
    void setPrimaryIdentifier(String identifier);

    String getSecondaryIdentifier();

    void setSecondaryIdentifier(String identifier);
}
