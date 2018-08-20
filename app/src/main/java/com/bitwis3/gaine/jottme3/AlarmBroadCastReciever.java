package com.bitwis3.gaine.jottme3;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.NOTIFICATION_SERVICE;

public class AlarmBroadCastReciever extends BroadcastReceiver {


    String message;
    int requestCodePassed;
    @Override
    public void onReceive(Context context, Intent intent) {



        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(intent.hasExtra("param")){
            message =  intent.getStringExtra("param");
            requestCodePassed = intent.getIntExtra("requestCodePassed",0);
            NotificationManager notificationMgr;
            RemoteViews contentView = new RemoteViews("com.bitwis3.gaine.jottme3", R.layout.custom_notification);
            contentView.setImageViewResource(R.id.image, R.mipmap.pencil);
            contentView.setTextViewText(R.id.notificationtitle, "My friend, the Jott you placed... ");
            contentView.setTextViewText(R.id.notificationtext, message);






            Notification.Builder builder =
                    new Notification.Builder(context);

            builder.setSmallIcon
                    (R.drawable.ic_pages_black_24dp);
            Intent intent2 =
                    new Intent(context, NotificationDelete.class);
          //  intent2.putExtra("jott", message);
            intent2.setData(Uri.parse("jott:" + message));
            intent2.putExtra("requestCodePassed", requestCodePassed);
            intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, requestCodePassed
                            , intent2, 0);
            builder.setContentIntent(pendingIntent);
            builder.setContent(contentView);
            builder.setContentTitle(message);

            builder.setAutoCancel(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Create the NotificationChannel
                CharSequence name = "default use";
                String description = "get reminders from this app";
                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                NotificationChannel mChannel = new NotificationChannel("ChannelID", name, importance);
                mChannel.setDescription(description);
                // Register the channel with the system; you can't change the importance
                // or other notification behaviors after this
                NotificationManager notificationManager = (NotificationManager) context.getSystemService(
                        NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(mChannel);
                builder.setChannelId("ChannelID");
            }

            Notification notification = builder.build();



            notification.flags |= Notification.FLAG_AUTO_CANCEL;
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;

                notificationMgr = (NotificationManager)
                        context.getSystemService(NOTIFICATION_SERVICE);


                notificationMgr.notify(requestCodePassed, notification);
        }else if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.i("JOSH", "BOOT COMPLETE");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(new Intent(context, MyService.class));
            } else {
                context.startService(new Intent(context, MyService.class));
            }
        }
       }


}
