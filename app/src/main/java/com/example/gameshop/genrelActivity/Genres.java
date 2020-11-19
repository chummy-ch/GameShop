package com.example.gameshop.genrelActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gameshop.DataBase;

import java.util.ArrayList;

public class Genres {
    private final Context context;

    public Genres(Context context){
        this.context = context;
    }

    public ArrayList<Genre> GetGenres(){
        ArrayList<Genre> gen = new ArrayList<>();
        DataBase dataBase = new DataBase(context, "genres");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from genres;", null);
        if(!c.moveToFirst()) return null;
        do{
            Genre genre = new Genre();
            genre.desc = c.getString(2);
            genre.gen = c.getString(1);
            gen.add(genre);
        }while(c.moveToNext());
        return gen;
    }

    public void AddGenre(String genre, String desc){
        DataBase dataBase = new DataBase(context, "genres");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from genres where genre = '" + genre + "';", null);
        if(c.moveToFirst()) {
            db.close();
            c.close();
            return;
        }
        db.execSQL("insert into genres (genre, description) values ('" + genre + "', '" + desc + "')");
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
