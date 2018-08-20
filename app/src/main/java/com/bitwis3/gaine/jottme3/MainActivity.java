    package com.bitwis3.gaine.jottme3;

    import android.app.Activity;
    import android.app.AlarmManager;
    import android.app.AlertDialog;
    import android.app.Dialog;
    import android.app.PendingIntent;
    import android.content.ClipData;
    import android.content.ClipboardManager;
    import android.content.ContentValues;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.content.SharedPreferences;
    import android.database.Cursor;
    import android.database.sqlite.SQLiteDatabase;
    import android.graphics.Color;
    import android.graphics.drawable.ColorDrawable;
    import android.net.Uri;
    import android.os.Build;
    import android.os.Bundle;
    import android.support.annotation.NonNull;
    import android.support.design.widget.BottomNavigationView;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.PopupMenu;
    import android.support.v7.widget.Toolbar;
    import android.text.Editable;
    import android.text.TextWatcher;
    import android.util.Log;
    import android.view.ContextMenu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.Window;
    import android.view.WindowManager;
    import android.view.inputmethod.InputMethodManager;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.FilterQueryProvider;
    import android.widget.ImageButton;
    import android.widget.ListView;
    import android.widget.RadioGroup;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.TimePicker;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.Calendar;

    import osmandroid.project_basics.Task;

    public class MainActivity extends AppCompatActivity {


        Button editMove;
        Button editDelete;
        Button editCancel;
        View view1000;
        View view10000;
        EditText editTextForSearch;
        Spinner spinnerEdit;
        TextView infoTextView;
        EditText entryText;
        ListView listToHoldEntries;
        EditText editText;
        String folder2 = "";
        Cursor cursor = null;
        DBHelper helper = null;
        SQLiteDatabase db = null;
        String testString;
                String itemEntryBody;
        String itemEntryDate;
        String itemEntryID;
        String spinnerChangover = "";
        final int MY_REQUEST_CODE = 29;
        ContentValues values = null;
        Toolbar toolbar;
        DBcursorAdapter dbCursorAdapter;
        String entry;
        String date;
        String filename;
        FloatingActionButton fab;
     //   FloatingActionButton fab2;
        BottomNavigationView navigation;
        String resultEntryBodyString;
        ClipboardManager clipboard;
        ClipData clip;
        ImageButton button;
        ImageButton buttonSearch;
        Spinner spinner;
        ArrayAdapter<String> adapter;
        public static boolean reload = false;
        int monthN = 99;
        int dayN = 99;
        int yearN = 99;
        int hourN = 99;
        int minuteN = 99;
        int offSetMonthN = 99;
        int notificationsRequestCode;
        boolean alreadyClicked = false;




        private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                =  new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.entry_page:
                 /*       spinner.setVisibility(View.VISIBLE);
                        spinnerEdit.setVisibility(View.GONE);
                        editMove.setVisibility(View.GONE);
                        view1000.setVisibility(View.GONE);
                        editDelete.setVisibility(View.GONE);
                        editCancel.setVisibility(View.GONE);
                        view1000.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);

*/                      flipToNormalMode();
                        fab.setVisibility(View.VISIBLE);
                        buttonSearch.setVisibility(View.GONE);
                        entryText.setVisibility(View.VISIBLE);
                        listToHoldEntries.setVisibility(View.INVISIBLE);

                        return true;


                    case R.id.jott_page:

                        Log.i("JOSH jott page ID", "hello " + R.id.jott_page);
                        buttonSearch.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
              //          fab2.setVisibility(View.GONE);

                        flipToNormalMode();
                        entryText.setVisibility(View.INVISIBLE);

                        listToHoldEntries.setVisibility(View.VISIBLE);
                        updateJottListViewBasedOffSpinner();
                        return true;

                }
                return false;
            }

        };


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
            setContentView(R.layout.activity_main);
             editMove = (Button)findViewById(R.id.move_many);
             editDelete=(Button)findViewById(R.id.delete_many);
             editCancel=(Button)findViewById(R.id.cancel_many);
             view1000 = (View)findViewById(R.id.view1000);
             view10000 = (View)findViewById(R.id.view10000);
             editTextForSearch = (EditText)findViewById(R.id.editTextToSearch);
             view1000.setVisibility(View.GONE);
            view10000.setVisibility(View.GONE);
            editTextForSearch.setVisibility(View.GONE);
            editMove.setVisibility(View.GONE);
            editDelete.setVisibility(View.GONE);
            editCancel.setVisibility(View.GONE);
            listToHoldEntries = (ListView) findViewById(R.id.listToHoldEntries);
            listToHoldEntries.setTextFilterEnabled(true);
            values = new ContentValues();
            helper = new DBHelper(this, "db_of_entries", null, 11);
            db = helper.getWritableDatabase();
            spinner = (Spinner)findViewById(R.id.spinner);
            spinnerEdit = (Spinner)findViewById(R.id.spinnerEDIT);
            spinnerEdit.setVisibility(View.GONE);
            SharedPreferences prefs2 = getSharedPreferences("FIRSTENTRY", MODE_PRIVATE);
            String firstSpinnerEntry = prefs2.getString("firstrun", "yes");

            if(firstSpinnerEntry.equals("yes")){
                Log.i("JOSHfirstentry", firstSpinnerEntry);
                doFirstEntry();
            }


          //  fab2 = (FloatingActionButton) findViewById(R.id.mainActFabReset);
            infoTextView = (TextView) findViewById(R.id.infoTextView);


            entryText = (EditText) findViewById(R.id.editText);


