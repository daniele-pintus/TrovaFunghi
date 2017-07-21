package trova.funghi.util.locale;

import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by xid73 on 30/06/2017.
 */

public class CalendarHelper {

    public static Long getCurrentDate(){
        TimeZone t = TimeZone.getTimeZone("Europe/Amsterdam");
        Calendar c = Calendar.getInstance(t);
        return c.getTimeInMillis();
    }
    private static Calendar getGMTInstance() {
        TimeZone t = TimeZone.getTimeZone("Europe/Amsterdam");
        Calendar c = Calendar.getInstance(t);
        c.set(Calendar.YEAR,2017);
        c.set(Calendar.DAY_OF_MONTH,31);
        c.set(Calendar.MONTH,Calendar.DECEMBER);
        c.set(Calendar.HOUR_OF_DAY,23);
        c.set(Calendar.MINUTE,30);
        c.set(Calendar.SECOND,59);
        return c;
    }
}
