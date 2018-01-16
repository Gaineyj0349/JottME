package com.bitwis3.gaine.jottme3;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    ArrayList<Entry> entries=null;



  public DBHelper(Context context, String dbName,
                  SQLiteDatabase.CursorFactory factory, int version) {
    super(context, dbName, factory, version);

  }

  @Override
  public void onCreate(SQLiteDatabase db) {
      String createString =
              "CREATE TABLE IF NOT EXISTS table_of_entries (" +
                      "    _id              INTEGER PRIMARY KEY AUTOINCREMENT\n" +
                      "                             NOT NULL," +
                      "    entry_body       STRING  NOT NULL," +
                      "    entry_date       STRING  NOT NULL," +
                      "    folder           STRING  NOT NULL," +
                      "    year             INTEGER," +
                      "    month            INTEGER," +
                      "    day              INTEGER," +
                      "    hour             INTEGER," +
                      "    minute           INTEGER," +
                      "    has_notification STRING  DEFAULT [no]," +
                      "    request_code     INTEGER);";

    String createString2 =
            "CREATE TABLE IF NOT EXISTS table_of_folders "
                    + "( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "folder STRING NOT NULL);";
    Log.d("JOSH", "onCreate called +");

    db.execSQL(createString);
    db.execSQL(createString2);
  }


    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
    String q1 = "ALTER TABLE table_of_entries ADD folder STRING";
        String q2 = "ALTER TABLE table_of_entries ADD year INTEGER";
        String q3 = "ALTER TABLE table_of_entries ADD month INTEGER";
        String q4 = "ALTER TABLE table_of_entries ADD day INTEGER";
        String q5 = "ALTER TABLE table_of_entries ADD hour INTEGER";
        String q6 = "ALTER TABLE table_of_entries ADD minute INTEGER";
        String q7 = "ALTER TABLE table_of_entries ADD has_notification STRING  DEFAULT [no]";
        String q8 = "ALTER TABLE table_of_entries ADD request_code INTEGER";





        db.execSQL(q1);
        db.execSQL(q2);
        db.execSQL(q3);
        db.execSQL(q4);
        db.execSQL(q5);
        db.execSQL(q6);
        db.execSQL(q7);
        db.execSQL(q8);

        Log.d("JOSH", "onUpgrade called +" +oldVersion + " " + newVersion);
        onCreate(db);
    }
/*
  @Override
public void onUpgrade(SQLiteDatabase db,
      int oldVersion, int newVersion) {

      buildArrayList();

    String dropString =
            "CREATE TABLE table_of_entries2 (" +
        "    _id              INTEGER PRIMARY KEY AUTOINCREMENT\n" +
        "                             NOT NULL," +
        "    entry_body       STRING  NOT NULL," +
        "    entry_date       STRING  NOT NULL," +
        "    folder           STRING  NOT NULL," +
        "    year             INTEGER," +
        "    month            INTEGER," +
        "    day              INTEGER," +
        "    hour             INTEGER," +
        "    minute           INTEGER," +
        "    has_notification STRING  DEFAULT [no]," +
        "    request_code     INTEGER" +
        ");" +

        "INSERT INTO table_of_entries2 ("+

        "                                  entry_body," +
        "                                  entry_date" +
        "                              )" +

        "                                     entry_body," +
        "                                     entry_date" +
        "                                FROM table_of_entries;" +

        "DROP TABLE table_of_entries;";




    db.execSQL(dropString);

    Log.d("JOSH", "onUpgrade called +" +oldVersion + " " + newVersion);
    onCreate(db);
    refillTable();
  }
 /*
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    String alter_query1="alter table table_of_entries RENAME TO temp;";
    String alter_query2=

                    "CREATE TABLE table_of_entries2" +
                    "      ( _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "                      entry_body STRING NOT NULL, " +
                    "                       entry_date STRING NOT NULL," +
                    "                       folder STRING NOT NULL,"
                    + "year INTEGER NOT NULL DEFAULT 0, "
                    + "day INTEGER DEFAULT 0,"
                    +"month INTEGER DEFAULT 0,"
                    + "hour INTEGER DEFAULT 0, "
                    + "minute INTEGER DEFAULT 0,"+
                    " has_notification STRING DEFAULT no," +
                    " request_code INTEGER NOT NULL DEFAULT -1);";
    String alter_query3="insert into table_of_entries2 select *, 0 from temp;";
    String alter_query4="DROP TABLE temp;";

    db.execSQL(alter_query1);
    db.execSQL(alter_query2);
   db.execSQL(alter_query3);
    db.execSQL(alter_query4);
  }






 // public Cursor getAllItemInventoryListings(String strItemCode){

   // Cursor c = db.rawQuery("select * from table_of_entries2 where entry_body LIKE '%" + strItemCode + "%';", null);
   // return c;


 // }

  public void buildArrayList(){



    entries = new ArrayList<Entry>();
    Entry entry = new Entry();
       db =  this.getWritableDatabase();
    Cursor c = db.rawQuery("select * from table_of_entries", null);
    if (c != null && c.moveToFirst()){
        do{
            entry.clear();
            entry.setEntryBody(c.getString(1));
            Log.i("JOSH BODY:" , c.getString(1));

            entry.setEntryDate(c.getString(2));
            Log.i("JOSH DATE:" , c.getString(1));
            entries.add(entry);
        }while(c.moveToNext());


      }
  }

  public void refillTable(){

     for(Entry entry : entries){
         ContentValues values = new ContentValues();
         values.put("entry_body", entry.getEntryBody());
         values.put("entry_date", entry.getEntryDate());
         db.insert("table_of_entries2",null,values);
         values.clear();
     }
      Log.i("JOSH success here", "WHOO HOOO");

  }

  */
  public Cursor getAllItemInventoryListings(String strItemCode){
      SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("select * from table_of_entries where entry_body LIKE '%" + strItemCode + "%';", null);
         return c;


         }
}
