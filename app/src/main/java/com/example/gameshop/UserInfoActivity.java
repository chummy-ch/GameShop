package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.TextView;

public class UserInfoActivity extends AppCompatActivity {
    private TextView name, games, birthday, money;
    private Context context;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        context = this;
        name = findViewById(R.id.userName);
        games = findViewById(R.id.gamesList);
        birthday = findViewById(R.id.birthday);
        money = findViewById(R.id.moneySpent);
        user = getIntent().getStringExtra("user");

        Filler();
    }

    private void Filler(){
        DataBase dataBase = new DataBase(context, "users");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where mail = '" + user + "';", null);
        if(!c.moveToFirst()) return;
            name.setText(c.getString(c.getColumnIndex("mail")));
            games.setText(c.getString(c.getColumnIndex("games")));
            birthday.setText(c.getString(c.getColumnIndex("age")));
            Cursor cc = db.rawQuery("select * from transactions;", null);
            cc.moveToFirst();
            int moneySpent = 0;
            int index = cc.getColumnIndex("user");
            int moneyIndex = cc.getColumnIndex("price");
            do{
                System.out.println("USer " + user);
                System.out.println(cc.getCount());
                System.out.println(moneyIndex);
                if(cc.getString(index).equals(user)) moneySpent += cc.getInt(moneyIndex);
            }while(cc.moveToNext());
            money.setText(String.valueOf(moneySpent));
            c.close();
            cc.close();
            db.close();
    }
}