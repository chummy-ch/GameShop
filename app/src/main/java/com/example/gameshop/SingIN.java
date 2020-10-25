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

    public void Logining(String mail, String psw){
        ContentValues cv = new ContentValues();
        UsersDB usersDB = new UsersDB(context, activity);
        SQLiteDatabase db = usersDB.getWritableDatabase();

        if(cv.containsKey(mail)) System.out.println(cv.getAsString(mail));
        usersDB.close();
    }

    public void AddUser(String mail, String psw, String brth){
        ContentValues cv = new ContentValues();
        UsersDB usersDB = new UsersDB(context, activity);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        Cursor c = db.query("users", null, null, null, null, null, null);

        cv.put("mail", mail);
        cv.put("age", brth);
        cv.put("password", psw);

        db.insert("users", null, cv);
        usersDB.close();
    }
}
