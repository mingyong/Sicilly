package xyz.shaohui.sicilly.views.setting;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import xyz.shaohui.sicilly.R;

/**
 * Created by shaohui on 16/10/9.
 */

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
        Preference sing = findPreference("fewf");
        sing.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                return false;
            }
        });
    }
}
