package me.centralworks.modules.reports.enums;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public enum ReportState implements Serializable {

    @SerializedName("UNDER_ANALYSIS")
    UNDER_ANALYSIS,
    @SerializedName("AWAITING_ANALYSIS")
    AWAITING_ANALYSIS

}
