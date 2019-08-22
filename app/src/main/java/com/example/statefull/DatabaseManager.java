package com.example.statefull;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    static DatabaseManager databaseManager = null;

    DatabaseManager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        databaseManager = this;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = "CREATE TABLE " + TABLE_NAME1 + " (\n" +
                "    " + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n" +
                "    " + COLUMN_USERNAME + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_PASSWORD + " varchar(200) NOT NULL,\n" +
                "    " + COLUMN_EMAIL + " varchar(200) NOT NULL\n" +
                ");";

        String sql1 = "CREATE TABLE "+ TABLE_NAME2+"(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,\n"+
                " " + COLUMN_USERID + "Integer,"+
                " " + COLUMN_TIME   + "DATE,"   +
                " " + COLUMN_VALUE  + "INTEGER);";

        String sql2 = "CREATE TABLE " + TABLE_NAME3 + "(_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " " + COLUMN_LOGGED + " Boolean," +
                " " + COLUMN_LOG_TIME + " DATE," +
                " " + COLUMN_LAST_LOG + " DATE);";

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);
        sqLiteDatabase.execSQL(sql2);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME1 + ";";
        sqLiteDatabase.execSQL(sql);
        sql = "DROP TABLE IF EXISTS "+TABLE_NAME2+";";
        sqLiteDatabase.execSQL(sql);
        sql = "DROP TABLE IF EXISTS " + TABLE_NAME3 + ";";
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

    void loginSuccess() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, 1);
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
    boolean isUser(String name,String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_NAME1, null);
        if(cursor.moveToFirst()){
            do{

                String mail_ = cursor.getString(3);
                String pass_ = cursor.getString(2);
                if (mail_.equals(name) && pass_.equals(pass)) {
                    cursor.close();
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }
}