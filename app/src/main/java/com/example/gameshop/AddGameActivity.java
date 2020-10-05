package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class AddGameActivity extends AppCompatActivity {
    private EditText name;
    private EditText price;
    private EditText image;
    private CheckBox age;
    private EditText desc;
    private EditText genres;
    private EditText sale;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        name = findViewById(R.id.gameName);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        desc = findViewById(R.id.desc);
        age = findViewById(R.id.ageLimit);
        genres = findViewById(R.id.genres);
        sale = findViewById(R.id.sale);
        save = findViewById(R.id.saveGame);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGame();
            }
        });

    }

    private void AddGame(){
        ContentValues cv = new ContentValues();
        AddGame addGame = new AddGame(this, this);
        SQLiteDatabase db = addGame.getWritableDatabase();
        cv.put("name", name.getText().toString());
        cv.put("price", Integer.parseInt(price.getText().toString()));
        cv.put("image", 0);
        cv.put("description", desc.getText().toString());
        cv.put("genres", genres.getText().toString());
        cv.put("sale", sale.getText().toString());
        cv.put("AgeLimit", age.isChecked());
        db.insert("games", null, cv);
        addGame.close();
        finish();
    }
}