button = (ImageButton)findViewById(R.id.buttonmenu);
            buttonSearch = (ImageButton)findViewById(R.id.buttonsearch);

            listToHoldEntries.setVisibility(View.INVISIBLE);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                                    @Override
                                                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                                    updateJottListViewBasedOffSpinner();

                                                                    }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }});
            spinnerEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    updateJottListViewBasedOffSpinnerEditMode();

                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }});


                                                                    //fab2.setVisibility(View.GONE);
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
            registerForContextMenu(listToHoldEntries);



            fab = (FloatingActionButton) findViewById(R.id.mainActFab);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addJott();
                }
            });
            fab.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    entry = entryText.getText().toString();
            if (entry.isEmpty()){
                        Toast.makeText(getApplicationContext(), R.string.where_jott,
                                Toast.LENGTH_SHORT).show();}else{
                    inflateNotificationDialog();}
                    return false;
                }

            });




            loadFolderSpinner();
            fab.setVisibility(View.VISIBLE);
            //    fab2.setVisibility(View.GONE);

            entryText.setVisibility(View.VISIBLE);
            listToHoldEntries.setVisibility(View.INVISIBLE);

            navigation.setSelectedItemId(R.id.entry_page);
            updateJottListViewBasedOffSpinner();








        }

        public void resetAPP() {
            db.delete("table_of_entries", "1", null);
            updateJottListView();
        }


        public void addJott() {

            entry = entryText.getText().toString();
            date = Calendar.getInstance().getTime().toString();

            if(!spinner.getSelectedItem().toString().equals("Notifications")){

            if (entry.length() > 0) {
                // takes jot and date and puts into database named entry_db
                values.put("entry_body", entry);
                values.put("entry_date", date);
                values.put("folder", spinner.getSelectedItem().toString());
                db.insert("table_of_entries", "", values);
                values.clear();
                entryText.setText("");
                hideSoftKeyboard(this);
               dbCursorAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), R.string.jott_added,
                        Toast.LENGTH_SHORT).show();

            } else if (entry.isEmpty())
                Toast.makeText(getApplicationContext(), R.string.where_jott,
                        Toast.LENGTH_SHORT).show();}else{
                Toast.makeText(getApplicationContext(),"Jotts can not be placed in Notifications folder.",Toast.LENGTH_LONG).show();
            }
        }


        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            super.onCreateContextMenu(menu, view, menuInfo);
            MenuInflater inflater = getMenuInflater();



                    inflater.inflate(R.menu.list_menu_item_longpress,
                            menu);

        }



        @Override
        public boolean onContextItemSelected(MenuItem item) {

            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            Cursor c = (Cursor) listToHoldEntries.getItemAtPosition(info.position);
            itemEntryBody = c.getString(1);
            String folder = c.getString(3);
            itemEntryDate = c.getString(2);
            itemEntryID = Integer.toString(c.getInt(0));
            String itemANotification = c.getString(9);
            Log.i("JOSH", itemANotification);

            switch (item.getItemId()) {

                case R.id._delete:

                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:


                                    Toast.makeText(getApplicationContext(), R.string.jott_deleted,
                                            Toast.LENGTH_SHORT).show();
                                    deleteJottSQL(itemEntryDate);
                                 dialog.dismiss();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
dialog.dismiss();
                                    break;
                            }

                        }
                    };

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                            .setNegativeButton("No", dialogClickListener).show();






                    return true;
    //NEED TO MAKE CODE FOR EDITING AN ENTRY
                case R.id._edit:

                    Intent intent = new Intent(this, CustomizeEntry1.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("itemEntryBody", itemEntryBody);
                    intent.putExtra("itemANotification", itemANotification);
                    intent.putExtra("passedFolder", folder);
                    startActivityForResult(intent, MY_REQUEST_CODE);

                    return true;
                case R.id._copy_text:
                    clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("jott", itemEntryBody);
                    clipboard.setPrimaryClip(clip);
                    return true;
            }
            return super.onContextItemSelected(item);
        }


    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if (requestCode == MY_REQUEST_CODE && resultCode == RESULT_OK){
            resultEntryBodyString = intent.getStringExtra("changedEntryBody");
            folder2 = intent.getStringExtra("passedFolder2");
            updateEditedEntry();


                Log.i("JOSH", "PassedFolder2 " + folder2 );

            if(intent.hasExtra("itemANotification")){
                Intent i = new Intent(MainActivity.this, MyService.class);
                startService(i);
            }

        }

    }

        private void updateEditedEntry() {
            Log.i("JOSHYO", folder2);
        if (!folder2.equals("")){
            Log.i("JOSHYO", folder2);
            values.put("folder", folder2);
        }
            String str = "entry_date ='"+ itemEntryDate + "';";
            values.put("entry_body", resultEntryBodyString);
            db.update("table_of_entries", values, str, null );
            values.clear();
            updateJottListViewBasedOffSpinner();
            folder2 = "";
                }


        private void deleteJottSQL(String itemEntryDates) {

     String str = "entry_date ='"+itemEntryDates + "';";

       db.delete("table_of_entries", str , null);
    updateJottListViewBasedOffSpinner();


        }


        public static void hideSoftKeyboard(Activity activity) {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) activity.getSystemService(
                            Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0);
        }

        private void updateJottListView() {

            String getCursor = "select *  from table_of_entries " +
                                      "Order by _id DESC;";
            cursor = db.rawQuery(getCursor, null);
            dbCursorAdapter = new DBcursorAdapter(this, cursor,false);
            listToHoldEntries.setAdapter(dbCursorAdapter);
          //  dbCursorAdapter.notifyDataSetChanged();


        }
        private void updateJottListViewBasedOffSpinner() {
        if(spinner.getSelectedItem().toString().equals("All Folders")){updateJottListView();}
        else{
        if(getStringArrayListForFolders() != null) {
            String getCursor = "select *  from table_of_entries " +
                    "WHERE folder = '" + spinner.getSelectedItem().toString() + "'" +
                    " Order by _id DESC;";
            cursor = db.rawQuery(getCursor, null);
            dbCursorAdapter = new DBcursorAdapter(this, cursor,false);
            listToHoldEntries.setAdapter(dbCursorAdapter);
         //   dbCursorAdapter.notifyDataSetChanged();
        }else {
            listToHoldEntries.setAdapter(null);
        }
        }}

        private void updateJottListViewEditMode() {

            String getCursor = "select *  from table_of_entries " +
                    "Order by _id DESC;";
            cursor = db.rawQuery(getCursor, null);
            dbCursorAdapter = new DBcursorAdapter(this, cursor,true);
            listToHoldEntries.setAdapter(dbCursorAdapter);
            //  dbCursorAdapter.notifyDataSetChanged();


        }
        private void updateJottListViewBasedOffSpinnerEditMode() {
            if(spinnerEdit.getSelectedItem().toString().equals("All Folders")){updateJottListViewEditMode();}
            else{
                if(getStringArrayListForFolders() != null) {
                    String getCursor = "select *  from table_of_entries " +
                            "WHERE folder = '" + spinnerEdit.getSelectedItem().toString() + "'" +
                            " Order by _id DESC;";
                    cursor = db.rawQuery(getCursor, null);
                    dbCursorAdapter = new DBcursorAdapter(this, cursor,true);
                    listToHoldEntries.setAdapter(dbCursorAdapter);
                    //   dbCursorAdapter.notifyDataSetChanged();
                }else {
                    listToHoldEntries.setAdapter(null);
                }
            }}

