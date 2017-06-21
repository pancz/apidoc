package com.zhaoyun.docmanager.common.core;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.nio.charset.Charset;
import java.util.Set;

public final class Constant {
    public static final String APP_NAME = Config.getString("app_name");
    public static final String PARTNER = APP_NAME;
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String DEFAULT_ENCODE = DEFAULT_CHARSET.displayName();
    public static final String DEFAULT_CONTENT_TYPE = "application/json;charset=" + DEFAULT_ENCODE;
    public static final String CHAR_COMMA = ",";
    public static final String CHAR_SEMICOLON = ";";
    public static final String CHAR_COLON = ":";
    public static final String CHAR_POINT = ".";
    public static final String CHAR_QUESTION = "?";
    public static final String CHAR_OBLIQUE = "/";
    public static final String CHAR_UNDER = "_";
    public static final String CHAR_DOLLAR = "$";

    public static final String ENV = "env";

    public static final String NULL_STRING = "null";
    public static final String EMPTY_STRING = "";
    public static final String EMPTY_JSON = "{}";
    public static final String USER_DIR = System.getProperty("user.dir");

    public static final String SERVICE_PHONE = "4006990000";

    public static final long CODE_LIVE_SECOND = 30 * 60;

    public final static char[] HEX = "0123456789ABCDEF".toCharArray();

    public final static String SALT = "ToWell_EasyWork_EasyLife_2015";

    //请求参数、结果是否混淆（注解方式）
    public static final boolean MIX_RESULT = Config.getBoolean("open.mix.result");

    //是否需要校验token（注解方式）
    public static final boolean CHECK_TOKEN = Config.getBoolean("open.check.token");

    //是否需要校验登录（注解方式）
    public static final boolean CHECK_LOGIN = Config.getBoolean("open.check.login");

    public static final String HTTP_PROTOCOL = "http://";
    public static final String PROJECT_NAME = "CASHCOW";

    public static final String BANK_IMG_PREFIX = "http://static.zhaoyun.com/upload/app/bank/";

    public final static int TYRE_CATEGORY_ID = 10;

    public final static int COMMENT_GOOD = Config.getInt("comment.good", 4);
    public final static int COMMENT_BAD = Config.getInt("comment.bad", 3);

    public static final String CAR_LOGO_PREFIX = "http://static.zhaoyun.com/upload/logo/";

    // 第三方平台logo前缀
    public static final String THIRD_PLATFORM_LOGO_PREFIX = "http://static.zhaoyun.com/upload/app/2016/05/";
    public static final String IMAGE_URL_PREFIX = "http://static.zhaoyun.com";
    public static final String IMAGE_URL_UPLOAD_PREFIX = "http://static.zhaoyun.com/upload";
    public static final String LICENSE_URL_PREFIX = "/upload";

    public static final int ORDER_LIMIT_NUM = Config.getInt("top.order.free.service", 100);

    public static final int COUPON_SOON_EXPIRE_MILLISECOND = Config.getInt("coupon.soon.expire.millisecond", 3 * 60 * 60 * 1000);

    public static final boolean OPEN_DOC = Config.getBoolean("open.doc");

    public static final String MESSAGE_GROUP_SMALL_FORMAT = "http://static.zhaoyun.com/upload/app/2016/05/%d_small.png";
    public static final String MESSAGE_GROUP_SMALL_UNREAD_FORMAT = "http://static.zhaoyun.com/upload/app/2016/05/%d_small_unread.png";

    public static final String MESSAGE_GROUP_BIG_FORMAT = "http://static.zhaoyun.com/upload/app/2016/05/%d_big.png";
    public static final String MESSAGE_GROUP_BIG_UNREAD_FORMAT = "http://static.zhaoyun.com/upload/app/2016/05/%d_big_unread.png";

    public static final String THIRD_PLATFORM_BIG_FORMAT = "http://static.zhaoyun.com/upload/app/2016/05/%d_small.png";
    public static final String THIRD_PLATFORM_SMALL_FORMAT = "http://static.zhaoyun.com/upload/app/2016/05/%d_big.png";

    //默认门店经纬度
    public static final double STORE_DEFAULT_LAT = 30.2773660000d;
    public static final double STORE_DEFAULT_LNG = 120.1075200000d;

    public static final Set<Class<?>> BASE_CLASS_SET;

    static {
        Set<Class<?>> set = Sets.newHashSet();
        set.add(String.class);
        set.add(Integer.class);
        set.add(int.class);
        set.add(Byte.class);
        set.add(byte.class);
        set.add(Short.class);
        set.add(short.class);
        set.add(Long.class);
        set.add(long.class);
        set.add(Double.class);
        set.add(double.class);
        set.add(Float.class);
        set.add(float.class);
        set.add(Boolean.class);
        set.add(boolean.class);
        BASE_CLASS_SET = ImmutableSet.copyOf(set);
    }

}

