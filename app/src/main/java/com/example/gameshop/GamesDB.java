package com.example.gameshop;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GamesDB extends SQLiteOpenHelper {

    public GamesDB(Context context, Activity activity){
        super(context, "GameList", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table games ("
                + "name text,"
                + "image text,"
                + "price int,"
                + "description text,"
                + "genres text,"
                + "sale int,"
                + "AgeLimit binary"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
