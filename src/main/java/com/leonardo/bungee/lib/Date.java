package com.leonardo.bungee.lib;

import java.util.List;

public class Date {

    private static Date instance;
    private final long minutes;
    private final long hours;
    private final long days;
    private final long weeks;
    private final long months;
    private final long years;

    private Date() {
        this.minutes = 1000L * 60;
        this.hours = 1000L * 60 * 60;
        this.days = 1000L * 60 * 60 * 24;
        this.weeks = 1000L * 60 * 60 * 24 * 7;
        this.months = 1000L * 60 * 60 * 24 * 30;
        this.years = 1000L * 60 * 60 * 24 * 30 * 12;
    }

    public static Date getInstance() {
        if (instance == null) instance = new Date();
        return instance;
    }

    public long getMinutes() {
        return minutes;
    }

    public long getHours() {
        return hours;
    }

    public long getDays() {
        return days;
    }

    public long getWeeks() {
        return weeks;
    }

    public long getMonths() {
        return months;
    }

    public long getYears() {
        return years;
    }

    public Long convertPunishmentDuration(List<String> entries) {
        long duration = 0L;
        for (String entry : entries) {
            final int amount = Integer.parseInt(entry.split(":")[0]);
            final String dateFormat = entry.split(":")[1];
            final long multiplier = dateFormat.equals("minutos") ? minutes : dateFormat.equals("horas") ? hours : dateFormat.equals("dias") ? days : dateFormat.equals("weeks") ? weeks : dateFormat.equals("meses") ? months : years;
            duration += amount * multiplier;
        }
        return duration;
    }


}
