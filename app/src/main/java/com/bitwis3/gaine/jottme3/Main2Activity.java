package com.bitwis3.gaine.jottme3;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {


    Button editButton;
    Button addButton;
    Button deleteButton;
    Spinner spinnerForFolder;
    Cursor cursor = null;
    DBHelper helper = null;
    SQLiteDatabase db = null;
    ContentValues values;
    ArrayAdapter<String> adapter;
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
        setContentView(R.layout.activity_main2);
        helper = new DBHelper(this, "db_of_entries", null, 11);
        db = helper.getWritableDatabase();
        values = new ContentValues();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabfolder);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              MainActivity.reload = true;
               Main2Activity.this.finish();
            }
        });

        spinnerForFolder = (Spinner) findViewById(R.id.spinner2);
        addButton = (Button) findViewById(R.id.add_new_folder);
        editButton = (Button)findViewById(R.id.edit_folder);
        deleteButton = (Button) findViewById(R.id.delete_folder);

  loadFolderSpinner();

    }

    public void addNewFolder(View view){
        final Dialog dialog = new Dialog(Main2Activity.this);

        View mView = getLayoutInflater().inflate(R.layout.dialog_for_folder, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        final EditText editText = (EditText) mView.findViewById(R.id.etForFolder);
        final Button saveButton = (Button) mView.findViewById(R.id.saveButtonInFolderDialog);
        final Button cancelButton = (Button) mView.findViewById(R.id.cancelButtonInFolderDialog);
        editText.setHint("Enter Folder Name");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText.getText().length() > 0 && !editText.getText().toString().equals("All Folders"
                ) && !editText.getText().toString().equals("Notifications"
                )
                     && !editText.getText().toString().contains("'")){
                    String newFolder =   editText.getText().toString();
                    values.put("folder", newFolder);

                    db.insert("table_of_folders","", values);
                    values.clear();
                    loadFolderSpinner();
                    spinnerForFolder.setSelection(adapter.getPosition(editText.getText().toString()));

                    dialog.dismiss();

                    Toast.makeText(getApplicationContext(), newFolder + " added", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),
                            "Folder must not be blank and must not include a ' or be Notifications or All Folders!"
                            , Toast.LENGTH_LONG).show();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                    dialog.dismiss();
                                            }
                                        });
        dialog.setContentView(mView);
        dialog.show();


    }
    public void editFolder(View view){
        if (!spinnerForFolder.getSelectedItem().toString().equals("") &&
                !spinnerForFolder.getSelectedItem().toString().equals("All Folders")&&
        !spinnerForFolder.getSelectedItem().toString().equals("Notifications")) {
            final Dialog dialog = new Dialog(Main2Activity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            final String inSpinnerNow = spinnerForFolder.getSelectedItem().toString();
            final String whereArg = "folder ='"+inSpinnerNow+"'";
            View mView = getLayoutInflater().inflate(R.layout.dialog_for_folder, null);


            final EditText editText = (EditText) mView.findViewById(R.id.etForFolder);
            editText.setHint(inSpinnerNow);
            final Button saveButton = (Button) mView.findViewById(R.id.saveButtonInFolderDialog);
            final Button cancelButton = (Button) mView.findViewById(R.id.cancelButtonInFolderDialog);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (editText.getText().length() > 0 && !editText.getText().toString().equals("All Folders")
                            && !editText.getText().toString().equals("Notifications")&&
                            !getStringArrayListForFolders().contains(editText.getText().toString()) &&
                            !editText.getText().toString().equals("noData")
                            && !editText.getText().toString().contains("'")) {
                        String newFolder = editText.getText().toString();
                        values.put("folder", newFolder);
                        db.update("table_of_entries", values, whereArg, null);
                        db.update("table_of_folders", values, whereArg, null);
                        values.clear();
                        loadFolderSpinner();
                        spinnerForFolder.setSelection(adapter.getPosition(editText.getText().toString()));

                        dialog.dismiss();
                        Toast.makeText(getApplicationContext(), inSpinnerNow + " changed to " + newFolder, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Folder already exists here please choose another or check to make sure spinner isn't empty",
                                Toast.LENGTH_LONG).show();
                    }
                }

            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
                });
            dialog.setContentView(mView);
            dialog.show();
        } else{Toast.makeText(getApplicationContext(), "You must first create a folder, then you can edit if you wish, also Notifications and All Folders can't be altered.",
                Toast.LENGTH_LONG).show();
        }}


    public void deleteFolder(View view){
        final String inFolder = spinnerForFolder.getSelectedItem().toString();
        if(inFolder.equals("All Folders") || inFolder.equals("Notifications")){

            Toast.makeText(getApplicationContext(), "This Folder can not be deleted",
                Toast.LENGTH_LONG
        ).show();}else {
            final String whereClause = "folder ='" + inFolder + "';";
            final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            if (getStringArrayListForFolders().size() > 0) {
                                db.delete("table_of_folders", whereClause, null);
                                db.delete("table_of_folders", whereClause, null);
                                spinnerForFolder.setAdapter(null);
                                loadFolderSpinner();
                                Toast.makeText(getApplicationContext(), inFolder + " deleted!", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), " Nothing to delete!!", Toast.LENGTH_LONG).show();
                            }
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:

                            break;
                    }

                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(Main2Activity.this);
            builder.setMessage("Are you sure? this will also delete all Jotts in this folder ("+ inFolder + "), consider renaming the folder or moving Jotts first!").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
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

    public void loadFolderSpinner() {
        if (getStringArrayListForFolders() != null) {
            if (getStringArrayListForFolders().size() > 0) {
                adapter = new ArrayAdapter<String>(Main2Activity.this,
                        R.layout.spinnerz, getStringArrayListForFolders());

                adapter.setDropDownViewResource(R.layout.spinnerzdrop);
                spinnerForFolder.setAdapter(adapter);
            }
        }else {
            spinnerForFolder.setAdapter(null);


        }

    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs

        MainActivity.reload = true;
        Main2Activity.this.finish();
    }


}
