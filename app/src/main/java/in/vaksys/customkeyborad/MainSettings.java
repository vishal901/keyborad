package in.vaksys.customkeyborad;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by luke on 10/16/13.
 */
public class MainSettings extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
