package com.kite.core.util;

import com.kite.core.constants.KiteConstants;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateUtils {

    private DateUtils() {
        throw new AssertionError("No instances allowed");
    }

    // --- NOW ---

    public static Instant now() {
        return Instant.now();
    }

    public static LocalDate today() {
        return LocalDate.now(KiteConstants.DateTime.ZONE_VN);
    }

    public static LocalDateTime nowDateTime() {
        return LocalDateTime.now(KiteConstants.DateTime.ZONE_VN);
    }

    // --- BUILDERS ---

    public static InstantBuilder builder() {
        return new InstantBuilder(now());
    }

    public static InstantBuilder builder(Instant instant) {
        return new InstantBuilder(instant);
    }

    public static LocalDateTimeBuilder dateTimeBuilder() {
        return new LocalDateTimeBuilder(nowDateTime());
    }

    public static LocalDateTimeBuilder dateTimeBuilder(LocalDateTime localDateTime) {
        return new LocalDateTimeBuilder(localDateTime);
    }

    // --- FORMAT ---

    public static String format(Instant instant) {
        return format(instant, KiteConstants.DateTime.VN_DATETIME);
    }

    public static String format(Instant instant, DateTimeFormatter formatter) {
        if (instant == null)
            return null;
        return formatter.withZone(KiteConstants.DateTime.ZONE_VN).format(instant);
    }

    public static String formatIso(Instant instant) {
        return format(instant, KiteConstants.DateTime.ISO_OFFSET);
    }

    public static String formatDate(LocalDate date) {
        if (date == null)
            return null;
        return date.format(KiteConstants.DateTime.VN_DATE);
    }

    // --- PARSE ---

    public static Instant parse(String value) {
        if (value == null)
            return null;
        return Instant.parse(value);
    }

    public static LocalDate parseDate(String value) {
        if (value == null)
            return null;
        return LocalDate.parse(value, KiteConstants.DateTime.VN_DATE);
    }

    public static LocalDateTime parseDateTime(String value) {
        if (value == null)
            return null;
        return LocalDateTime.parse(value, KiteConstants.DateTime.VN_DATETIME);
    }

    // --- RANGE ---

    public static Instant startOfDay(LocalDate date) {
        if (date == null)
            return null;
        return date.atStartOfDay(KiteConstants.DateTime.ZONE_VN).toInstant();
    }

    public static Instant endOfDay(LocalDate date) {
        if (date == null)
            return null;
        return date.atTime(LocalTime.MAX).atZone(KiteConstants.DateTime.ZONE_VN).toInstant();
    }

    public static Instant startOfMonth(LocalDate date) {
        if (date == null)
            return null;
        return date.withDayOfMonth(1).atStartOfDay(KiteConstants.DateTime.ZONE_VN).toInstant();
    }

    public static Instant endOfMonth(LocalDate date) {
        if (date == null)
            return null;
        return date.withDayOfMonth(date.lengthOfMonth()).atTime(LocalTime.MAX).atZone(KiteConstants.DateTime.ZONE_VN)
                .toInstant();
    }

    // --- CONVERT ---

    public static Instant toInstant(LocalDateTime localDateTime) {
        if (localDateTime == null)
            return null;
        return localDateTime.atZone(KiteConstants.DateTime.ZONE_VN).toInstant();
    }

    public static LocalDateTime toLocalDateTime(Instant instant) {
        if (instant == null)
            return null;
        return LocalDateTime.ofInstant(instant, KiteConstants.DateTime.ZONE_VN);
    }

    public static Date toDate(Instant instant) {
        return instant != null ? Date.from(instant) : null;
    }

    // --- CALCULATE ---

    public static Instant plusDays(Instant instant, long days) {
        return instant != null ? instant.plus(Duration.ofDays(days)) : null;
    }

    public static Instant minusDays(Instant instant, long days) {
        return instant != null ? instant.minus(Duration.ofDays(days)) : null;
    }

    public static long betweenDays(Instant start, Instant end) {
        if (start == null || end == null)
            return 0;
        return Duration.between(start, end).toDays();
    }
}
