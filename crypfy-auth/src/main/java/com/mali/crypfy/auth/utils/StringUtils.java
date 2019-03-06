package com.mali.crypfy.auth.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class StringUtils {

    public static final String SPACE_SEPARATOR = " ";
    public static final String PATTERN_DIGITS = "[^0-9]";
    public static final String PATTERN_ALPHA = "[^a-zA-Z]";
    public static final String PATTERN_ALPHA_DIGITS = "[^\\p{Alpha}\\p{Digit}]+";
    public static final String PATTERN_CNPJ = "[0-9]{2}\\.?[0-9]{3}\\.?[0-9]{3}\\/?[0-9]{4}\\-?[0-9]{2}";
    public static final String PATTERN_CPF = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
    public static final String PATTERN_CEP = "^\\d{5}-\\d{3}$";
    public static final String PATTERN_PHONE = "^\\([1-9]{2}\\) [2-9][0-9]{3,4}\\-[0-9]{4,5}$";
    public static final String PATTERN_CREDIT_CARD_NUMBER = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
    public static final String PATTERN_CREDIT_CARD_SECURITY_CODE = "^[0-9]{3,4}$";
    public static final String PATTERN_EMAIL = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";

    public static String getMD5(String string) {
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(string.getBytes());

            byte byteData[] = md.digest();

            //convert the byte to hex format method 1
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid.replace("-","");
    }

}
