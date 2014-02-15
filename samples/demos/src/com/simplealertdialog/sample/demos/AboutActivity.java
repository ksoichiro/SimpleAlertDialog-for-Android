/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.simplealertdialog.sample.demos;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * @author Soichiro Kashima
 */
public final class AboutActivity extends Activity {

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_about);

        if (Build.VERSION_CODES.HONEYCOMB <= Build.VERSION.SDK_INT) {
            getActionBar().setDisplayHomeAsUpEnabled(true);
            getActionBar().setHomeButtonEnabled(true);
        }

        ((TextView) findViewById(R.id.version_name)).setText(getVersionName());
        ((TextView) findViewById(R.id.copyright)).setText(Html
                .fromHtml(getString(R.string.msg_copyright)));
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem menu) {
        int id = menu.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    private String getVersionName() {
        final PackageManager manager = getPackageManager();
        String versionName;
        try {
            final PackageInfo info = manager.getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_META_DATA);
            versionName = info.versionName;
        } catch (NameNotFoundException e) {
            versionName = "";
        }
        return versionName;
    }

}
