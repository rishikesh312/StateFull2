package com.example.statefull;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql1);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME1 + ";";
        sqLiteDatabase.execSQL(sql);
        sql = "DROP TABLE IF EXISTS "+TABLE_NAME2+";";
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
    boolean isUser(String name,String pass) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor =  db.rawQuery("SELECT * FROM " + TABLE_NAME1, null);


        if(cursor.moveToFirst()){
            do{

                String user_ = cursor.getString(1);
                String pass_ = cursor.getString(2);
                if (user_.equals(name) && pass_.equals(pass)) {
                    cursor.close();
                    return true;
                }
            }while(cursor.moveToNext());
        }
        return false;
    }
}