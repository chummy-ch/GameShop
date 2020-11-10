package com.example.gameshop.authorization;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;

import com.example.gameshop.DataBase;
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

    private boolean DBCheck(SQLiteDatabase db, String user, String psw){
        Cursor c = db.rawQuery("select * from users where mail = '" + user + "';", null);
        if(c.getCount() == 0) {
            c.close();
            return false;
        }
        c.moveToFirst();
        if(psw == null) {
            c.close();
            return true;
        }
        int pswIndex = c.getColumnIndex("password");
        boolean ret = c.getString(pswIndex).equals(psw);
        c.close();
        return ret;
    }

    public boolean Logining(String mail, String psw){
        DataBase usersDB = new DataBase(context, "users");
        SQLiteDatabase db = usersDB.getWritableDatabase();
        return DBCheck(db, mail, psw);
    }

    public boolean AddUser(String mail, String psw, String brth){
        ContentValues cv = new ContentValues();
        DataBase usersDB = new DataBase(context, "users");
        SQLiteDatabase db = usersDB.getWritableDatabase();
        if(DBCheck(db, mail, null)){
            db.close();
            return false;
        }
        cv.put("mail", mail);
        cv.put("age", brth);
        cv.put("password", psw);

        db.insert("users", null, cv);
        usersDB.close();
        return true;
    }
}
