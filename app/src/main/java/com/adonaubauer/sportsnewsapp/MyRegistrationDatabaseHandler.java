package com.adonaubauer.sportsnewsapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Austin on 5/4/2017.
 */

public class MyRegistrationDatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "UserRegistrationDatabase.db";
    private static final String TABLE_USERS   = "users";

    private static final String COLUMN_ID     = "_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    MyRegistrationDatabaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {

        String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_USERNAME +
                " TEXT," + COLUMN_PASSWORD + " TEXT)";

        database.execSQL(CREATE_USERS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);

        onCreate(database);

    }

    void addUser(User user) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_ID, user.getID());

        contentValues.put(COLUMN_USERNAME, user.getUsername());

        contentValues.put(COLUMN_PASSWORD, user.getPassword());

        SQLiteDatabase database = this.getWritableDatabase();

        database.insert(TABLE_USERS, null, contentValues);

        database.close();

    }

    User findUser(String username, String password) {

        String queryUsersDatabase = "Select * FROM " + TABLE_USERS + " WHERE "
                + COLUMN_USERNAME + " = \"" + username + "\"";

        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.rawQuery(queryUsersDatabase, null);

        User user = new User();

        if (cursor.moveToFirst()) {

            cursor.moveToFirst();

            user.setID(Integer.parseInt(cursor.getString(0)));

            user.setUsername(cursor.getString(1));

            user.setPassword(cursor.getString(2));

            cursor.close();

        } else {

            user = null;

        }

        database.close();

        return user;

    }

}
