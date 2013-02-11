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

import android.graphics.Bitmap;

public class Utils {
    public static int[] makeSendableArray(final Bitmap bitmap) {
        int pixelArray[] = new int[bitmap.getWidth() * bitmap.getHeight()];
        bitmap.getPixels(pixelArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        return pixelArray;
    }
}
