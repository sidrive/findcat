package id.findcat.app.utils;

import android.os.Build;
import android.text.TextUtils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("FinalStaticMethod")
public class DateUtils {

    /**
     * These constant only used for old android platform, which sometimes give empty value.
     * Abbreviation obtained from:
     * http://www.localeplanet.com/java/id-ID/index.html
     * http://www.localeplanet.com/icu/id/
     */
    public static final String[] months
            = new String[]{"Januari", "Februari", "Maret", "April", "Mei", "Juni",
            "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public static final String[] shortMonths
            = new String[]{"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt", "Nov", "Des"};

    public static final String[] weekdays
            = new String[]{"Unused", "Minggu", "Senin", "Selasa", "Rabu", "Kamis", "Jumat", "Sabtu"};

    public static final String[] shortWeekdays
            = new String[]{"Unused", "Min", "Sen", "Sel", "Rab", "Kam", "Jum", "Sab"};

    /**
     * Get Calendar in local timezone, initialized to current local date & time.
     */
    public static final Calendar getCalendar() {
        return Calendar.getInstance(Locale.ENGLISH);
    }

    /**
     * Get Calendar in UTC or GMT+00, initialized to current date & time.
     */
    public static final Calendar getCalendar0() {
        return Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    }

    private static SimpleDateFormat createSDFInternal(String pattern, boolean isGMT0) {
        Locale defaultLocale = Locale.getDefault();
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, defaultLocale);

        createDateFormatSymbolsInternal(sdf);

        // SimpleDateFormat contains incorrect Calendar
        if (isGMT0)
            sdf.setCalendar(getCalendar0());
        else
            sdf.setCalendar(getCalendar());
        return sdf;
    }


    public static final DateFormatSymbols createDateFormatSymbols() {
        return createDateFormatSymbolsInternal(null);
    }

    /**
     * Create DateFormatSymbols from supplied SimpleDateFormat, or from default Locale if SimpleDateFormat is null.
     */
    private static DateFormatSymbols createDateFormatSymbolsInternal(SimpleDateFormat sdf) {
        DateFormatSymbols dfs;
        Locale defaultLocale = Locale.getDefault();

        if (sdf != null)
            dfs = sdf.getDateFormatSymbols();
        else
            dfs = new DateFormatSymbols(defaultLocale);

        // Fix for indonesian days because it's not available on older android platform
        if (defaultLocale.getLanguage().equals("in")
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            dfs.setMonths(months);
            dfs.setShortMonths(shortMonths);
            dfs.setWeekdays(DateUtils.weekdays);
            dfs.setShortWeekdays(DateUtils.shortWeekdays);
        }
        if (sdf != null)
            sdf.setDateFormatSymbols(dfs);

        return dfs;
    }

    /**
     * Create SimpleDateFormat instance in local timezone. Can throw exception,
     * advised to call under try-catch.
     */
    public static final SimpleDateFormat createSDF(String pattern) {
        return createSDFInternal(pattern, false);
    }

    /**
     * Create SimpleDateFormat instance in UTC. Can throw exception, advised to
     * call under try-catch.
     */
    public static final SimpleDateFormat createSDF0(String pattern) {
        return createSDFInternal(pattern, true);
    }

    /**
     * Get current local time in milliseconds (long).
     * NOTE: {@link System#currentTimeMillis()} didn't take timezone or system change into account.
     */
    public static final long getCurrentTimeMillis() {
        return getCalendar().getTimeInMillis();
    }

