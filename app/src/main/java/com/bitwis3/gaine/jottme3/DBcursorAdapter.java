package com.bitwis3.gaine.jottme3;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by gaine on 9/2/2017.
 */

public class DBcursorAdapter extends CursorAdapter implements Filterable {
    boolean editMode = false;
    String entryDateText;



    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>(); // array list for store state of each checkbox;

    public static ArrayList<String> stringAL = new ArrayList<>();
    public static ArrayList<Integer> positionAL = new ArrayList<>();

    public DBcursorAdapter(Context context, Cursor cursor, boolean editMode) {
        super(context, cursor,0);
        this.editMode = editMode;

        for (int i = 0; i < cursor.getCount(); i++) { // c.getCount() return total number of your Cursor
            itemChecked.add(i, false); // initializes all items value with false
        }
    }




    // The newView method is used to inflate a new view and return it,
    // you don't bind any data to the view at this point.
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.entry_list_item, parent, false);

    }

    // The bindView method is used to bind all data to a given view
    // such as setting the text on a TextView.
    @Override
    public void bindView(View view, final Context context, Cursor cursor) {

        final int position = cursor.getPosition(); // get position by cursor


        // Find fields to populate in inflated template
        CheckBox checkBox = (CheckBox)view.findViewById(R.id.checkboxForLVitem);
        TextView entryBody = (TextView) view.findViewById(R.id.entry_placeholder);
        TextView entryDate = (TextView) view.findViewById(R.id.entry_date_placeholder);
        TextView notification = (TextView) view.findViewById(R.id.entry_notification_date);




        // Extract properties from cursor
        String entryBodyText = cursor.getString(1);
        stringAL.add(entryBodyText);
        entryDateText = cursor.getString(2);


        // Populate fields with extracted properties

        entryBody.setText(entryBodyText);
        entryDate.setText(entryDateText);
      /*  checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getId();
                dateArrayList.add(new String(entryDateText));
                Log.i("JOSHadapteradded:",entryDateText);
               Log.i("JOSHadapteradded", "id is " + context.g());
            }
        });
*/


        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (itemChecked.get(position) == true) { // if current checkbox is checked, when you click -> change it to false
                    itemChecked.set(position, false);
                    positionAL.removeAll(Arrays.asList(position));
                    Log.i("JOSH CursorAdapter","position: "+position+" - checkbox state: "+itemChecked.get(position));
                } else {
                    itemChecked.set(position, true);
                    positionAL.add(position);
                    Log.i("JOSH CursorAdapter","position: "+position+" - checkbox state: "+itemChecked.get(position));
                }
            }
        });


        checkBox.setChecked(itemChecked.get(position)); // set the checkbox state base on arraylist object state
        Log.i("JOSH CursorAdapter","position: "+position+" - checkbox state: "+itemChecked.get(position));



        if (!editMode){
            checkBox.setVisibility(View.GONE);

        }else{
            checkBox.setVisibility(View.VISIBLE);

        }


        if(cursor.getString(9).equals("yes")){
            String entryNotificationDate = "Notification set for " +(Integer.parseInt(cursor.getString(5))+1)
                    + "/" + cursor.getString(6)
                    + "/" + cursor.getString(4)
                    + " at "
                    + buildTime(cursor.getInt(7), cursor.getInt(8));
        notification.setText(entryNotificationDate);}
        else{
            notification.setVisibility(View.GONE);
        }
    }


    public static ArrayList getArrayList(){

        return positionAL;
    }

    public String buildTime(int hourN, int minuteN) {
        String time = "";
        String hour = "";
        String minute = "";
        String amPm = "";
        if (hourN >= 12) {
            amPm = "PM";
            if (hourN == 12) {
                hour = "12";
            } else {
                int newHour1 = hourN - 12;
                hour = String.valueOf(newHour1);
            }
        } else {
            amPm = "AM";
            hour = String.valueOf(hourN);
        }

        if (minuteN < 10) {
            minute = "0" + minuteN;
        } else {
            minute = String.valueOf(minuteN);
        }

        time = hour + ":" + minute + " " + amPm;


        return time;

    }



}