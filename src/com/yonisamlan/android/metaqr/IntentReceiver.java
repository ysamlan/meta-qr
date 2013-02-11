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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, final Intent intent) {
        Log.d(MetaQr.TAG, "onReceive()");

        String action = intent.getAction();
        if (action == null) {
            return;
        }

        if (action.equals("org.metawatch.manager.APPLICATION_DISCOVERY")) {
            Log.d(MetaQr.TAG, "Received APPLICATION_DISCOVERY intent");

            MetaQr.announce(context);

        } else if (action.equals("org.metawatch.manager.APPLICATION_ACTIVATE")) {
            Log.d(MetaQr.TAG, "Received APPLICATION_ACTIVATE intent");
            String id = intent.getStringExtra("id");
            if (id != null && id.equals(MetaQr.id)) {
                MetaQr.update(context);
            }
        } else if (action.equals("org.metawatch.manager.APPLICATION_DEACTIVATE")) {
            Log.d(MetaQr.TAG, "Received APPLICATION_DEACTIVATE intent");

        } else if (action.equals("org.metawatch.manager.BUTTON_PRESS")) {
            Log.d(MetaQr.TAG, "Received BUTTON_PRESS intent");

            String id = intent.getStringExtra("id");
            int button = intent.getIntExtra("button", 0); // button index
            int type = intent.getIntExtra("type", 0); // type: 0=pressed, 1=held
                                                      // short, 1=held long

            if (id != null && id.equals(MetaQr.id)) {
                MetaQr.button(context, button, type);
            }
        }
    }
}
