package cu.phibrain.plugins.cardinal.io.utils;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableInstant;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Michel on 13/2/2019.
 */

public class JodaTimeHelper {



    public static Boolean isYerterday(ReadableInstant time) {
        return LocalDate.now().minusDays(1).compareTo(new LocalDate(time)) == 0;
    }


    public static Boolean isToday(DateTime epDate) {

        return DateUtils.isToday(epDate);
    }


    public static String calculateAge(DateTime epDate) {

        DateTime now = new DateTime();

        if (isToday(epDate))
            return "hoy";

        if (isYerterday(epDate))
            return "ayer";

        Period period;
        if (epDate.isBefore(now)) {
            period = new Period(epDate, now, PeriodType.yearMonthDay());
        } else {
            period = new Period(now, epDate, PeriodType.yearMonthDay());
        }

        PeriodFormatterBuilder periodBuilder = new PeriodFormatterBuilder().appendYears();

        if (period.getYears() > 1)
            periodBuilder.appendSuffix(" años").appendSeparator(", ");
        else
            periodBuilder.appendSuffix(" año").appendSeparator(", ");

        periodBuilder.appendMonths();
        if (period.getMonths() > 1)
            periodBuilder.appendSuffix(" meses").appendSeparator(", ");
        else
            periodBuilder.appendSuffix(" mes").appendSeparator(", ");
/*
        periodBuilder.appendWeeks();
        if (period.getWeeks() > 1)
            periodBuilder.appendSuffix(" semanas").appendSeparator(", ");
        else
            periodBuilder.appendSuffix(" semana").appendSeparator(", ");*/

        periodBuilder.appendDays();
        if (period.getDays() > 1)
            periodBuilder.appendSuffix(" días").appendSeparator(", ");
        else
            periodBuilder.appendSuffix(" día").appendSeparator(", ");


        PeriodFormatter formatter = periodBuilder.printZeroNever().toFormatter();

        return "hace " + formatter.print(period);
    }

    public static String formatDateString(String formatPattern, String dateToFormat) {

        // Crear un formatter con una representación específica
       // DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

        // Obtener una fecha a partir de su representación "2010-01-01T12:00:00+01:00"
       // String date = dateToFormat.replaceAll("\\+0([0-9]){1}\\:00", "");

        Date asDate = parseStringAsDate(dateToFormat);

        //DateTime dt = fmt.parseDateTime(date);

        DateTime dt = new DateTime(asDate);

        return dt.toString(formatPattern);
    }

    public static String formatDate(String formatPattern, Date dateToFormat) {

        // Crear un formatter con una representación específica
        DateTimeFormatter fmt = DateTimeFormat.forPattern(formatPattern);
        //DateTime dt = new DateTime(dateToFormat);
        final DateTimeZone dtz = DateTimeZone.getDefault();
        LocalDateTime ldt = new LocalDateTime(dateToFormat, dtz);
        if (dtz.isLocalDateTimeGap(ldt)) {
            ldt = ldt.plusHours(1);
        }
        DateTime dt = ldt.toDateTime();

        return fmt.print(dt);

    }

    public static Date parseStringAsDate(String dateToFormat) {

        dateToFormat = dateToFormat.replaceAll("(\\+|\\-)0([0-9]){1}\\:00", "");

        final DateTimeZone dtz = DateTimeZone.getDefault();
        LocalDateTime ldt = new LocalDateTime(dateToFormat, dtz);
        if (dtz.isLocalDateTimeGap(ldt)) {
            ldt = ldt.plusHours(1);
        }
        DateTime dt = ldt.toDateTime();


        return dt.toDate();
    }

    /**
     * Return an ISO 8601 combined date and time string for current date/time
     *
     * @return String with format "yyyy-MM-dd'T'HH:mm:ssTDZ"
     */
    public static String getISO8601StringForCurrentDate() {
        Date now = new Date();
        return getISO8601StringForDate(now);
    }

    /**
     * Return an ISO 8601 combined date and time string for specified date/time
     *
     * @param date Date
     * @return String with format "yyyy-MM-dd'T'HH:mm:ssTDZ"
     */

    private static String getISO8601StringForDate(Date date) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssTDZ", Locale.US);
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormat.format(date);
    }

    public static String toISO8601UTC(Date date) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssTDZ");
        df.setTimeZone(tz);
        return df.format(date);
    }

    public static Date fromISO8601UTC(String dateStr) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssTDZ");
        df.setTimeZone(tz);

        try {
            return df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
