package com.kite.core.util;

import com.kite.core.constants.KiteConstants;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class InstantBuilder {

    private final ZoneId currentZoneID;
    private Instant instanceInstant;

    public InstantBuilder(Instant instant) {
        this(instant, KiteConstants.DateTime.ZONE_VN);
    }

    public InstantBuilder(Instant instant, ZoneId zoneID) {
        this.currentZoneID = zoneID;
        this.instanceInstant = instant;
    }

    public InstantBuilder(LocalDateTime localDateTime) {
        this(localDateTime, KiteConstants.DateTime.ZONE_VN);
    }

    public InstantBuilder(LocalDateTime localDateTime, ZoneId zoneID) {
        this.currentZoneID = zoneID;
        this.instanceInstant = localDateTime.atZone(this.currentZoneID).toInstant();
    }

    public InstantBuilder(String dateTime, DateTimeFormatter formatter) {
        this(dateTime, formatter, KiteConstants.DateTime.ZONE_VN);
    }

    public InstantBuilder(String dateTime, DateTimeFormatter formatter, ZoneId zoneID) {
        this.currentZoneID = zoneID;
        this.instanceInstant = LocalDateTime.parse(dateTime, formatter)
                .atZone(this.currentZoneID)
                .toInstant();
    }

    public InstantBuilder plusYears(int yearsToAdd) {
        this.instanceInstant = ZonedDateTime.ofInstant(instanceInstant, this.currentZoneID)
                .plusYears(yearsToAdd)
                .toInstant();
        return this;
    }

    public InstantBuilder plusMonths(int monthsToAdd) {
        this.instanceInstant = ZonedDateTime.ofInstant(instanceInstant, this.currentZoneID)
                .plusMonths(monthsToAdd)
                .toInstant();
        return this;
    }

    public InstantBuilder plusDays(long daysToAdd) {
        this.instanceInstant = getZonedDateTime().plusDays(daysToAdd).toInstant();
        return this;
    }

    public InstantBuilder plusHours(long hoursToAdd) {
        this.instanceInstant = getZonedDateTime().plusHours(hoursToAdd).toInstant();
        return this;
    }

    public InstantBuilder plusMinutes(long minutesToAdd) {
        this.instanceInstant = getZonedDateTime().plusMinutes(minutesToAdd).toInstant();
        return this;
    }

    public InstantBuilder plusSeconds(long secondsToAdd) {
        this.instanceInstant = instanceInstant.plusSeconds(secondsToAdd);
        return this;
    }

    private ZonedDateTime getZonedDateTime() {
        return instanceInstant.atZone(this.currentZoneID);
    }

    public String getMonthStr() {
        return getZonedDateTime().getMonth().name();
    }

    public String getDayOfWeekStr() {
        return getZonedDateTime().getDayOfWeek().name();
    }

    public int getYear() {
        return getZonedDateTime().getYear();
    }

    public int getDayOfYear() {
        return getZonedDateTime().getDayOfYear();
    }

    public int getDaysOfYear() {
        return Year.of(getYear()).length();
    }

    public int getMonth() {
        return getZonedDateTime().getMonthValue();
    }

    public int getDayOfMonth() {
        return getZonedDateTime().getDayOfMonth();
    }

    public int getDaysOfMonth() {
        return getZonedDateTime().getMonth().length(Year.isLeap(getYear()));
    }

    public int getWeekOfYear() {
        return getZonedDateTime().get(WeekFields.of(Locale.getDefault()).weekOfYear());
    }

    public int getWeekOfMonth() {
        return getZonedDateTime().get(WeekFields.of(Locale.getDefault()).weekOfMonth());
    }

    public int getDayOfWeek() {
        return getZonedDateTime().getDayOfWeek().getValue();
    }

    public int getHour() {
        return getZonedDateTime().getHour();
    }

    public int getMinute() {
        return getZonedDateTime().getMinute();
    }

    public int getSecond() {
        return getZonedDateTime().getSecond();
    }

    public int getMilliSecond() {
        return getZonedDateTime().getNano() / 1_000_000;
    }

    public boolean isBefore(Instant instantToCompare) {
        return this.instanceInstant.isBefore(instantToCompare);
    }

    public boolean isAfter(Instant instantToCompare) {
        return this.instanceInstant.isAfter(instantToCompare);
    }

    public LocalDateTime toLocalDateTime() {
        return LocalDateTime.ofInstant(instanceInstant, this.currentZoneID);
    }

    public LocalDateTime toLocalDateTime(ZoneId zoneID) {
        return LocalDateTime.ofInstant(instanceInstant, zoneID);
    }

    @Override
    public String toString() {
        return this.toString(KiteConstants.DateTime.VN_DATETIME);
    }

    public String toString(DateTimeFormatter formatter) {
        return formatter.withZone(this.currentZoneID).format(instanceInstant);
    }

    public Instant getInstance() {
        return this.instanceInstant;
    }
}