public void buttonMethod(View view){
    Intent intent = new Intent(this, HowToUse.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    startActivity(intent);
}

        @Override
        protected void onResume() {
            SharedPreferences prefs = getSharedPreferences("NOTIFICATIONREQUESTNUMBER", MODE_PRIVATE);
            notificationsRequestCode = prefs.getInt("currentRequestNumber", 0);

            super.onResume();
            if(reload == true){
            loadFolderSpinner();
updateJottListViewBasedOffSpinner();
            reload = false;}

        }

        @Override
        protected void onDestroy() {
            super.onDestroy();

            if (db.isOpen()) {
                db.close();
            }
        }


        public void showMenu(View v)
        {
            PopupMenu popup = new PopupMenu(this,v);
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.pick_theme:

                            final Dialog dialog = new Dialog(MainActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);


                            View mView = getLayoutInflater().inflate(R.layout.dialog_for_theme_selection, null);
                            final RadioGroup radioGroup = (RadioGroup) mView.findViewById(R.id.radiogroup);
                            final Button saveButton = (Button) mView.findViewById(R.id.dialog_for_theme_SAVE);
                            final Button exitButton = (Button) mView.findViewById(R.id.dialog_for_theme_CANCEL);

                            saveButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                     String selection = "";
                                    int checkedRadioButtonID = radioGroup.getCheckedRadioButtonId();

                                    switch (checkedRadioButtonID) {
                                        case R.id.checkBox1:
                                            selection = "Blue";
                                            break;
                                        case R.id.checkBox2:
                                            selection = "Purple";
                                            break;
                                        case R.id.checkBox3:
                                            selection = "Vibrant";
                                            break;
                                        case R.id.checkBox4:
                                            selection = "Mellow";
                                            break;

                                        case R.id.checkBox5:
                                            selection = "Night";
                                            break;
                                        case R.id.checkBox6:
                                            selection = "Twilight";
                                            break;
                                    }


                                    //    Toast.makeText(getApplicationContext(), "hello" + columnNumberSelected, 2).show();
                                    dialog.dismiss();
                                    updateTheme(selection);


                                }
                            });
                            exitButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    dialog.dismiss();
                                }
                            });
                            dialog.setContentView(mView);
                            dialog.show();

                            break;

                        case R.id.how_to_use:

                            Intent intent = new Intent(MainActivity.this, HowToUse.class);
                            startActivity(intent);
                            break;
                        case R.id.project_manager:
                            Task.MoreApps(getApplicationContext(), "GainWise");
                            break;
                        case R.id.rate:
                            openMarket("com.bitwis3.gaine.jottme3");

                            break;
                        case R.id.managefolders:
                            Intent intent2 = new Intent(MainActivity.this, Main2Activity.class);
                            startActivity(intent2);

                            break;
 /*                       case R.id.test2:

                            Intent i = new Intent(MainActivity.this, MyService.class);
                            startService(i);

                break;
  */                        case R.id.test:


                            String shareBody = "https://play.google.com/store/apps/details?id=com.bitwis3.gaine.jottme3";

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "APP NAME (Open it in Google Play Store to Download the Application)");

                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
              //  getStringALforBigMoves(DBcursorAdapter.getArrayList());
                     //       navigation.findViewById(R.id.jott_page).performClick();
                         break;
                          case R.id.selectMany:

                            //     Intent i = new Intent(MainActivity.this, MyService.class);
                            //     startService(i);
                            navigation.findViewById(R.id.jott_page).performClick();
                            spinnerChangover = spinner.getSelectedItem().toString();

                            flipToEditMode();
                            Log.i("JOSHspinner", "spinner change");
                            loadFolderSpinnerEdit();



                            updateJottListViewBasedOffSpinnerEditMode();

                            break;

                    }


                    return false;
                }
            });// to implement on click event on items of menu
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.contextformenu, popup.getMenu());
            popup.show();
        }

        private void openMarket(String PackageName) {

            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+PackageName)));
            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public void updateTheme(String selection){
            SharedPreferences.Editor editor2 = getSharedPreferences("THEME", MODE_PRIVATE).edit();
            editor2.putString("themeColor", selection);

            editor2.apply();
            Log.d("JOSH", selection );
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
                    adapter = new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinnerz, getStringArrayListForFolders());

                    adapter.setDropDownViewResource(R.layout.spinnerzdrop);
                    spinner.setAdapter(adapter);
                }
            }else {
                spinner.setAdapter(null);


            }
            spinner.setSelection(adapter.getPosition("All Folders"));

        }
        public void loadFolderSpinnerEdit() {
            if (getStringArrayListForFolders() != null) {
                if (getStringArrayListForFolders().size() > 0) {
                    adapter = new ArrayAdapter<String>(MainActivity.this,
                            R.layout.spinnerz, getStringArrayListForFolders());

                    adapter.setDropDownViewResource(R.layout.spinnerzdrop);
                    spinnerEdit.setAdapter(adapter);
                }
            }else {
                spinnerEdit.setAdapter(null);


            }
            spinnerEdit.setSelection(adapter.getPosition(spinnerChangover));

        }

        public void doFirstEntry(){

            values.put("folder", "All Folders");

            db.insert("table_of_folders","", values);
            values.clear();
            values.put("folder", "Notifications");

            db.insert("table_of_folders","", values);
            values.clear();

            SharedPreferences.Editor editor3 = getSharedPreferences("FIRSTENTRY", MODE_PRIVATE).edit();
            editor3.putString("firstrun", "no");


            editor3.apply();
            loadFolderSpinner();
            updateJottListViewBasedOffSpinner();

            final Dialog dialog20 = new Dialog(MainActivity.this);
            View mview20 = getLayoutInflater().inflate(R.layout.dialogfirstrun,null);
            dialog20.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Button yesB = mview20.findViewById(R.id.yesB);
            Button noB = mview20.findViewById(R.id.noB);

            yesB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog20.dismiss();
                    Intent intent99 = new Intent(MainActivity.this, HowToUse.class);
                    startActivity(intent99);

                }
            });
            noB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog20.dismiss();


                }
            });


            dialog20.setContentView(mview20);
            dialog20.show();
        }

        public void createNotification() {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(System.currentTimeMillis());
            cal.clear();
            cal.set(Calendar.YEAR, yearN);
            cal.set(Calendar.MONTH, offSetMonthN);
            cal.set(Calendar.DAY_OF_MONTH, dayN);
            cal.set(Calendar.HOUR_OF_DAY, hourN);
            cal.set(Calendar.MINUTE, minuteN);
            cal.set(Calendar.SECOND, 0);

            Log.i("JOSHmay", String.valueOf(yearN));
            Log.i("JOSHmam", String.valueOf(offSetMonthN));
            Log.i("JOSHmad", String.valueOf(dayN));
            Log.i("JOSHmah", String.valueOf(hourN));
            Log.i("JOSHmam", String.valueOf(minuteN));



            //    cal.set(2017,11,2,22,46);
            long calTime = cal.getTimeInMillis();
            long currentTime = System.currentTimeMillis();
            Log.i("JOSH1", "JOSH" + calTime);
            Log.i("JOSH2", "that was milli time originally set");

            Intent intent = new Intent(this, AlarmBroadCastReciever.class);
            intent.putExtra("param", entry);
            intent.putExtra("requestCodePassed", notificationsRequestCode);
            PendingIntent pendingIntent1 = PendingIntent.getBroadcast(this, notificationsRequestCode, intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            AlarmManager alarmManager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
            //  alarmManager1.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pendingIntent1);
            alarmManager1.setExact(AlarmManager.RTC_WAKEUP, calTime, pendingIntent1);
            entryText.setText("");
            notificationsRequestCode = notificationsRequestCode + 1;
            SharedPreferences.Editor editor3 = getSharedPreferences("NOTIFICATIONREQUESTNUMBER", MODE_PRIVATE).edit();
            editor3.putInt("currentRequestNumber", notificationsRequestCode);

            editor3.apply();

            entry = "";
        }
        public void inflateNotificationDialog(){
            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            View mView = getLayoutInflater().inflate(R.layout.dialogforlongclickaddjott, null);
            final Button buttonToPickDate = (Button)mView.findViewById(R.id.buttonToLaunchDatePicker);
            final Button buttonToPickTime = (Button)mView.findViewById(R.id.buttonToLaunchTimePicker);
            final Button buttonToSaveNotification = (Button)mView.findViewById(R.id.buttonToSaveNotification);
            final Button buttonToCancelNotification = (Button)mView.findViewById(R.id.buttonToCancelNotification);
            final TextView dateTextView = (TextView)mView.findViewById(R.id.textViewforlongclicknotificationDate);
            final TextView timeTextView = (TextView)mView.findViewById(R.id.textViewforlongclicknotificationTime);

            buttonToPickDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {








                   final Dialog dialog2 = new Dialog(MainActivity.this);
                    dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    View mView2 = getLayoutInflater().inflate(R.layout.datepickerdialog, null);
                    final DatePicker datePicker = (DatePicker) mView2.findViewById(R.id.datePicker);
                    final Button saveButtonDate = (Button)mView2.findViewById(R.id.saveButtonInDateDialog);
                    final Button cancelButtonDate = (Button)mView2.findViewById(R.id.cancelButtonInDateDialog);

                    saveButtonDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        offSetMonthN = datePicker.getMonth();
                         monthN = datePicker.getMonth() + 1;
                         dayN = datePicker.getDayOfMonth();
                         yearN = datePicker.getYear();

                        String date = monthN + "/" + dayN + "/" +
                                yearN;

                          dateTextView.setText(date);

                        dialog2.dismiss();
                        }
                    });

                    cancelButtonDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog2.dismiss();
                        }
                    });

                    dialog2.setContentView(mView2);
                    dialog2.show();
                }

            });

            buttonToPickTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    final Dialog dialog3 = new Dialog(MainActivity.this);
                    dialog3.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    View mView3 = getLayoutInflater().inflate(R.layout.time_picker , null);
                    final TimePicker timePicker = (TimePicker)mView3.findViewById(R.id.timePicker);
                    final Button saveButtonTime = (Button)mView3.findViewById(R.id.saveButtonInTimeDialog);
                    final Button cancelButtonTime = (Button)mView3.findViewById(R.id.cancelButtonInTimeDialog);

                    saveButtonTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                hourN = timePicker.getHour();
                                minuteN = timePicker.getMinute();
                            }else{
                                hourN = timePicker.getCurrentHour();
                                minuteN = timePicker.getCurrentMinute();

                            }

                            String time = buildTime(hourN, minuteN);
                            timeTextView.setText(time);
                            dialog3.dismiss();
                        }
                    });

                    cancelButtonTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                    dialog3.dismiss();
                        }
                    });
                    dialog3.setContentView(mView3);
                    dialog3.show();

                }
            });

            buttonToSaveNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    entry = entryText.getText().toString();
                    date = Calendar.getInstance().getTime().toString();

                    if((yearN != 99) && (offSetMonthN != 99) && (dayN != 99) && (hourN != 99) && (minuteN != 99)
                            &&(entry.length() > 0)){
                        values.put("entry_body", entry);
                        values.put("entry_date", date);
                        values.put("folder", "Notifications");
                        values.put("has_notification", "yes");
                        values.put("year", yearN);
                        values.put("month", offSetMonthN);
                        values.put("day", dayN);
                        values.put("hour", hourN);
                        values.put("minute", minuteN);
                        values.put("request_code", notificationsRequestCode);
                        Log.i("JOSH", "This is request code = " + notificationsRequestCode);
                        db.insert("table_of_entries", null, values);
                        values.clear();
                        createNotification();
                        Toast.makeText(getApplicationContext(), "Notification set!",
                                Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "There was not enough data to create notification," +
                                        " check entry and date/time selected!",
                                Toast.LENGTH_LONG).show();

                    }


                    dialog.dismiss();
                }
            });

            buttonToCancelNotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
            dialog.dismiss();
                }
            });



            dialog.setContentView(mView);
            dialog.show();

        }
       public String buildTime(int hourN, int minuteN){
            String time ="";
            String hour = "";
            String minute = "";
           String amPm = "";
             if (hourN >= 12){
                  amPm = "PM";
                 if (hourN == 12){
                     hour = "12";
                 }else {
                     int newHour1 = hourN - 12;
                     hour = String.valueOf(newHour1);
                 }
             }else {
                 amPm = "AM";
                 hour = String.valueOf(hourN);
             }

             if (minuteN < 10){
                 minute = "0"+minuteN;
             }else{minute = String.valueOf(minuteN);}

             time = hour + ":" + minute + " " + amPm;



            return time;

       }
        public void bigMove(View v) {

            final int size = DBcursorAdapter.getArrayList().size();
            Log.i("JOSHarraysize", "HERE: " + size);
            for (int i = 0; i < size; i++) {
                Log.i("JOSHlvpositions", "HERE: " + dbCursorAdapter.getArrayList().get(i));
            }


            if ((size > 0) && (!spinnerEdit.getSelectedItem().toString().equals("Notifications"))&&
                    (!doesListHaveNotification(DBcursorAdapter.getArrayList()))) {


                final Dialog dialog99 = new Dialog(MainActivity.this);
                dialog99.requestWindowFeature(Window.FEATURE_NO_TITLE);
                View mView99 = getLayoutInflater().inflate(R.layout.dialog_for_bigops, null);
                Button saveButton99 = mView99.findViewById(R.id.bigOPSsave);
                Button cancelButton99 = mView99.findViewById(R.id.bigOPScancel);
                final Spinner spinner99 = mView99.findViewById(R.id.dialogspinner);
              //  final ProgressBar progressBar99 = mView99.findViewById(R.id.progress);
              //  progressBar99.setProgress(25);
                saveButton99.setText("MOVE ALL");
                cancelButton99.setText("CANCEL");

                if (getStringArrayListForFolders() != null) {
                    if (getStringArrayListForFolders().size() > 0) {
                        adapter = new ArrayAdapter<String>(MainActivity.this,
                                R.layout.spinnerz, getStringArrayListForFolders());

                        adapter.setDropDownViewResource(R.layout.spinnerzdrop);
                        spinner99.setAdapter(adapter);
                    }
                } else {
                    spinner99.setAdapter(null);


                }
                spinner99.setSelection(adapter.getPosition("All Folders"));


                saveButton99.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!spinner99.getSelectedItem().toString().equals("Notifications")){
                        String inSpinner99 = spinner99.getSelectedItem().toString();
                        Log.i("JOSHinspinner99", inSpinner99);
                        for (int i = 0; i < size; i++) {


                      //      progressBar99.setProgress(50);
                            String dateForUpdate = getStringALforBigMoves(DBcursorAdapter.getArrayList()).get(i).toString();
                            String whereClause = "entry_date = '" + dateForUpdate + "';";
                            values.put("folder", inSpinner99);
                            db.update("table_of_entries", values, whereClause, null);
                            values.clear();
                       //     progressBar99.setProgress(100);
                        }
                        dialog99.dismiss();
                        flipToNormalMode();
                    }else{Toast.makeText(getApplicationContext(),"Notifications is app reserved!", Toast.LENGTH_LONG).show();}}
                });
                cancelButton99.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog99.dismiss();
                    }
                });


                dialog99.setCancelable(true);
                dialog99.setContentView(mView99);
                dialog99.show();

            } else {
                Toast.makeText(getApplicationContext(), "Can not move Notifications or there is nothing to move!", Toast.LENGTH_LONG).show();

            }
        }
        public void bigCancel(View v) {
           flipToNormalMode();
        }
        public void bigDelete(View v) {
            final int size = DBcursorAdapter.getArrayList().size();
            Log.i("JOSHarraysize", "HERE: " + size);
            for (int i = 0; i < size; i++) {
                Log.i("JOSHlvpositions", "HERE: " + dbCursorAdapter.getArrayList().get(i));
            }


            if (size > 0) {


                final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:

                                for (int i = 0; i < size; i++) {


                                    String dateForUpdate = getStringALforBigMoves(DBcursorAdapter.getArrayList()).get(i).toString();
                                    String whereClause = "entry_date = '" + dateForUpdate + "';";

                                    db.delete("table_of_entries", whereClause, null);

                                }
                            flipToNormalMode();
                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }

                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();


            }}




        public ArrayList<String> getStringALforBigMoves(ArrayList<Integer> incomingIntAL) {
            ArrayList<String> ALforbigmoves = new ArrayList<>();

            int size9876 = incomingIntAL.size();
            Log.i("JOSHthesizeingetAL:", "size is:" + size9876);
            for (int i = 0; i < size9876; i++) {
                int position = incomingIntAL.get(i);
                Cursor cursor987 = (Cursor) listToHoldEntries.getItemAtPosition(position);
                String dateString = cursor987.getString(2);
                Log.i("JOSHthedate:", dateString);
                ALforbigmoves.add(dateString);


            }
       return
       ALforbigmoves;
       }

            public boolean doesListHaveNotification(ArrayList<Integer> incomingIntAL) {

                boolean itDoes = false;
                int size9876 = incomingIntAL.size();
                Log.i("JOSHthesizeingetAL:", "size is:" + size9876);
                for (int i = 0; i < size9876; i++) {
                    int position = incomingIntAL.get(i);
                    Cursor cursor987 = (Cursor) listToHoldEntries.getItemAtPosition(position);

                    if(cursor987.getString(9).equals("yes")){
                  itDoes = true;
                    }



                }
                return itDoes;

       }




            /// fetch cursor to build the AL of dates (string), it will return and that will be used
            //to do the big ops moving/deleting.

            //make sure widgets visibility all match and handle clicks.

            //make sure Jotts can't be placed in notifications folder,




        public void flipToEditMode(){
            alreadyClicked = false;
            spinner.setVisibility(View.GONE);
            spinnerEdit.setVisibility(View.VISIBLE);
            editMove.setVisibility(View.VISIBLE);
            view1000.setVisibility(View.VISIBLE);
            view10000.setVisibility(View.GONE);
            editTextForSearch.setVisibility(View.GONE);
            editDelete.setVisibility(View.VISIBLE);
            editCancel.setVisibility(View.VISIBLE);
            fab.setVisibility(View.GONE);

            loadFolderSpinnerEdit();
            updateJottListViewBasedOffSpinnerEditMode();
        }
        public void flipToNormalMode(){
            alreadyClicked = false;
            spinner.setVisibility(View.VISIBLE);
            spinnerEdit.setVisibility(View.GONE);
            editMove.setVisibility(View.GONE);
            view1000.setVisibility(View.GONE);
            view10000.setVisibility(View.GONE);
            editTextForSearch.setVisibility(View.GONE);
            editDelete.setVisibility(View.GONE);
            editCancel.setVisibility(View.GONE);


        DBcursorAdapter.positionAL.clear();
            updateJottListViewBasedOffSpinner();
        }

        public void flipToSearchMode(){
            spinner.setVisibility(View.VISIBLE);
            spinnerEdit.setVisibility(View.GONE);
            editMove.setVisibility(View.GONE);
            view1000.setVisibility(View.GONE);
            view10000.setVisibility(View.VISIBLE);
            editTextForSearch.setVisibility(View.VISIBLE);
            editDelete.setVisibility(View.GONE);
            editCancel.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
          updateJottListViewBasedOffSpinner();
         editTextForSearch.requestFocus();
        }


        public void initiateSearch(View view) {

            //search button has been clicked
            if (!alreadyClicked) {
                flipToSearchMode();


                dbCursorAdapter.setFilterQueryProvider(new FilterQueryProvider() {

                    @Override
                    public Cursor runQuery(CharSequence constraint) {

                        String strItemCode = constraint.toString();
                        return helper.getAllItemInventoryListings(strItemCode);

                    }
                });

                editTextForSearch.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int
                            after) {
                        Log.i("JOSH before", "hello " + s);

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int
                            count) {
                        if (s.toString().equals("")) {

                            // reset listview
                            Log.i("JOSH onchange", "hello " + s);


                        } else {
                            // perform search


                        }

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.i("JOSH after", "hello " + s);
                        filterListView(s.toString());
                        dbCursorAdapter.getFilter().filter(s.toString());
                        dbCursorAdapter.notifyDataSetChanged();

                    }
                });
                alreadyClicked = true;
            }else{
                editTextForSearch.setText("");
                editTextForSearch.setHint("Type Search Here");
                flipToNormalMode();
                alreadyClicked = false;
            }
            }

        public void filterListView(String s) {
            Log.i("JOSH after", "string be " + s);

        }

/*
         public Cursor getAllItemInventoryListings(String strItemCode){

         Cursor c = db.rawQuery("select * from table_of_entries where entry_body LIKE '%" + strItemCode + "%';", null);
         return c;


         }
*/
    }
