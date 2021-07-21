package cu.phibrain.plugins.cardinal.io.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.annotation.DrawableRes;

import java.io.ByteArrayOutputStream;

import eu.geopaparazzi.library.images.ImageUtilities;

public class ImageUtil {
    public static Bitmap convertToBitmapFromBase64(String base64Str) throws IllegalArgumentException {
        // data:image/jpg;base64, ...
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static byte[] convertToBytesFromBase64(String base64Str) throws IllegalArgumentException {
        // data:image/jpg;base64, ...
        byte[] decodedBytes = Base64.decode(
                base64Str.substring(base64Str.indexOf(",") + 1),
                Base64.DEFAULT
        );

        return decodedBytes;
    }


    public static String convertToBase64(Bitmap bitmap, String ext) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

        String header = String.format("data:image/%s;base64,", ext);

        return header + Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
    }

    public static Bitmap getThumbnail(Bitmap image) {
        int width = image.getWidth();
        int height = image.getHeight();
        // define sampling for thumbnail
        float sampleSizeF = (float) width / (float) ImageUtilities.THUMBNAILWIDTH;
        float newHeight = height / sampleSizeF;
        Bitmap thumbnail = getScaledBitmap(image, ImageUtilities.THUMBNAILWIDTH, (int) newHeight, false);

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, stream);
//        byte[] thumbnailBytes = stream.toByteArray();
        return thumbnail;
    }

    public static Bitmap getScaledBitmap(Bitmap b, int reqWidth, int reqHeight, boolean filter) {
        int bWidth = b.getWidth();
        int bHeight = b.getHeight();

        int nWidth = bWidth;
        int nHeight = bHeight;

        if (nWidth > reqWidth) {
            int ratio = bWidth / reqWidth;
            if (ratio > 0) {
                nWidth = reqWidth;
                nHeight = bHeight / ratio;
            }
        }

        if (nHeight > reqHeight) {
            int ratio = bHeight / reqHeight;
            if (ratio > 0) {
                nHeight = reqHeight;
                nWidth = bWidth / ratio;
            }
        }

        return Bitmap.createScaledBitmap(b, nWidth, nHeight, filter);
    }

    public static byte[] getScaledBitmapAsByteArray(Bitmap b, int reqWidth, int reqHeight, boolean filter) {

        Bitmap scaled = getScaledBitmap(b, reqWidth, reqHeight, filter);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaled.compress(Bitmap.CompressFormat.WEBP, 90, stream);
        return stream.toByteArray();
    }

    public static Bitmap getBitmap(Context context, @DrawableRes final int resId) {
        Drawable drawable = context.getResources().getDrawable(resId);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
