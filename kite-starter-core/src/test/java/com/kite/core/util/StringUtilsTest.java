package com.kite.core.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class StringUtilsTest {

    @Test
    void testNormalize() {
        assertEquals("nguyen van a", StringUtils.normalize("Nguyễn Văn A"));
        assertEquals("d d", StringUtils.normalize("đ Đ"));
        assertEquals("hello @ world!", StringUtils.normalize("Hello @ World!"));
        assertNull(StringUtils.normalize(null));
    }

    @Test
    void testToSlug() {
        assertEquals("bai-viet-moi-2024", StringUtils.toSlug("Bài viết mới 2024!"));
        assertEquals("a-b-c", StringUtils.toSlug("A   B   C"));
        assertEquals("user-profile", StringUtils.toSlug("User Profile"));
        assertEquals("mot-ngay-dep-troi", StringUtils.toSlug("Một ngày đẹp trời"));
        assertEquals("", StringUtils.toSlug(null));
    }

    @Test
    void testHasText() {
        assertTrue(StringUtils.hasText("abc"));
        assertFalse(StringUtils.hasText("   "));
        assertFalse(StringUtils.hasText(""));
        assertFalse(StringUtils.hasText(null));
    }

    @Test
    void testIsEmpty() {
        assertTrue(StringUtils.isEmpty(null));
        assertTrue(StringUtils.isEmpty(""));
        assertFalse(StringUtils.isEmpty(" ")); // Space is not empty
        assertFalse(StringUtils.isEmpty("abc"));
    }
}
