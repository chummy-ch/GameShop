package com.example.gameshop;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class SingIN {
    private Context context;
    private Activity activity;

    public SingIN(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }

    private boolean Check(SQLiteDatabase db, String key, String column){
        Cursor c = db.query("users", null, null, null, null, null, null);

        if(!c.moveToFirst()) return false;
        int columnIndex = c.getColumnIndex(column);
        do{
            if(c.getString(columnIndex).equals(key)) return true;
        }while (c.moveToNext());
        return false;
    }

    public boolean Logining(String mail, String psw){
        UsersDB usersDB = new UsersDB(context, activity);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        return Check(db, mail, "mail") && Check(db, psw, "password");
    }

    public boolean AddUser(String mail, String psw, String brth){
        ContentValues cv = new ContentValues();
        UsersDB usersDB = new UsersDB(context, activity);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        if(Check(db, mail, "mail")) return false;

        cv.put("mail", mail);
        cv.put("age", brth);
        cv.put("password", psw);

        db.insert("users", null, cv);
        usersDB.close();
        return true;
    }
}