    /**
     * Create Date object from server datetime string, in UTC.
     */
    public static final Date getServerDate(String serverDateTime) {
        if (TextUtils.isEmpty(serverDateTime))
            return null;

        String inputFormat = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        try {
            // read date in server time (UTC)
            return createSDF0(inputFormat).parse(serverDateTime);
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * All in UTC.
     */
    public static final long getServerDateInMillis(String serverDateTime) {
        Date serverDate = getServerDate(serverDateTime);
        if (serverDate == null)
            return -1;
        return serverDate.getTime();
    }

    /**
     * Get current date & time in string (in local time).
     *
     * @param format If null or empty, default format is "dd MMMM yyyy HH:mm"
     */
    public static final String getCurrentDateTimeStr(String format) {
        SimpleDateFormat sdf = createSDF(format);
        return sdf.format(getCalendar().getTime());
    }

    /**
     * Return current month index (in local time). January = 0.
     */
    public static final int getCurrentMonth() {
        return getCalendar().get(Calendar.MONTH);
    }

    /**
     * Return current year (in local time).
     */
    public static final int getCurrentYear() {
        return getCalendar().get(Calendar.YEAR);
    }

    /**
     * Return current date in string (in local time), formatted to "dd MMMM yyyy",
     * such as 12 January 2015.
     */
    public static final String getCurrentDate() {
        return getCurrentDateTimeStr("dd MMMM yyyy");
    }

    /**
     * Return current hour in string (in local time), formatted to "HH:mm", such as 20:15.
     */
    public static final String getCurrentTime() {
        return getCurrentDateTimeStr("HH:mm");
    }

    /**
     * Get milliseconds from datetime string (in local time).
     *
     * @param format If null or empty, default format is "yyyy-MM-dd HH:mm"
     * @return date time in milis, or -1 if error
     */
    public static final long getTimeMillis(String dateTimeStr, String format) {
        if (TextUtils.isEmpty(format))
            format = "yyyy-MM-dd HH:mm";

        Date date = null;
        try {
            date = createSDF(format).parse(dateTimeStr);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        if (date != null)
            return date.getTime();
        else
            return -1;
    }

    /**
     * Get datetime string from milliseconds (in local time).
     *
     * @param format If null or empty, default format is "dd MMMM yyyy HH:mm"
     */
    public static final String getDateTimeStr(long timeMillis, String format) {
        if (TextUtils.isEmpty(format))
            format = "dd MMMM yyyy HH:mm";

        Calendar c = getCalendar();
        c.setTimeInMillis(timeMillis);
        SimpleDateFormat sdf = createSDF(format);
        sdf.setCalendar(c); // important otherwise use system calendar
        return sdf.format(c.getTime());
    }

    /**
     * Get datetime string from another datetime string, possibly in different format (in local time).
     *
     * @param inputFormat  Format of dateTimeStr, if null or empty default format is "dd MMMM yyyy HH:mm"
     * @param outputFormat Format of result string, if null or empty default format is "dd MMMM yyyy HH:mm"
     */
    public static final String getDateTimeStr(String dateTimeStr,
                                              String inputFormat, String outputFormat) {
        if (TextUtils.isEmpty(inputFormat))
            inputFormat = "dd MMMM yyyy HH:mm";
        if (TextUtils.isEmpty(outputFormat))
            outputFormat = "dd MMMM yyyy HH:mm";

        Date date = null;
        try {
            date = createSDF(inputFormat).parse(dateTimeStr);
        } catch (Exception e) {
            Log.printStackTrace(e);
        }

        // on error return original string
        if (date == null)
            return dateTimeStr;

        Calendar c = getCalendar();
        c.setTimeInMillis(date.getTime());
        SimpleDateFormat sdf = createSDF(outputFormat);
        sdf.setCalendar(c); // important otherwise use system calendar
        return sdf.format(c.getTime());
    }

    /**
     * Call {@link #getDateTimeStr(int, int, int, String)} with format "dd MMMM yyyy",
     * such as 12 January 2015.
     */
    public static final String getDateTimeStr(int date, int month, int year) {
        return getDateTimeStr(date, month, year, "dd MMMM yyyy");
    }

    /**
     * Get datetime string from date, month, year index (in local time). January = 0.
     */
    public static final String getDateTimeStr(int date, int month, int year, String format) {
        if (TextUtils.isEmpty(format))
            format = "dd MMMM yyyy";

        Calendar c = getCalendar();
        c.set(year, month, date);
        SimpleDateFormat sdf = createSDF(format);
        sdf.setCalendar(c);
        return sdf.format(c.getTime());
    }

    /**
     * Format server datetime string.
     *
     * @param serverDateTime Raw date string from server, usually formatted in form
     *                       "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
     * @param outputFormat   If null, default format is "dd MMMM yyyy / HH:mm", which will
     *                       produce output like "09 April 2015 / 19:01".
     * @param isLocal        Result in system local time or UTC time.
     * @return New formatted string, or the original string if error occurred.
     */
    public static final String formatServerDate(String serverDateTime, String outputFormat,
                                                boolean isLocal) {
        if (TextUtils.isEmpty(outputFormat))
            outputFormat = "dd MMMM yyyy / HH:mm";

        Date serverDate = getServerDate(serverDateTime);
        if (serverDate == null)
            return serverDateTime;

        try {
            if (isLocal)
                return createSDF(outputFormat).format(serverDate);
            else
                return createSDF0(outputFormat).format(serverDate);

        } catch (Exception e) {
            Log.printStackTrace(e);
        }
        return serverDateTime;
    }

    /**
     * Helper method. Format server datetime string, which is in UTC time, to local datetime string.
     */
    public static final String formatServerDate(String serverDateTime, String outputFormat) {
        return formatServerDate(serverDateTime, outputFormat, true);
    }

    /**
     * Helper method. Format server datetime string, which is in UTC time, to another (non-local) UTC
     * datetime string.
     */
    public static final String formatServerDate0(String serverDateTime, String outputFormat) {
        return formatServerDate(serverDateTime, outputFormat, false);
    }

    /**
     * Check if specified timemillis is alrady past (larger or equals) n expireDays.
     * Compared with current local time.
     */
    public static final boolean isExpiredInDays(long timemillis, long expireDays) {
        long delta = getCurrentTimeMillis() - timemillis;
        long deltaDays = TimeUnit.MILLISECONDS.toDays(delta);
        if (deltaDays >= expireDays) {
            Log.d("Expiry Check", "Expired " + deltaDays + " days");
            return true;
        }
        return false;
    }

    /**
     * Check if specified timemillis is alrady past (larger or equals) n hours.
     * Compared with current local time.
     */
    public static final boolean isExpiredInHours(long timemillis, long expireHours) {
        long delta = getCurrentTimeMillis() - timemillis;
        long deltaHours = TimeUnit.MILLISECONDS.toHours(delta);
        if (deltaHours >= expireHours) {
            Log.d("Expiry Check", "Expired " + deltaHours + " hours");
            return true;
        }
        return false;
    }

    /**
     * Check if specified timemillis is alrady past (larger or equals) n minutes.
     * Compared with current local time.
     */
    public static final boolean isExpiredInMinutes(long timemillis, long expireMinutes) {
        long delta = getCurrentTimeMillis() - timemillis;
        long deltaMinutes = TimeUnit.MILLISECONDS.toMinutes(delta);
        if (deltaMinutes >= expireMinutes) {
            Log.d("Expiry Check", "Expired " + deltaMinutes + " minutes");
            return true;
        }
        return false;
    }

    /**
     * Get duration in Indonesia
     */
    public static String getDurationDate(Date date) {
        Date today = new Date();

        long diff = today.getTime() - date.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;
        long diffDays = diff / (24 * 60 * 60 * 1000) % 30;
        long diffWeeks = diff / (30 * 24 * 60 * 60 * 1000) / 4;
        long diffMonth = diff / (30 * 24 * 60 * 60 * 1000);

        if (diffMonth > 0) {
            return diffMonth + " bulan yang lalu";
        } else if (diffWeeks > 0) {
            return diffWeeks + " minggu yang lalu";
        } else if (diffDays > 0) {
            return diffDays + " hari yang lalu";
        } else if (diffHours > 0) {
            return diffHours + " jam yang lalu";
        } else if (diffMinutes > 0) {
            return diffMinutes + " menit yang lalu";
        } else {
            return "Baru saja";
        }
    }
}
