package com.bitwis3.gaine.jottme3;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import java.util.Calendar;

public class MyService extends Service {
    public MyService() {
    }
    Cursor cursor = null;
    DBHelper helper;
    SQLiteDatabase db;
    int dayN;
    int yearN;
    int hourN;
    int minuteN;
    int monthN;
    String entry;
    int numNotifications = 0;
    int requestCode;
    String date;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.i("JOSH", "Service has been started!!");
      return null;
    }

    @Override
    public int onStartCommand(Intent intent,
                              int flags, int startId) {

        startForeground(2, getNotificationforAuto());

         helper = new DBHelper(this, "db_of_entries", null, 11);
         db = helper.getWritableDatabase();

        Log.i("JOSH", "onStartCommandCalled");
        cursor = db.rawQuery("SELECT * from table_of_entries WHERE has_notification = 'yes'", null);

if (cursor != null && cursor.moveToFirst()) {
    findNumNotifications();
    startBuilding();
}else{
    stopForeground(true);
    stopSelf();
    MyService.this.onDestroy();
}



        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("JOSH", "service Destroyed");
    }


    public void startBuilding() {
    cursor.moveToFirst();

    do{
        buildThePackage();
        createNotification2();}
        while (cursor.moveToNext());
    stopForeground(true);
    stopSelf();
    MyService.this.onDestroy();

    }

    public void findNumNotifications(){
        cursor.moveToFirst();
        do {

            numNotifications = numNotifications+1;
            Log.i("JOSHc1",cursor.getString(0));
            Log.i("JOSH",cursor.getString(1));
            Log.i("JOSH",cursor.getString(2));
            Log.i("JOSH",cursor.getString(3));
            Log.i("JOSH",cursor.getString(4));
            Log.i("JOSH",cursor.getString(5));
            Log.i("JOSH",cursor.getString(6));
            Log.i("JOSH",cursor.getString(7));
            Log.i("JOSH",cursor.getString(8));
            Log.i("JOSH",cursor.getString(9));
            Log.i("JOSH",cursor.getString(10));
            Log.i("JOSH", "Number of  Notifications now = " +numNotifications);
        }
        while(cursor.moveToNext());
    }
    public void buildThePackage(){
        entry = cursor.getString(1);
        yearN = cursor.getInt(4);
        monthN = cursor.getInt(5);
        dayN = cursor.getInt(6);
        hourN = cursor.getInt(7);
        minuteN = cursor.getInt(8);
        requestCode = cursor.getInt(10);
        date = cursor.getString(2);
    }
    public void createNotification2(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(Calendar.YEAR, yearN);
        cal.set(Calendar.MONTH, monthN);
        cal.set(Calendar.DAY_OF_MONTH, dayN);
        cal.set(Calendar.HOUR_OF_DAY, hourN);
        cal.set(Calendar.MINUTE, minuteN);
        cal.set(Calendar.SECOND, 0);


        //    cal.set(2017,11,2,22,46);
        long calTime = cal.getTimeInMillis();
        long currentTime = System.currentTimeMillis();
        Log.i("JOSH1", "JOSH" + calTime);
        Log.i("JOSH2", "that was milli time set after boot");


        Intent intent = new Intent(this, AlarmBroadCastReciever.class);
        intent.putExtra("param", entry);
        intent.putExtra("requestCodePassed", requestCode);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        //  alarmManager1.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent1);
        alarmManager1.setExact(AlarmManager.RTC_WAKEUP, calTime, pendingIntent1);


    }

    private Notification getNotificationforAuto(){
        RemoteViews contentView = new RemoteViews("com.bitwis3.gaine.jottme3", R.layout.custom_notification);
        contentView.setImageViewResource(R.id.image, R.mipmap.pencil);

        contentView.setTextViewText(R.id.notificationtext, "Rebuilding notifications");


        Notification.Builder builder =
                new Notification.Builder(this);

        builder.setSmallIcon
                (R.drawable.ic_blur_on_black_24dp);

        Intent intent2 =
                new Intent(this, MainActivity.class);




        PendingIntent pendingIntent =
                PendingIntent.getActivity(this, 1000000001
                        , intent2, 0);



        builder.setContentIntent(pendingIntent);
        builder.setContent(contentView);

        builder.setContentTitle("Rebuilding notifications");;


        builder.setAutoCancel(true);
        builder.setPriority(Notification.PRIORITY_MAX);
        builder.setDefaults(Notification.DEFAULT_VIBRATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            CharSequence name = "default use";
            String description = "get reminders from this app";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel("ChannelID", name, importance);
            mChannel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
            builder.setChannelId("ChannelID");
        }

        Notification notification = builder.build();

        return notification;
    }

}
