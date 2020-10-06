package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ShopActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public Context context;
    public Button addButton;
    public TextView text;

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
        recyclerView.setAdapter(new CardViewAdapter(ar, context));
        LoadDB();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DBHelperActivity.class);
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
        AddGame addGame = new AddGame(this, this);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = addGame.getWritableDatabase();
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
                card.disc = desc;
                card.price = price;
                card.genres = genres;
                cardsList.add(card);
                // получаем значения по номерам столбцов и пишем все в лог
/*
                text.setText(text.getText().toString() + name + " PRICE " + price + " DESC "+ desc + " GENRES "  + genres + " IMAGE " + image + " SALE "    +  sale + " age = "  + age + "\n");
*/
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
            System.out.println(cardsList.size());
            CardViewAdapter adapter = new CardViewAdapter(cardsList, context);
            recyclerView.setAdapter(adapter);
        }
    }

    public void RecLoad(){
    }
}