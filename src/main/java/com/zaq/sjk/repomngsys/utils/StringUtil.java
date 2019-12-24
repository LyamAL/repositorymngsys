package com.zaq.sjk.repomngsys.utils;

import java.util.Base64;

/**
 * @author ZAQ
 */
public class StringUtil {
    public static byte[] stringToBytes(String string) {
        return Base64.getDecoder().decode(string);
    }

    public static String bytesToString(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }
}
