package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.TextView;

import com.example.gameshop.authorization.MainActivity;
import com.sun.mail.imap.protocol.INTERNALDATE;

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

    public void Exit(View view){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("remove", true);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void Filler(){
        DataBase dataBase = new DataBase(context, "users");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where mail = '" + user + "';", null);
        if(!c.moveToFirst()) return;
            name.setText(c.getString(c.getColumnIndex("mail")));
            games.setText(c.getString(c.getColumnIndex("games")));
            birthday.setText(c.getString(c.getColumnIndex("age")));
            DataBase d = new DataBase(context, "transactions");
            SQLiteDatabase db2 = d.getWritableDatabase();
            Cursor cc = db2.rawQuery("select * from transactions;", null);
            cc.moveToFirst();
            int moneySpent = 0;
            int index = cc.getColumnIndex("user");
            int moneyIndex = cc.getColumnIndex("price");
            do{
                if(cc.getString(index).equals(user)) moneySpent += cc.getInt(moneyIndex);
            }while(cc.moveToNext());
            money.setText(String.valueOf(moneySpent) + "$");
            c.close();
            cc.close();
            db.close();
    }
}