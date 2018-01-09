package com.hhp.netty;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * proxy配置单例
 * @author hhp
 * @date 2018.01.03
 */
public class OcsProxyPropConstants {
    private OcsProxyPropConstants(){}

    public static void setString(OcsProxySettings props, String name, String value) {
        if(props == null){
            return;
        }
        if (StringUtils.isNotEmpty(name) && StringUtils.isNotEmpty(value)) {
            props.setValue(name, value);
        }
    }

    public static String getString(OcsProxySettings props, String name, String defaultValue) {
        return props == null ? defaultValue : props.getValue(name, defaultValue);
    }

    public static void setInt(OcsProxySettings props, String name, int value) {
        if (StringUtils.isNotEmpty(name)) {
            props.setValue(name, String.valueOf(value));
        }
    }

    public static int getInt(OcsProxySettings props, String name, int defaultValue) {
        String sInt = props.getValue(name, "");
        if (sInt.length() <= 0) {
            return defaultValue;
        } else {
            try {
                return Integer.parseInt(sInt);
            } catch (NumberFormatException var0) {
                return defaultValue;
            }
        }
    }

    public static void setLong(OcsProxySettings props, String name, long value) {
        if (StringUtils.isNotEmpty(name)) {
            props.setValue(name, String.valueOf(value));
        }
    }

    public static long getLong(OcsProxySettings props, String name, long defaultValue) {
        String sLong = props.getValue(name, "");
        if (sLong.length() <= 0) {
            return defaultValue;
        } else {
            try {
                return Long.parseLong(sLong);
            } catch (NumberFormatException var1) {
                return defaultValue;
            }
        }
    }

    public static void setDouble(OcsProxySettings props, String name, double value) {
        if (StringUtils.isNotEmpty(name)) {
            props.setValue(name, String.valueOf(value));
        }
    }

    public static double getDouble(OcsProxySettings props, String name, double defaultValue) {
        String sLong = props.getValue(name, "");
        if (sLong.length() <= 0) {
            return defaultValue;
        } else {
            try {
                return Double.parseDouble(sLong);
            } catch (NumberFormatException var2) {
                return defaultValue;
            }
        }
    }

    public static void setBoolean(OcsProxySettings props, String name, boolean value) {
        if (StringUtils.isNotEmpty(name)) {
            if (value) {
                props.setValue(name, "true");
            } else {
                props.setValue(name, "false");
            }
        }
    }

    public static boolean getBoolean(OcsProxySettings props, String name, boolean defaultValue) {
        String sBoolean = props.getValue(name, BooleanUtils.toStringTrueFalse(defaultValue));
        return BooleanUtils.toBoolean(sBoolean);
    }

}
