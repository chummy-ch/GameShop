package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
                Intent intent = new Intent(context, AddGameActivity.class);
                startActivity(intent);
            }
        });
    }

    public void LoadDB(){
        String LOG_TAG = "";
        AddGame addGame = new AddGame(this, this);
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = addGame.getWritableDatabase();
        Cursor c = db.query("games", null, null, null, null, null, null);
        if (c.moveToFirst()) {

            // определяем номера столбцов по имени в выборке
            int idColIndex = c.getColumnIndex("name");
            int nameColIndex = c.getColumnIndex("price");



            do {
                String name = c.getString(idColIndex);
                String price = c.getString(nameColIndex);
                // получаем значения по номерам столбцов и пишем все в лог
                Log.d(LOG_TAG,
                        "ID = " + c.getString(idColIndex) +
                                ", name = " + c.getString(nameColIndex));

                text.setText(text.getText().toString() + name + " = " + price + "\n");
                // переход на следующую строку
                // а если следующей нет (текущая - последняя), то false - выходим из цикла
            } while (c.moveToNext());
        }
    }
}