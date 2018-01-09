package id.findcat.app.utils;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

/**
 * Created by rioswarawan on 12/19/16.
 */

public class Convert {

    public static int dpToPixel(Resources r, int dp) {
        // code is similar to TypedValue.applyDimension()
        return (int) (r.getDisplayMetrics().density * dp);
    }

    public static int spToPixel(Resources r, int sp) {
        // code is similar to TypedValue.applyDimension()
        return (int) (r.getDisplayMetrics().scaledDensity * sp);
    }

    /**
     * Casts object, when failed always return null, so you don't have to guard/check for
     * ClassCastException. <br />
     * See http://stackoverflow.com/questions/148828/<br />
     * Casting will return either Success (return object) or return null. Consistent result. <br />
     * <br />
     * NOTE1: This method use cast(), which in turn will also call isInstance().
     * So we inevitably call isInstance() twice here. <br />
     * We could simply cast with (T)o  but compiler will warn about unchecked cast. <br />
     * <br />
     * NOTE2: Primitive type will return its wrapper type. <br />
     * For example, providing int.class will be recognized as Integer.TYPE <br />
     * See http://stackoverflow.com/questions/35741258/does-int-class-autobox-to-classinteger
     * <br />
     */
    @Nullable
    public static <T> T as(Class<T> t, Object o) {
        return t.isInstance(o) ? t.cast(o) : null;
    }

    /**
     * Casts object, when failed always return ClassCastException. <br />
     * This is to deal with Java inconsistencies regarding casting null. <br />
     * http://stackoverflow.com/questions/18723596 <br />
     * Casting will return either Success (return object) or throw ClassCastException, but never
     * null. Consistent result.
     */
    public static <T> T asEx(@NonNull Class<T> t, @Nullable Object o) {
        if (o == null)
            throw new ClassCastException();
        else
            return t.cast(o);
    }

    /**
     * <p>
     * Converts an array of object Integers to primitives.
     * </p>
     * Codes copied from <a href=
     * "http://www.docjar.com/html/api/org/apache/commons/lang/ArrayUtils.java.html"
     * >org.apache.commons.lang.ArrayUtils</a>
     * <p>
     * License owned by Apache. Credit goes to original author (see link).
     * </p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array a {@code Integer} array, may be {@code null}
     * @return an {@code int} array, {@code null} if null array input
     * @throws NullPointerException if array content is {@code null}
     */
    public static int[] toPrimitive(Integer[] array) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new int[0];
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            result[i] = array[i];
        }
        return result;
    }

    /**
     * <p>
     * Converts an array of object Integer to primitives, handling {@code null}.
     * </p>
     * Codes copied from <a href=
     * "http://www.docjar.com/html/api/org/apache/commons/lang/ArrayUtils.java.html"
     * >org.apache.commons.lang.ArrayUtils</a>
     * <p>
     * License owned by Apache. Credit goes to original author (see link).
     * </p>
     * This method returns {@code null} for a {@code null} input array.
     *
     * @param array        a {@code Integer} array, may be {@code null}
     * @param valueForNull the value to insert if {@code null} found
     * @return an {@code int} array, {@code null} if null array input
     */
    public static int[] toPrimitive(Integer[] array, int valueForNull) {
        if (array == null) {
            return null;
        } else if (array.length == 0) {
            return new int[0];
        }
        int[] result = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            Integer b = array[i];
            result[i] = (b == null ? valueForNull : b);
        }
        return result;
    }

    /**
     * Return Base64 String from bytes.
     */
    public static String bytesToBase64String(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * Return bytes from Base64 String.
     */
    public static byte[] base64StringToBytes(String s) {
        return Base64.decode(s, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * Base16 (hexadecimal) char array
     */
    private static char[] base16Array = "0123456789abcdef".toCharArray();

    /**
     * Return Base16 (hexadecimal) String from bytes.
     */
    public static String bytesToBase16String(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = base16Array[v >>> 4];
            hexChars[j * 2 + 1] = base16Array[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Return bytes from Base16 (hexadecimal) String.
     */
    public static byte[] base16StringToBytes(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                    .digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    /**
     * Convert string to int, return default value if exception happen.
     * This is similar to Apache Commons NumberUtils.toInt().
     */
    public static int toInt(String s, int defaultValue) {
        try {
            if (s.equals(""))
                return defaultValue;
            return Integer.parseInt(s);
        } catch (NumberFormatException ex) {
            Log.printStackTrace(ex);
            return defaultValue;
        }
    }

}
