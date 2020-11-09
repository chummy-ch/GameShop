package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class UsersListActivity extends AppCompatActivity {
    private RecyclerView rec;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);

        context = this;
        rec = findViewById(R.id.usersRecView);

        ArrayList<User> users = new ArrayList<>();
        rec.setLayoutManager(new LinearLayoutManager(context));
        rec.setAdapter(new UsersListAdapter(context, users, rec));

    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDB();
    }

    public void LoadDB(){
        DataBase usersDB = new DataBase(this, "users");
        SQLiteDatabase db = usersDB.getWritableDatabase();
        Cursor c = db.query("users", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            int userIndex = c.getColumnIndex("mail");
            int gamesIndex = c.getColumnIndex("games");
            int adminIndex = c.getColumnIndex("admin");
            int ageIndex = c.getColumnIndex("age");
            int pswIndex = c.getColumnIndex("password");

            ArrayList<User> cardsList = new ArrayList<>();

            do {
                User user = new User();
                user.user = c.getString(userIndex);
                user.games = c.getString(gamesIndex);
                user.admin = c.getInt(adminIndex);
                user.birthday = c.getString(ageIndex);
                user.password = c.getString(pswIndex);
                cardsList.add(user);
            } while (c.moveToNext());
            c.close();
            UsersListAdapter adapter = new UsersListAdapter(context, cardsList, rec);
            rec.setAdapter(adapter);
        }
    }
}