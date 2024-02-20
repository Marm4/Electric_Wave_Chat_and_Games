package com.marm4.electric_wave.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ImageUtility {

    public static Uri reduceQuality(Context context, Uri originalUri, int qualityPercentage) {
        try {
            if(originalUri==null)
                return null;

            Bitmap originalBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(originalUri));
            int quality = (qualityPercentage > 0 && qualityPercentage <= 100) ? qualityPercentage : 10;

            File outputFile = createTempImageFile(context);
            OutputStream outputStream = new FileOutputStream(outputFile);
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

            originalBitmap.recycle();
            outputStream.close();


            return Uri.fromFile(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Uri reduceSize(Context context, Uri originalUri, int targetSizeKB) {
        try {

            Bitmap originalBitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(originalUri));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            originalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            int initialSizeKB = outputStream.size() / 1024;
            int quality = 100;

            if (initialSizeKB <= targetSizeKB) {
                return originalUri;
            }


            double scaleFactor = Math.sqrt((double) targetSizeKB / initialSizeKB);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, (int) (originalBitmap.getWidth() * scaleFactor), (int) (originalBitmap.getHeight() * scaleFactor), true);
            File outputFile = createTempImageFile(context);
            OutputStream fileOutputStream = new FileOutputStream(outputFile);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, fileOutputStream);


            originalBitmap.recycle();
            resizedBitmap.recycle();
            fileOutputStream.close();


            return Uri.fromFile(outputFile);
        } catch (IOException e) {
            e.printStackTrace();

            return null;
        }
    }

    private static File createTempImageFile(Context context) throws IOException {
        String timeStamp = String.valueOf(System.currentTimeMillis());
        String imageFileName = "temp_" + timeStamp;
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

}
