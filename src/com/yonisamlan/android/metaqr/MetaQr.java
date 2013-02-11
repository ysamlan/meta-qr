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

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.ByteMatrix;
import com.google.zxing.qrcode.encoder.Encoder;
import com.google.zxing.qrcode.encoder.QRCode;

public class MetaQr {
    public static String TAG = "MetaQR";
    final public static String id = "com.yonisamlan.metaqr";
    final static String name = "MetaQR";

    static Bitmap bitmap = null;

    public static void announce(final Context context) {
        Intent intent = new Intent("org.metawatch.manager.APPLICATION_ANNOUNCE");
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putString("name", name);
        intent.putExtras(b);
        context.sendBroadcast(intent);
        Log.d(TAG, "Sent APPLICATION_ANNOUNCE");
    }

    public static void start(final Context context) {
        Intent intent = new Intent("org.metawatch.manager.APPLICATION_START");
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putString("name", name);
        intent.putExtras(b);
        context.sendBroadcast(intent);
        update(context);
    }

    private static void refreshApp(final Context context) {
        if (bitmap == null) {
            bitmap = Bitmap.createBitmap(96, 96, Bitmap.Config.RGB_565);
        }

        Canvas c = new Canvas(bitmap);
        c.drawColor(Color.WHITE);

        Matrix mQrScale = new Matrix();
        String qrData = PreferenceManager.getDefaultSharedPreferences(context).getString("qr_text",
                "");
        Bitmap unscaledQrbitmap;
        try {
            unscaledQrbitmap = getQrCodeBitmapOrBlank(qrData);
            Log.d(TAG, "unscaled QR bitmap is " + unscaledQrbitmap.getWidth() + "px wide");

            /*
             * Scale the bitmap up to 92px (leave 4px at the top/right so we don't overlap the
             * Manager's button labels)
             */
            float scaleFactor = 92 / unscaledQrbitmap.getWidth();
            mQrScale.setScale(scaleFactor, scaleFactor);
            c.setMatrix(mQrScale);

            int scaledWidth = (int) (unscaledQrbitmap.getWidth() * scaleFactor);
            int offset = (int) ((100 - scaledWidth) / (2 * scaleFactor));
            // Draw the bitmap, centering if necessary
            c.drawBitmap(unscaledQrbitmap, offset, offset, null);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public static void update(final Context context) {
        refreshApp(context);

        Intent intent = new Intent("org.metawatch.manager.APPLICATION_UPDATE");
        Bundle b = new Bundle();
        b.putString("id", id);
        b.putIntArray("array", Utils.makeSendableArray(bitmap));
        intent.putExtras(b);

        context.sendBroadcast(intent);
    }

    public static void stop(final Context context) {
        Intent intent = new Intent("org.metawatch.manager.APPLICATION_STOP");
        Bundle b = new Bundle();
        b.putString("id", id);
        intent.putExtras(b);
        context.sendBroadcast(intent);
        bitmap = null;
    }

    public static void button(final Context context, final int button, final int type) {
        // TODO: Allow a rotating set of QR codes based on button presses.
    }

    /**
     * Generate a QR code from a given string.
     *
     * @param qrCodeDataString String to encode.
     * @return a bitmap version of the generated QR code.
     */
    private static Bitmap getQrCodeBitmapOrBlank(final String qrCodeDataString)
            throws WriterException {
        final MultiFormatWriter writer = new MultiFormatWriter();

        /*
         * TODO customize error correction level to high if we have a type-1 QR code
         * (prefer a high-EC type 2 to a type 1) because that will scale better and have an
         * alignment pattern for better skew handling
         */
        final QRCode code = Encoder.encode(qrCodeDataString, ErrorCorrectionLevel.L);
        ByteMatrix matrix = code.getMatrix();
        final BitMatrix result = writer.encode(qrCodeDataString, BarcodeFormat.QR_CODE,
                matrix.getHeight(), matrix.getWidth(), null);
        final int width = result.getWidth();
        final int height = result.getHeight();

        /*
         * QR codes are encoded with 4 px padding, which is good in most situations, but hurts more
         * than it helps with a screen this small. Remove the built-in padding for now; we'll
         * display it centered as best we can later.
         */

        final int[] pixels = new int[(width - 8) * (height - 8)];
        // All are 0, or black, by default
        for (int y = 0; y < (height - 8); y++) {
            final int offset = y * (width - 8);
            for (int x = 0; x < (width - 8); x++) {
                if (result.get(x + 4, y + 4)) {
                    pixels[offset + x] = Color.BLACK;
                } else {
                    pixels[offset + x] = Color.WHITE;
                }
            }
        }

        return Bitmap.createBitmap(pixels, width - 8, height - 8, Bitmap.Config.RGB_565);
    }
}
