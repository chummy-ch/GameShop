package com.example.gameshop.shopActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.gameshop.DataBase;
import com.example.gameshop.PickAddingActivity;
import com.example.gameshop.R;
import com.example.gameshop.SqlCodeActivity;
import com.example.gameshop.tables.TablePickerActivity;
import com.example.gameshop.UserInfoActivity;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public Context context;
    public Button users;
    private String user;
    public ImageButton addButton;
    public TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        user = getIntent().getStringExtra("user");
        recyclerView = findViewById(R.id.recycler);
        addButton = findViewById(R.id.addNewGame);
        context = this;
        users = findViewById(R.id.usersList);
        text = findViewById(R.id.text);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<GameCard> ar = new ArrayList<>();
        recyclerView.setAdapter(new CardViewAdapter(ar, context, recyclerView, getIntent().getStringExtra("user")));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PickAddingActivity.class);
                startActivity(intent);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TablePickerActivity.class);
                startActivity(intent);
            }
        });

        Button sql = findViewById(R.id.sqlcode);
        sql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SqlCodeActivity.class);
                startActivity(intent);
            }
        });

    }

    public void Profile(View view){
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDB();
    }

    public void LoadDB(){
        DataBase gamesDB = new DataBase(this, "games");
        SQLiteDatabase db = gamesDB.getWritableDatabase();
        Cursor c = db.rawQuery("select * from games;", null);
/*
        Cursor c = db.query("games", null, null, null, null, null, null);
*/
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int nameColIndex = c.getColumnIndex("game");
            int priceColIndex = c.getColumnIndex("price");
            int imageColIndex = c.getColumnIndex("image");
            int descColIndex = c.getColumnIndex("description");
            int genColIndex = c.getColumnIndex("genres");
            int saleColIndex = c.getColumnIndex("sale");
            int ageColIndex = c.getColumnIndex("AgeLimit");

            ArrayList<GameCard> cardsList = new ArrayList<>();

            do {
                String name = c.getString(nameColIndex);
                int price = c.getInt(priceColIndex);
                String image = c.getString(imageColIndex);
                String desc = c.getString(descColIndex);
                String genres = c.getString(genColIndex);
                int sale = c.getInt(saleColIndex);
                int age = c.getInt(ageColIndex);
                GameCard card = new GameCard();{}
                card.ageLimit = age;
                card.sale = sale;
                card.name = name;
                card.image = image;
                card.disc = desc;
                card.price = price;
                card.genres = genres.split(",");
                cardsList.add(card);
            } while (c.moveToNext());
            c.close();
            db.close();
            CardViewAdapter adapter = new CardViewAdapter(cardsList, context, recyclerView, getIntent().getStringExtra("user"));
            recyclerView.setAdapter(adapter);
        }
    }
}