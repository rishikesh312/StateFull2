package com.example.statefull;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Master";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME1 = "User";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_USERNAME = "name";
    private static final String COLUMN_PASSWORD="password";
    private static final String COLUMN_EMAIL = "email";

    private static final String TABLE_NAME2="Data";
    private static final String COLUMN_USERID = "Userid";
    private static final String COLUMN_TIME="time";
    private static final String COLUMN_VALUE="mood";

    private static final String TABLE_NAME3 = "Variables";
    private static final String COLUMN_LOGGED = "logged";
    private static final String COLUMN_LOG_TIME = "log_time";
    private static final String COLUMN_LAST_LOG = "last_log";

    private static final String REMINDER_TABLE = "Reminder";
    private static final String COLUMN_REMINDER_TIME = "time";
    private static final String COLUMN_MERIDIAN = "meridian";
    private static final String COLUMN_ISACTIVE = "active";



    static DatabaseManager databaseManager = null;

    DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        databaseManager = this;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String sql = String.format("CREATE TABLE %s (\n    %s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n    %s varchar(200) NOT NULL,\n    %s varchar(200) NOT NULL,\n    %s varchar(200) NOT NULL\n);",
                TABLE_NAME1, COLUMN_ID, COLUMN_USERNAME, COLUMN_PASSWORD, COLUMN_EMAIL);

        String sql1 = String.format("CREATE TABLE %s(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n %sInteger, %sDATE, %sINTEGER);",
                TABLE_NAME2, COLUMN_USERID, COLUMN_TIME, COLUMN_VALUE);

        String sql2 = String.format("CREATE TABLE %s(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s INTEGER, %s Boolean, %s DATE, %s DATE);",
                TABLE_NAME3, COLUMN_USERID, COLUMN_LOGGED, COLUMN_LOG_TIME, COLUMN_LAST_LOG);

        String sql3 = String.format("CREATE TABLE %s(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s Text,  %s Text,  %s Integer);",
                REMINDER_TABLE, COLUMN_REMINDER_TIME, COLUMN_MERIDIAN, COLUMN_ISACTIVE);

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);
        sqLiteDatabase.execSQL(sql3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME1);
        sqLiteDatabase.execSQL(sql);
        sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME2);
        sqLiteDatabase.execSQL(sql);
        sql = String.format("DROP TABLE IF EXISTS %s;", TABLE_NAME3);
        sqLiteDatabase.execSQL(sql);
        onCreate(sqLiteDatabase);
    }

    boolean addUser(String name, String password, String mail) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_USERNAME,name);
        contentValues.put(COLUMN_PASSWORD,password);
        contentValues.put(COLUMN_EMAIL, mail);
        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME1, null, contentValues) != -1;
    }

    boolean userExist(String name) {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME1, null);
        if (cursor.moveToFirst()) {
            do {
                String x = cursor.getString(1);
                if (name.equals(x)) {
                    cursor.close();
                    return true;
                }
            } while (cursor.moveToNext());
        }
        return false;
    }


    void loginSuccess(int userid) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, 1);
        contentValues.put(COLUMN_USERID, userid);
        contentValues.put(COLUMN_LOGGED, true);
        Cursor cursor = this.getWritableDatabase().rawQuery("Select * from " + TABLE_NAME3, null);
        if (cursor.getCount() == 1) {
            this.getWritableDatabase().update(TABLE_NAME3, contentValues, COLUMN_ID + "=?", new String[]{"1"});
            Log.d("Updated", "loginSuccess");
        } else if (cursor.getCount() == 0) {
            this.getWritableDatabase().insert(TABLE_NAME3, null, contentValues);
            Log.d("Inserted ", "loginSuccess");
        }
        cursor.close();
    }

    void logout() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, 1);
        contentValues.put(COLUMN_LOGGED, false);
        this.getWritableDatabase().update(TABLE_NAME3, contentValues, COLUMN_ID + "=?", new String[]{"1"});
    }

    boolean logged() {
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_NAME3, null);
        if (cursor.moveToFirst()) {
            String x = cursor.getString(cursor.getColumnIndex(COLUMN_LOGGED));
            Log.d("Log value: ", x + "00");
            cursor.close();
            if (x.equals("1")) {
                Log.d("returning ", "true");
                return true;
            } else {
                Log.d("returning", "false");
                return false;
            }
        } else {
            Log.d("returning ", "out else");
            return false;
        }
    }

    int isUser(String name, String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_NAME1, null);
        if(cursor.moveToFirst()){
            do{
                String mail_ = cursor.getString(3);
                String pass_ = cursor.getString(2);
                if (mail_.equals(name) && pass_.equals(pass)) {
                    int id = Integer.parseInt(cursor.getString(0));
                    cursor.close();
                    return id;
                }
            } while (cursor.moveToNext());
        }
        return -1;
    }

    void fabAddReminder(int h, int m, String merd) {
        ContentValues contentValues = new ContentValues();
        String time = h + ":" + m;
        contentValues.put(COLUMN_REMINDER_TIME, time);
        contentValues.put(COLUMN_MERIDIAN, merd);
        contentValues.put(COLUMN_ISACTIVE, "1");

        getWritableDatabase().insert(REMINDER_TABLE, null, contentValues);
        Cursor g = getReadableDatabase().rawQuery("Select * from " + REMINDER_TABLE, null);
        if (g.moveToFirst()) {
            do {
                Log.d(COLUMN_ISACTIVE, g.getString(g.getColumnIndex(COLUMN_ISACTIVE)));
            } while (g.moveToNext());
        }

    }

    void negatereminder(int p) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("Select " + COLUMN_ISACTIVE + " from " + REMINDER_TABLE + " where _id=" + p, null);
        boolean current = true;
        if (c.moveToFirst()) {
            String x = c.getString(c.getColumnIndex(COLUMN_ISACTIVE));
            Log.d("X=", x);
            current = x.equals("1");
        }
        Log.d("Current2", Boolean.toString(current));
        boolean state = !current;
        SQLiteDatabase dp = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ISACTIVE, state);
        dp.update(REMINDER_TABLE, contentValues, "_id = ?", new String[]{Integer.toString(p)});
    }

    void deletereminder(int p) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(REMINDER_TABLE, "_id = ?", new String[]{Integer.toString(p)});
    }

    int getReminderTableSize() {
        SQLiteDatabase db = getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db, REMINDER_TABLE);
    }

    List<Reminder> getReminders() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + REMINDER_TABLE, null);
        List<Reminder> reminders = new ArrayList<Reminder>();
        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String hm = cursor.getString(cursor.getColumnIndex(COLUMN_REMINDER_TIME));
                String merd = cursor.getString(cursor.getColumnIndex(COLUMN_MERIDIAN));
                boolean isactive = cursor.getString(cursor.getColumnIndex(COLUMN_ISACTIVE)).equals("1");
                Log.d("Logd IsActive", "" + isactive);

                String[] t = hm.split(":");
                int h = Integer.parseInt(t[0]);
                int m = Integer.parseInt(t[1]);
                Reminder temp = new Reminder(h, m, merd, isactive);
                temp._id = id;
                reminders.add(temp);
            }while(cursor.moveToNext());
        }
        return reminders;
    }
}