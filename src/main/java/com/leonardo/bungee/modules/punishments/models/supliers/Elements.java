package com.leonardo.bungee.modules.punishments.models.supliers;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.Reasons;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentState;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor
public class Elements {

    private Long startedAt, finishAt;
    private String reason, punisher;
    private PunishmentState punishmentState;
    private List<String> evidences = Lists.newArrayList();
    private PunishmentType punishmentType;
    private boolean permanent;

    public String getReasonString() {
        return this.reason;
    }

    public Reason getReason() {
        return Reasons.getInstance().getByReason(getReasonString());
    }

    public Date getStartDate() {
        return new Date(getStartedAt());
    }

    public Date getFinishDate() {
        return new Date(getFinishAt());
    }

    public Timestamp getStartDateSQL() {
        return new Timestamp(getStartedAt());
    }

    public Timestamp getFinishDateSQL() {
        return new Timestamp(getFinishAt());
    }

    public String getEvidencesFinally() {
        return getEvidences().isEmpty() ? "Nenhuma anexada" : evidences.get(0);
    }


}
