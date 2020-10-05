package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;
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

        LoadDB();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DBHelperActivity.class);
                startActivity(intent);
            }
        });
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



            do {
                String name = c.getString(nameColIndex);
                String price = c.getString(priceColIndex);
                String image = c.getString(imageColIndex);
                String desc = c.getString(descColIndex);
                String genres = c.getString(genColIndex);
                String sale = c.getString(saleColIndex);
                String age = c.getString(ageColIndex);
                // получаем значения по номерам столбцов и пишем все в лог
                text.setText(text.getText().toString() + name + " PRICE " + price + " DESC "+ desc + " GENRES "  + genres + " IMAGE " + image + " SALE "    +  sale + " age = "  + age + "\n");
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
    }
}