package com.example.gameshop.genrelActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.ScriptIntrinsicYuvToRGB;
import android.widget.Toast;

import com.example.gameshop.DataBase;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Genres {
    private final Context context;

    public Genres(Context context){
        this.context = context;
    }

    public ArrayList<String> GetGenres(){
        ArrayList<String> gen = new ArrayList<>();
        DataBase dataBase = new DataBase(context, "genres");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from genres;", null);
        if(!c.moveToFirst()) return null;
        do{
            gen.add(c.getString(1));
        }while(c.moveToNext());
        return gen;
    }

    public void AddGenre(String genre){
        DataBase dataBase = new DataBase(context, "genres");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from genres where genre = '" + genre + "';", null);
        if(c.moveToFirst()) {
            db.close();
            c.close();
            return;
        }
        db.execSQL("insert into genres (genre) values ('" + genre + "')");
        db.close();
        c.close();
    }

    public void RemoveGenre(String genre){
        DataBase database = new DataBase(context, "genres");
        SQLiteDatabase db = database.getWritableDatabase();
        Cursor c = db.rawQuery("select * from genres where genre = '" + genre + "';", null);
        if(c.moveToFirst()){
            c.close();
            db.close();
            return;
        }
        db.execSQL("delete from genre where genre = '" + genre + "';");
        db.close();
    }
}
