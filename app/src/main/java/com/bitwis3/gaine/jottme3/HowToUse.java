package com.bitwis3.gaine.jottme3;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class HowToUse extends AppCompatActivity {
FloatingActionButton fab;
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
            case "Dusk":
                setTheme(R.style.AppTheme6);

                break;




        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        TextView tvtoEmail = (TextView) findViewById(R.id.email);
        TextView tvtoVid = (TextView) findViewById(R.id.tutorial);

        tvtoEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto","gaineyj0349@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));

            }
        });

        tvtoVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                watchYoutubeVideo(HowToUse.this, "qvVqBEz4YRU");
            }
        });
        fab = (FloatingActionButton) findViewById(R.id.floatingActionButton33);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               HowToUse.this.finish();
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs


        HowToUse.this.finish();
    }
    public static void watchYoutubeVideo(Context context, String id){
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + id));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }
}
