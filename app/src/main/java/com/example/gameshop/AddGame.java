package com.example.gameshop;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;

public class AddGame extends SQLiteOpenHelper {
    private Activity activity;
    private EditText name;
    private EditText price;
    private EditText image;
    private EditText desc;
    private EditText genres;
    private EditText sale;
    private Context context;

    public AddGame(Context context, Activity activity){
        super(context, "GameList", null, 1);
        this.activity = activity;

    }

    public void  AddGame(){

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table games ("
                + "name text,"
                + "image int,"
                + "price int,"
                + "descriprion text,"
                + "geres text,"
                + "Onsale binary,"
                + "sale int,"
                + "AgeLimit binary"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
