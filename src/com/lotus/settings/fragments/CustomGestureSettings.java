/*
 * Copyright (C) 2019 The LotusOS Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lotus.settings.fragments;
import android.content.Context;
import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceScreen;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.provider.SearchIndexableResource;
import android.support.v14.preference.SwitchPreference;
import android.provider.Settings;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;
import com.lotus.settings.preferences.SystemSettingMasterSwitchPreference;
import com.android.internal.logging.nano.MetricsProto;
import com.android.settings.R;

import java.util.ArrayList;
import java.util.List;

public class CustomGestureSettings extends SettingsPreferenceFragment implements Preference.OnPreferenceChangeListener, Indexable {

   private static final String GESTURE_ANYWHERE_ENABLED = "gesture_anywhere_enabled"; 
   private SystemSettingMasterSwitchPreference mGestureAnywhereEnabled; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.lotus_settings_gestures);
		 PreferenceScreen prefSet = getPreferenceScreen();
        ContentResolver resolver = getActivity().getContentResolver();
		
		mGestureAnywhereEnabled = (SystemSettingMasterSwitchPreference) findPreference(GESTURE_ANYWHERE_ENABLED);
        mGestureAnywhereEnabled.setOnPreferenceChangeListener(this);
        int gestureAnywhereEnabled = Settings.System.getInt(resolver, GESTURE_ANYWHERE_ENABLED, 0);
        mGestureAnywhereEnabled.setChecked(gestureAnywhereEnabled != 0);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.LOTUS_SETTINGS;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
	ContentResolver resolver = getActivity().getContentResolver();
	 if (preference == mGestureAnywhereEnabled) {
            boolean value = (Boolean) newValue;
            Settings.System.putInt(resolver, GESTURE_ANYWHERE_ENABLED, value ? 1 : 0);
            return true;
	 }
        return false;
    }

    public static final SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
            new BaseSearchIndexProvider() {
                 @Override
                public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                        boolean enabled) {
                    final ArrayList<SearchIndexableResource> result = new ArrayList<>();
                     final SearchIndexableResource sir = new SearchIndexableResource(context);
                    sir.xmlResId = R.xml.lotus_settings_gestures;
                    result.add(sir);
                    return result;
                }
                 @Override
                public List<String> getNonIndexableKeys(Context context) {
                    final List<String> keys = super.getNonIndexableKeys(context);
                    return keys;
                }
    };
}