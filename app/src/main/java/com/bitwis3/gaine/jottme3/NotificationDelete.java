package com.bitwis3.gaine.jottme3;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;



public class NotificationDelete extends AppCompatActivity {
       SQLiteDatabase db;
    String jott = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        SharedPreferences prefs = getSharedPreferences("THEME", MODE_PRIVATE);
        String retrievedColorTheme = prefs.getString("themeColor", "Blue");
        Log.d("JOSH", retrievedColorTheme);
        switch (retrievedColorTheme){

            case "Blue":
                setTheme(R.style.AppTheme1);

                break;

            case "Purple":
                setTheme(R.style.AppTheme2);

                break;
            case "Vibrant":
                setTheme(R.style.AppTheme3);

                break;
            case "Mellow":
                setTheme(R.style.AppTheme4);

                break;
            case "Night":
                setTheme(R.style.AppTheme5);

                break;
            case "Twilight":
                setTheme(R.style.AppTheme6);

                break;




        }



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_delete);
        final TextView textView = (TextView)findViewById(R.id.notificationtv);

       final DBHelper helper = new DBHelper(this, "db_of_entries", null, 11);
       db = helper.getWritableDatabase();

       Intent intent = getIntent();

        jott = intent.getData().getSchemeSpecificPart();
    //  jott = getIntent().getStringExtra("jott");

        textView.setText(jott);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabtodeletenotification);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              deleteEntry();
                MainActivity.reload = true;
                NotificationDelete.this.finish();
                Snackbar.make(view, "Deleted my friend!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }
    public void deleteEntry(){
        db.delete("table_of_entries"," entry_body = '" + jott + "' AND folder like 'Notifications';"
               ,null);
    }
    @Override
    protected void onPause() {
        super.onPause();
    NotificationDelete.this.finish();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (db.isOpen()) {
            db.close();
        }
    }

    boolean double_backpressed = false;
    @Override
    public void onBackPressed(){
        if(double_backpressed) {
            super.onBackPressed();

            MainActivity.reload = true;

            return;
        }
        this.double_backpressed=true;
        Toast.makeText(getApplicationContext(),"Click back again to exit, Jott notification remains active but can be deleted within app.",Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                double_backpressed = false;
            }
        },2000);

    }
}
