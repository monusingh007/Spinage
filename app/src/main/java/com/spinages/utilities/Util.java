package com.spinages.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by monu on 10/8/2015.
 */
public class Util {
    public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
        int targetWidth = 400;
        int targetHeight = 400;
        Bitmap targetBitmap = Bitmap.createBitmap(targetWidth,
                targetHeight, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(targetBitmap);
        Path path = new Path();
        path.addCircle(((float) targetWidth - 1) / 2,
                ((float) targetHeight - 1) / 2,
                (Math.min(((float) targetWidth),
                        ((float) targetHeight)) / 2),
                Path.Direction.CCW);

        canvas.clipPath(path);
        Bitmap sourceBitmap = scaleBitmapImage;
        canvas.drawBitmap(sourceBitmap,
                new Rect(0, 0, sourceBitmap.getWidth(),
                        sourceBitmap.getHeight()),
                new Rect(0, 0, targetWidth, targetHeight), null);
        return targetBitmap;
    }
    public  static   Typeface setTextFontOpenSans(Context context, String fontname) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/OpenSans/"+fontname);
        return typeFace;
    }
    public  static   Typeface setTextFontRoboto(Context context, String fontname) {
        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/roboto/"+fontname);
        return typeFace;
    }
 void   tryToClearMessageBufferIfExixts(Context context ,String key)

    {
        PrefManager  prefManager = new PrefManager(context);
        try {
            prefManager.clearKeyMessage(key);
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
