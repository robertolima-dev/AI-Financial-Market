package com.mali.crypfy.gateway.utils;

import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.UUID;

public class StringUtils {
    public static final String SPACE_SEPARATOR = " ";
    public static final String PATTERN_DIGITS = "[^0-9]";
    public static final String PATTERN_ALPHA = "[^a-zA-Z]";
    public static final String PATTERN_ALPHA_DIGITS = "[^\\p{Alpha}\\p{Digit}]+";
    public static final String PATTERN_CNPJ = "[0-9]{2}\\.?[0-9]{3}\\.?[0-9]{3}\\/?[0-9]{4}\\-?[0-9]{2}";
    public static final String PATTERN_CPF = "[0-9]{3}\\.?[0-9]{3}\\.?[0-9]{3}\\-?[0-9]{2}";
    public static final String PATTERN_CEP = "^\\d{5}-\\d{3}$";
    public static final String PATTERN_PHONE = "^\\([1-9]{2}\\) [2-9][0-9]{3,4}\\-[0-9]{4}$";
    public static final String PATTERN_CREDIT_CARD_NUMBER = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
    public static final String PATTERN_CREDIT_CARD_SECURITY_CODE = "^[0-9]{3,4}$";

    public static String removeBreakLines(String str) {
        return str.replace("\n", "").replace("\r", "");
    }

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

    public static String lpadTo(String input, int width, char ch) {

        String strPad = "";

        StringBuffer sb = new StringBuffer(input.trim());
        while (sb.length() < width)
            sb.insert(0, ch);
        strPad = sb.toString();

        if (strPad.length() > width) {
            strPad = strPad.substring(0, width);
        }
        return strPad;
    }

    public static String toMoneyFormat(BigDecimal value) {
        if (value == null)
            return "0,00";
        NumberFormat df = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setCurrencySymbol("");
        dfs.setGroupingSeparator('.');
        dfs.setMonetaryDecimalSeparator(',');
        dfs.setMinusSign('-');
        ((DecimalFormat) df).setDecimalFormatSymbols(dfs);
        ((DecimalFormat) df).setNegativePrefix("-");
        ((DecimalFormat) df).setNegativeSuffix("");
        return df.format((BigDecimal) value);
    }


    public static String toQtyFormat(BigDecimal value) {
        return (value != null) ? value.stripTrailingZeros().toPlainString().replace(".", ",") : "";
    }

    public static String onlyNumbers(String str) {
        return str.replaceAll("[^0-9]+", "");
    }

    public static boolean isCPF(String cpf) {
        if (cpf != null) {
            cpf.replaceAll(".", "");
            cpf.replaceAll("-", "");
            return (cpf.length() == 11) ? true : false;
        }
        return false;
    }

    public static String stripAccents(String s) {
        s = Normalizer.normalize(s, Normalizer.Form.NFD);
        s = s.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
        return s;
    }

    // The below methods just format some fields, they doesn't validate anything
    public static String formatCnpj(String cpnj) {
        String formattedCnpj = "";
        if (cpnj != null && !cpnj.isEmpty())
            formattedCnpj = String.format("%s.%s.%s/%s-%s", cpnj.substring(0, 2), cpnj.substring(2, 5), cpnj.substring(5, 8), cpnj.substring(8, 12), cpnj.substring(12, 14));
        return formattedCnpj;
    }

    public static String formatCpf(String cpf) {
        String formattedCpf = "";
        if (cpf != null && !cpf.isEmpty())
            formattedCpf = String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6), cpf.substring(6, 9), cpf.substring(9, 11));
        return formattedCpf;
    }

    public static String formatPhone(String phone) {
        String formattedPhone = "";
        if (phone != null && !phone.isEmpty())
            formattedPhone = String.format("(%s) %s-%s", phone.substring(0, 2), phone.substring(2, 6), phone.substring(6, 10));
        return formattedPhone;
    }

    public static String formatCellPhone(String cellPhone) {
        String formattedCelPhone = "";
        if (cellPhone != null && !cellPhone.isEmpty())
            formattedCelPhone = String.format("(%s) %s-%s", cellPhone.substring(0, 2), cellPhone.substring(2, 7), cellPhone.substring(7, 11));
        return formattedCelPhone;
    }

    public static String formatCep(String cep) {
        String formattedCep = "";
        if (cep != null && !cep.isEmpty())
            formattedCep = String.format("%s-%s", cep.substring(0, 5), cep.substring(5, 8));
        return formattedCep;
    }

    public static String generateRandomString() {
        String uuid = UUID.randomUUID().toString();
        return uuid;
    }
}
