package com.example.gameshop.authorization;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.InputType;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;

import com.example.gameshop.DataBase;
import com.example.gameshop.mailsender.SendMail;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingIN {
    private Context context;
    private Activity activity;
    private AlertDialog.Builder builder;
    private String mail, psw, brth;
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

    public int getRandomNumber() {
        return (int) ((Math.random() * (999999 - 100000)) + 100000);
    }

    public boolean AddUser(String mail, String psw, String brth){
        this.mail = mail;
        this.psw = psw;
        this.brth = brth;
        DataBase usersDB = new DataBase(context, "users");
        SQLiteDatabase db = usersDB.getWritableDatabase();
        if(DBCheck(db, mail, null)){
            db.close();
            return false;
        }
        db.close();

        String code =  String.valueOf(getRandomNumber());
        SendMail send = new SendMail(context, mail, "Secret code", "Please enter this code in the app :" + code, null);
        send.execute();
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra("mail", mail).putExtra("psw", psw).putExtra("brth", brth)
                .putExtra("code", code);
        context.startActivity(intent);
        return true;
    }
}
