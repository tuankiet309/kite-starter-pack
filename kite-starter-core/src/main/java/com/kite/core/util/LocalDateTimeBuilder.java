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

public class LocalDateTimeBuilder {

    private final ZoneId currentZoneID;
    private LocalDateTime instanceLocalDateTime;

    public LocalDateTimeBuilder(LocalDateTime localDateTime) {
        this(localDateTime, KiteConstants.DateTime.ZONE_VN);
    }

    public LocalDateTimeBuilder(LocalDateTime localDateTime, ZoneId zoneID) {
        this.currentZoneID = zoneID;
        this.instanceLocalDateTime = localDateTime.atZone(this.currentZoneID).toLocalDateTime();
    }

    public LocalDateTimeBuilder(Instant instant) {
        this(instant, KiteConstants.DateTime.ZONE_VN);
    }

    public LocalDateTimeBuilder(Instant instant, ZoneId zoneID) {
        this.currentZoneID = zoneID;
        this.instanceLocalDateTime = LocalDateTime.ofInstant(instant, this.currentZoneID);
    }

    public LocalDateTimeBuilder(String localDateTime, DateTimeFormatter formatter) {
        this(localDateTime, formatter, KiteConstants.DateTime.ZONE_VN);
    }

    public LocalDateTimeBuilder(String localDateTime, DateTimeFormatter formatter, ZoneId zoneID) {
        this.currentZoneID = zoneID;
        this.instanceLocalDateTime = LocalDateTime.parse(localDateTime, formatter)
                .atZone(this.currentZoneID)
                .toLocalDateTime();
    }

    public LocalDateTimeBuilder plusYears(int yearsToAdd) {
        this.instanceLocalDateTime = this.instanceLocalDateTime.plusYears(yearsToAdd);
        return this;
    }

    public LocalDateTimeBuilder plusMonths(int monthsToAdd) {
        this.instanceLocalDateTime = this.instanceLocalDateTime.plusMonths(monthsToAdd);
        return this;
    }

    public LocalDateTimeBuilder plusDays(long daysToAdd) {
        this.instanceLocalDateTime = this.instanceLocalDateTime.plusDays(daysToAdd);
        return this;
    }

    public LocalDateTimeBuilder plusHours(long hoursToAdd) {
        this.instanceLocalDateTime = this.instanceLocalDateTime.plusHours(hoursToAdd);
        return this;
    }

    public LocalDateTimeBuilder plusMinutes(long minutesToAdd) {
        this.instanceLocalDateTime = this.instanceLocalDateTime.plusMinutes(minutesToAdd);
        return this;
    }

    public LocalDateTimeBuilder plusSeconds(long secondsToAdd) {
        this.instanceLocalDateTime = this.instanceLocalDateTime.plusSeconds(secondsToAdd);
        return this;
    }

    private ZonedDateTime getZonedDateTime() {
        return this.instanceLocalDateTime.atZone(this.currentZoneID);
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

    public boolean isBefore(LocalDateTime localDateTimeToCompare) {
        return this.instanceLocalDateTime.isBefore(localDateTimeToCompare);
    }

    public boolean isAfter(LocalDateTime localDateTimeToCompare) {
        return this.instanceLocalDateTime.isAfter(localDateTimeToCompare);
    }

    public Instant toInstant() {
        return this.instanceLocalDateTime.atZone(this.currentZoneID).toInstant();
    }

    public Instant toInstant(ZoneId zoneID) {
        return this.instanceLocalDateTime.atZone(zoneID).toInstant();
    }

    @Override
    public String toString() {
        return this.toString(KiteConstants.DateTime.VN_DATETIME);
    }

    public String toString(DateTimeFormatter formatter) {
        return formatter.withZone(this.currentZoneID).format(this.instanceLocalDateTime);
    }

    public LocalDateTime getInstance() {
        return this.instanceLocalDateTime;
    }
}
