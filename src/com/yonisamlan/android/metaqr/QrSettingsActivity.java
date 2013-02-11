/*
 * Copyright (C) 2013 Yoni Samlan
 * Copyright (c) 2011 Meta Watch Ltd.
 * www.MetaWatch.org
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
package com.yonisamlan.android.metaqr;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class QrSettingsActivity extends Activity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_settings);
        final EditText qrEditText = (EditText) findViewById(R.id.qr_text);
        // TODO: move disk read to a background thread (strict mode violation)
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String savedQrText = prefs.getString("qr_text", "");
        qrEditText.setText(savedQrText);

        findViewById(android.R.id.button1).setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Editor editor = prefs.edit();
                editor.putString("qr_text", qrEditText.getText().toString());
                editor.apply();
                Toast.makeText(QrSettingsActivity.this, R.string.settings_toast_saved,
                        Toast.LENGTH_LONG).show();
            }
        });

        ((TextView) findViewById(android.R.id.text1)).setMovementMethod(LinkMovementMethod
                .getInstance());

        MetaQr.announce(this);
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.qr_settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_item_attributions:
            openLicenses();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void openLicenses() {
        startActivity(new Intent(this, AttributionsActivity.class));
    }
}
