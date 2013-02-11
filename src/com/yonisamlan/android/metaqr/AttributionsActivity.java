/*
 * Copyright (C) 2013 Yoni Samlan
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

import java.io.IOException;

import org.apache.commons.io.IOUtils;

import android.app.Activity;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class AttributionsActivity extends Activity {
    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView textView = new TextView(this);
        setContentView(textView);
        String html;

        try {
            html = IOUtils.toString(getResources().openRawResource(R.raw.licenses));
        } catch (NotFoundException e) {
            e.printStackTrace();
            html = "Error reading license.";
        } catch (IOException e) {
            e.printStackTrace();
            html = "Error reading license.";
        }

        textView.setText(Html.fromHtml(html));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
