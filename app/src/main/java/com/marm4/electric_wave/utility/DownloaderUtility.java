package com.marm4.electric_wave.utility;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.marm4.electric_wave.Interface.OnProfilePictureCompleteListener;
import com.marm4.electric_wave.global.CurrentUser;
import com.marm4.electric_wave.model.User;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DownloaderUtility {

    public static void setProfilePicture(User user, File directory, Context context, OnProfilePictureCompleteListener listener){
        String fileName = user.getId() + user.getLastUpdate() + ".jpg";
        File file = new File(directory, fileName);

        Log.i("DownloaderUtility", "New DownloaderUtility create");

        if(file.exists()){
            Log.i("DownloaderUtility", "File exist");
            user.setProfilePicture(Uri.fromFile(file));
            if(listener!=null){
                Log.i("DownloaderUtility", "Listener exist");
                listener.onProfilePictureCompleteListener();
            }
        }
        else if(user.getProfilePictureUrl() != null && !user.getProfilePictureUrl().isEmpty()){
            Log.i("DownloaderUtility", "File no exist");
            downloadProfilePicture(user, context, fileName, file, listener);
        }
        else{
            Log.i("DownloaderUtility", "no file & no url");
            if(listener!=null)
                listener.onProfilePictureCompleteListener();
        }

    }

    private static void downloadProfilePicture(User user, Context context, String fileName, File file, OnProfilePictureCompleteListener listener){
        BroadcastReceiver downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    Log.i("DownloaderUtility", "Success on downloading pp");
                    user.setProfilePicture(Uri.fromFile(file));
                    if(listener!=null)
                        listener.onProfilePictureCompleteListener();
                }
            }
        };

        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(downloadReceiver, filter);

        Log.i("DownloaderUtility", "Getting profile picture for: " + user.getUserName());

        String imageUrl = user.getProfilePictureUrl();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(imageUrl))
                .setTitle("Profile Image")
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalFilesDir(context, "Electric-Wave-PP", fileName);
        downloadManager.enqueue(request);
    }
}

