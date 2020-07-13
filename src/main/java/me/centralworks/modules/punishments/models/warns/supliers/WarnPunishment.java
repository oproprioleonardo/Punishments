package me.centralworks.modules.punishments.models.warns.supliers;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class WarnPunishment {

    private String id;
    private int amount;
    private String command;

}
