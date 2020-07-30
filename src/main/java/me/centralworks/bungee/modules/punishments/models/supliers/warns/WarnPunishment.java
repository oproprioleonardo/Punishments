package me.centralworks.bungee.modules.punishments.models.supliers.warns;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WarnPunishment {

    private String id;
    private int amount;
    private String command;

}
