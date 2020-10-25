package com.example.gameshop;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UsersDB extends SQLiteOpenHelper {

    public UsersDB(Context context, Activity activity){
        super(context, "Users", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table users ("
                        + "mail text PRIMARY KEY,"
                        + "games text,"
                        + "admin int,"
                        + "age text,"
                        + "password text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
