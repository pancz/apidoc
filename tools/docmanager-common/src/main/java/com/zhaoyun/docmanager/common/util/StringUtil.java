package com.zhaoyun.docmanager.common.util;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.zhaoyun.docmanager.common.commons.http.URIUtils;
import com.zhaoyun.docmanager.common.core.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(StringUtil.class);

    public final static char[] LowHex = "0123456789abcdef".toCharArray();

    public static boolean isEmpty(String v) {
        return v == null || v.isEmpty();
    }

    public static boolean isBlank(String v) {
        return v == null || v.trim().isEmpty();
    }

    public static boolean isNotEmpty(String v) {
        return !isEmpty(v);
    }

    public static boolean isNotBlank(String v) {
        return !isBlank(v);
    }

    public static boolean hasLength(String v, int length) {
        return isNotEmpty(v) && v.length() > length;
    }

    public static List<String> split(String v) {
        return split(v, ",");
    }

    public static List<String> split(String v, String sp) {
        if (v == null) {
            return Collections.<String>emptyList();
        }
        return Splitter.on(sp).omitEmptyStrings().trimResults().splitToList(v);
    }

    public static String join(Collection<String> v) {
        return join(v, ",");
    }

    public static String join(Collection<String> v, String sp) {
        if (v == null) {
            return "";
        }
        return Joiner.on(sp).skipNulls().join(v);
    }

    public static String substring(String v, int begin, int end) {
        if (v == null || begin > end) {
            throw new IllegalArgumentException("substring : if(v == null || begin > end)");
        }
        return v.substring(begin, end);
    }

    public static boolean isPhone(String num) {
        if (isBlank(num))
            return false;
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    public static boolean isPositiveNumber(String v) {
        if (isBlank(v))
            return false;
        return v.matches("[0-9]+");
    }

    public static String hidePhoneMiddle(String phone) {
        if (phone.length() > 11)
            phone = phone.substring(phone.length() - 11);
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
    /*public static List<String> split(String v){
        if(v == null){
			return null;
		}
		List<String> list = Splitter.on(',').omitEmptyStrings().trimResults().splitToList(v);
		String[] result = new String[list.size()];
		list.toArray(result);
		return result;
	}*/

    /**
     * base64解密，异常返回null
     *
     * @param encoded
     * @return
     */
    public static String base64Decode(final String encoded) {
        if (encoded.length() < 77) {
            try {
                return new String(new BASE64Decoder().decodeBuffer(encoded));
            } catch (IOException e) {
                LOGGER.error("base64Decode", e);
                return null;
            }
        }
        // 超过76换行
        BufferedReader br = null;
        try {
            br = new BufferedReader(new StringReader(encoded));
            StringBuilder s = new StringBuilder(300);
            for (String line; (line = br.readLine()) != null; ) {
                s.append(line);
            }
            return new String(new BASE64Decoder().decodeBuffer(s.toString()));
        } catch (IOException e) {
            LOGGER.error("base64Decode", e);
            return null;
        } finally {
            CloseUtil.closeQuietly(br);
        }
    }

    public static String base64Encode(final String src) {
        try {
            return new BASE64Encoder().encodeBuffer(src.getBytes(Constant.DEFAULT_CHARSET));
        } catch (Exception e) {
            LOGGER.error("base64Encode", e);
        }
        return null;
    }

    /**
     * 加盐加密
     *
     * @param originStr
     * @return
     */
    public static String md5Encrypt(final String originStr) {
        MessageDigest messageDigest = null;
        byte[] b = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            b = messageDigest.digest((originStr + Constant.SALT).getBytes(Constant.DEFAULT_CHARSET));
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
        StringBuilder s = new StringBuilder();

        int length = 0;
        if (b != null) {
            length = b.length;
        }
        for (int i = 0; i < length; i++) {
            s.append(Constant.HEX[(b[i] >>> 4) & 0xf]);
            s.append(Constant.HEX[b[i] & 0xf]);
        }
        return s.toString();
    }

    /**
     * 不加盐，小写表示
     *
     * @param originStr
     * @return
     */
    public static String md5EncryptLowerCase(final String originStr) {
        MessageDigest messageDigest = null;
        byte[] b = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            b = messageDigest.digest((originStr).getBytes(Constant.DEFAULT_CHARSET));
        } catch (NoSuchAlgorithmException e) {
            LOGGER.warn("", e);
        } catch (Exception ex) {
            LOGGER.warn("", ex);
        }
        StringBuilder s = new StringBuilder();
        int length = 0;
        if (b != null) {
            length = b.length;
        }
        for (int i = 0; i < length; i++) {
            s.append(LowHex[(b[i] >>> 4) & 0xf]);
            s.append(LowHex[b[i] & 0xf]);
        }
        return s.toString();
    }

    public static String md5EncryptByEncoding(final String originStr, final String encoding) {
        MessageDigest messageDigest = null;
        byte[] b = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            b = messageDigest.digest((originStr).getBytes(encoding));
        } catch (Exception e) {
            LOGGER.warn("", e);
        }
        StringBuilder s = new StringBuilder();
        int length = 0;
        if (b != null) {
            length = b.length;
        }
        for (int i = 0; i < length; i++) {
            s.append(LowHex[(b[i] >>> 4) & 0xf]);
            s.append(LowHex[b[i] & 0xf]);
        }
        return s.toString();
    }

    /**
     * 不加盐，大写表示
     *
     * @param originStr
     * @return
     */
    public static String md5EncryptWithoutSalt(final String originStr) {
        MessageDigest messageDigest = null;
        byte[] b = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            b = messageDigest.digest((originStr).getBytes(Constant.DEFAULT_CHARSET));
        } catch (Exception e) {
            LOGGER.warn("", e);
        }

        StringBuilder s = new StringBuilder();
        int length = 0;
        if (b != null) {
            length = b.length;
        }
        for (int i = 0; i < length; i++) {
            s.append(Constant.HEX[(b[i] >>> 4) & 0xf]);
            s.append(Constant.HEX[b[i] & 0xf]);
        }
        return s.toString();
    }

    public static boolean isNullOrEmpty(String... needCheck) {
        if (needCheck == null || needCheck.length == 0) {
            return true;
        }
        for (String curr : needCheck) {
            if (isBlank(curr)) {
                return true;
            }
        }
        return false;
    }

    public static String joinHttpPrefix(String prefix, String suffix) {
        Preconditions.checkNotNull(prefix, "StringUtil joinHttpUrl prefix not null");
        if (isBlank(suffix)) {
            return prefix;
        }
        StringBuilder builder = new StringBuilder();
        if (!prefix.toLowerCase().startsWith(Constant.HTTP_PROTOCOL)) {
            builder.append(Constant.HTTP_PROTOCOL);
        }
        builder.append(prefix);
        if (builder.charAt(builder.length() - 1) == Constant.CHAR_OBLIQUE.charAt(0)) {
            builder.deleteCharAt(builder.length() - 1);
        }
        if (suffix.charAt(0) != Constant.CHAR_OBLIQUE.charAt(0)) {
            builder.append(Constant.CHAR_OBLIQUE);
        }
        builder.append(suffix);
        return builder.toString();
    }

    public static String joinHttpQueryString(String url,
                                             Map<String, String> parameter) {
        if (isBlank(url)) {
            return url;
        }
        /*StringBuilder urlString = new StringBuilder(url);
        StringBuilder queryString = new StringBuilder(Joiner.on('&')
        		.useForNull(Constant.EMPTY_STRING)
        		.withKeyValueSeparator("=").join(parameter));
        char endChar = urlString.charAt(urlString.length() - 1);
        if (endChar == '/' || endChar == Constant.CHAR_QUESTION.charAt(0)
        		|| endChar == '&') {
        	urlString.deleteCharAt(urlString.length() - 1);
        }
        if (urlString.indexOf(Constant.CHAR_QUESTION) < 0) {
        	queryString.setCharAt(0, Constant.CHAR_QUESTION.charAt(0));
        }
        return urlString.append(queryString).toString();*/
        return URIUtils.getRealUri(url, parameter).toString();
    }

    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = 0;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
                chLength++;
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }
    }


    public static String toChinese(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (isMessyCode(str)) {
            try {
                return new String(str.getBytes("GBK"), "UTF-8");
            } catch (Exception e) {
            }
        }
        return str;
    }

}
