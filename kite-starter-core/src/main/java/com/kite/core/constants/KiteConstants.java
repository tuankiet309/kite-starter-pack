package com.kite.core.constants;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class KiteConstants {

    private KiteConstants() {
        throw new AssertionError("No instances allowed");
    }

    public interface Character {
        String EMPTY = "";
        String SPACE = " ";
        String DOT = ".";
        String COMMA = ",";
        String COLON = ":";
        String UNDERSCORE = "_";
        String DASH = "-";
        String SLASH = "/";
        Charset UTF_8 = StandardCharsets.UTF_8;
    }

    public interface Header {
        String TRACE_ID = "X-Trace-Id";
        String REQUEST_ID = "X-Request-Id";
    }

    public interface System {
        String USER = "SYSTEM";
        String ANONYMOUS = "anonymousUser";
    }

    public interface DateTime {
        // Time zone
        ZoneId ZONE_UTC = ZoneId.of("UTC");
        ZoneId ZONE_VN = ZoneId.of("Asia/Ho_Chi_Minh");
        ZoneId ZONE_US_EAST = ZoneId.of("America/New_York");

        // ISO/API
        String PATTERN_ISO_OFFSET = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
        String PATTERN_ISO_LOCAL = "yyyy-MM-dd'T'HH:mm:ss";
        DateTimeFormatter ISO_OFFSET = DateTimeFormatter.ofPattern(PATTERN_ISO_OFFSET);
        DateTimeFormatter ISO_LOCAL = DateTimeFormatter.ofPattern(PATTERN_ISO_LOCAL);

        // System/Global
        String PATTERN_SYS_DATE = "yyyy-MM-dd";
        String PATTERN_SYS_DATETIME = "yyyy-MM-dd HH:mm:ss";
        DateTimeFormatter SYS_DATE = DateTimeFormatter.ofPattern(PATTERN_SYS_DATE);
        DateTimeFormatter SYS_DATETIME = DateTimeFormatter.ofPattern(PATTERN_SYS_DATETIME);

        // VN
        String PATTERN_VN_DATE = "dd/MM/yyyy";
        String PATTERN_VN_DATETIME = "dd/MM/yyyy HH:mm:ss";
        DateTimeFormatter VN_DATE = DateTimeFormatter.ofPattern(PATTERN_VN_DATE);
        DateTimeFormatter VN_DATETIME = DateTimeFormatter.ofPattern(PATTERN_VN_DATETIME);

        // US
        String PATTERN_US_DATE = "MM/dd/yyyy";
        String PATTERN_US_DATETIME = "MM/dd/yyyy HH:mm:ss";
        DateTimeFormatter US_DATE = DateTimeFormatter.ofPattern(PATTERN_US_DATE);
        DateTimeFormatter US_DATETIME = DateTimeFormatter.ofPattern(PATTERN_US_DATETIME);

        // Flexible Parsing
        List<DateTimeFormatter> SUPPORTED_FORMATTERS = List.of(
                ISO_OFFSET,
                ISO_LOCAL,
                SYS_DATETIME,
                SYS_DATE,
                VN_DATETIME,
                VN_DATE,
                US_DATETIME,
                US_DATE);
    }
}
