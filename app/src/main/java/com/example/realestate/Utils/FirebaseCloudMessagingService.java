package com.example.realestate.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.realestate.Activities.LoginActivity;
import com.example.realestate.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

public class FirebaseCloudMessagingService extends FirebaseMessagingService {

    public FirebaseCloudMessagingService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {

        Log.d("FirebaseCloud","message received from cloud");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri imageUrl = remoteMessage.getNotification().getImageUrl();
            Bitmap bmp = null;
            try {
                InputStream in = new URL(imageUrl.toString()).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                Log.d("FirebaseCloud","Image is null");
            }

            createNotification(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody(),bmp);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotification(String title,String text,Bitmap image){

        // create notification manager to handle all notification related tasks
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        // create a notification channel to send all the notifications through this channel
        String NOTIFICATION_CHANNEL_ID = "com.example.realestate.Utils.test";
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification", NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);


        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, LoginActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // create a builder that sets the properties of the notification to be shown
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notifBuilder.setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.common_full_open_on_phone)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(contentIntent);

        if(image!=null){
            notifBuilder.setLargeIcon(image);
        }

        // finally create the notification using the notification manager
        manager.notify(new Random().nextInt(), notifBuilder.build());

    }
}
