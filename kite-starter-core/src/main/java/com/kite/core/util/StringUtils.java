package com.kite.core.util;

import com.kite.core.constants.KiteConstants;
import java.text.Normalizer;
import java.util.regex.Pattern;

public final class StringUtils {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private static final Pattern NON_ALPHANUMERIC = Pattern.compile("[^a-z0-9-]");
    private static final Pattern DIACRITICS = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");

    private StringUtils() {
        throw new AssertionError("No instances allowed");
    }

    /*
     * ================================
     * Check
     * ================================
     */

    public static boolean hasText(String str) {
        return str != null && !str.isBlank();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /*
     * ================================
     * Normalize (for search/index)
     * ================================
     */

    public static String normalize(String input) {
        if (input == null)
            return null;

        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        normalized = DIACRITICS.matcher(normalized).replaceAll("");
        normalized = normalized.replace("đ", "d").replace("Đ", "D");

        return normalized.toLowerCase();
    }

    /*
     * ================================
     * Slug (SEO safe)
     * ================================
     */

    public static String toSlug(String input) {
        if (input == null)
            return KiteConstants.Character.EMPTY;

        String normalized = normalize(input);
        normalized = normalized.toLowerCase();

        normalized = WHITESPACE.matcher(normalized).replaceAll("-");
        normalized = NON_ALPHANUMERIC.matcher(normalized).replaceAll("");

        // remove duplicate dashes
        return normalized.replaceAll("-{2,}", "-")
                .replaceAll("^-|-$", "");
    }
}
