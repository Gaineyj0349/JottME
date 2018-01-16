package com.bitwis3.gaine.jottme3;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomizeEntry1 extends AppCompatActivity {
    Cursor cursor = null;
    DBHelper helper = null;
    SQLiteDatabase db = null;
    String passedFolder;
    EditText et;
    ArrayAdapter<String> adapter;
String editEntryPassedString;
Spinner spinnerForFolder;
String isANotification;
String inFolderNow;

    String editEntryNewString;
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
        setContentView(R.layout.activity_customize_entry1);
        helper = new DBHelper(this, "db_of_entries", null, 11);
        db = helper.getWritableDatabase();
        spinnerForFolder = (Spinner) findViewById(R.id.spinner2incustomize);


        Intent intent = getIntent();
        isANotification = intent.getStringExtra("itemANotification");
        editEntryPassedString = intent.getStringExtra("itemEntryBody");
        passedFolder = intent.getStringExtra("passedFolder");
        loadFolderSpinner();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        et = (EditText) findViewById(R.id.customizeEditTextBox);
        et.setText(editEntryPassedString);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inFolderNow = spinnerForFolder.getSelectedItem().toString();

                returnEditedJott();


            }
        });




}
    public void loadFolderSpinner() {
        if (getStringArrayListForFolders() != null) {
            if (getStringArrayListForFolders().size() > 0) {
                adapter = new ArrayAdapter<String>(CustomizeEntry1.this,
                        R.layout.spinnerz, getStringArrayListForFolders());

                adapter.setDropDownViewResource(R.layout.spinnerzdrop);
                spinnerForFolder.setAdapter(adapter);
            }
        }else {
            spinnerForFolder.setAdapter(null);


        }


        spinnerForFolder.setSelection(adapter.getPosition(passedFolder));;
    }


   public void returnEditedJott() {
if((!inFolderNow.equals("Notifications")&& isANotification.equals("no"))||(inFolderNow.equals("Notifications")&& isANotification.equals("yes")) ){
       hideSoftKeyboard(this);
       Toast.makeText(getApplicationContext(), R.string.snackbarJottEdit,
               Toast.LENGTH_SHORT).show();
        editEntryNewString = et.getText().toString();
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("changedEntryBody", editEntryNewString);
        intent.putExtra("passedFolder2", inFolderNow);
       Log.i("JOSH folder 2", inFolderNow);
        setResult(RESULT_OK,intent);
        if(isANotification.equals("yes")){
            intent.putExtra("itemANotification", isANotification);
            Log.i("JOSH","Service was started");
        }

        finish();}else
{Toast.makeText(getApplicationContext(), "Notifications are app reserved, Jotts can not be moved in or out of the folder, Notification text can " +
        "only be changed, not" +
        " the date.", Toast.LENGTH_LONG).show();}

    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public ArrayList<String> getStringArrayListForFolders(){
        ArrayList<String> foldersArrayList = new ArrayList<>();
        String query = "SELECT distinct folder FROM  table_of_folders;";
        cursor = null;
        cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        if (cursor != null && cursor.getCount() > 0) {

            do {
                foldersArrayList.add(new String(cursor.getString(0
                )));
            } while (cursor.moveToNext());
        }


        if(foldersArrayList.size()>0){

            return foldersArrayList;} else{
            return null;
        }
    }

}
