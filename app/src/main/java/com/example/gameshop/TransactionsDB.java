package com.example.gameshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TransactionsDB extends SQLiteOpenHelper {
    private Context context;

    public TransactionsDB(Context context){
        super(context, "Transactions", null, 1);
        this.context = context;}

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table transactions("
                    +"id integer PRIMARY KEY AUTOINCREMENT,"
                    + "game text,"
                    + "user text,"
                    + "price int,"
                    + "date text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

