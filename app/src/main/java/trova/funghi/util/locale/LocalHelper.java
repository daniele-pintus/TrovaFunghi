package trova.funghi.util.locale;

import android.content.res.Configuration;

import java.util.Locale;

/**
 * Created by xid73 on 30/06/2017.
 */

public class LocalHelper {
    private static Locale instance = new Locale("it", "IT");

    public static Locale getLocale() {
        return instance;
    }

    public static String getDisplayLanguage() {
        return instance.getDisplayLanguage();
    }

    public static String getDisplayCountry() {
        return instance.getDisplayCountry();
    }

    public static void setApplicationLocale(){
        Locale.setDefault(instance);
        Configuration config = new Configuration();
        config.locale = instance;
    }
}
