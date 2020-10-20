package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public Context context;
    public Button addButton;
    public TextView text;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        recyclerView = findViewById(R.id.recycler);
        addButton = findViewById(R.id.addNewGame);
        context = this;
        text = findViewById(R.id.text);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<GameCard> ar = new ArrayList<>();
        recyclerView.setAdapter(new CardViewAdapter(ar, context, recyclerView));
        LoadDB();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GamesDBHelperActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDB();
    }

    public void LoadDB(){
        GamesDB gamesDB = new GamesDB(this, this);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = gamesDB.getWritableDatabase();
        Cursor c = db.query("games", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int nameColIndex = c.getColumnIndex("name");
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
                String sale = c.getString(saleColIndex);
                String age = c.getString(ageColIndex);
                GameCard card = new GameCard();
                card.name = name;
                card.image = image;
                card.disc = desc;
                card.price = price;
                card.genres = genres;
                cardsList.add(card);
            } while (c.moveToNext());
            CardViewAdapter adapter = new CardViewAdapter(cardsList, context, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }
}