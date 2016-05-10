package sayem.picosoft.banglaassistant.commons;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sayemkcn on 5/10/16.
 */
public class Pref {
    public static void savePreference(Activity activity, String key, String value) {
        SharedPreferences sharedPref = activity.getSharedPreferences("BlAssistant", Context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = sharedPref.edit();

        prefEditor.putString(key, value);
        prefEditor.apply();

    }

    public static String getPreferenceData(Activity activity, String key) {
        SharedPreferences sharedPref = activity.getSharedPreferences("BlAssistant", Context.MODE_PRIVATE);

        String data = sharedPref.getString(key, "");
        return data;
    }
}
