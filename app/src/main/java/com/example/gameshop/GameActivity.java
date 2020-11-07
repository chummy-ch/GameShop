package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameActivity extends AppCompatActivity {
    private GameCard game ;
    private TextView name, genres, price, desc, age;
    private ImageView img;
    private String user;
    private Button edit;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        name = findViewById(R.id.game);
        genres = findViewById(R.id.genres);
        price = findViewById(R.id.price);
        age = findViewById(R.id.age);
        edit = findViewById(R.id.edit);
        desc = findViewById(R.id.desc);
        img = findViewById(R.id.img);
        context = this;

        user = getIntent().getStringExtra("user");
        Gson gson = new Gson();
        game = gson.fromJson(getIntent().getStringExtra("game"), GameCard.class);
        Filler();

        HasGame();
    }

    public void EditGame(View view){
        Intent intent = new Intent(context, AddGameActivity.class);
        Gson json = new Gson();
        String extra = json.toJson(game);
        intent.putExtra("EditGame", extra);
        startActivity(intent);
        finish();
    }

    public void BuyGame(View view){
        UsersDB usersDB = new UsersDB(this, this);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where mail = '" + user + "';", null);
        if(!c.moveToFirst()){
            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();
            return;
        }
        int gameColumn = c.getColumnIndex("games");
        String games = c.getString(gameColumn);
        if(games != null && games.contains(name.getText().toString())){
            db.close();
            Toast.makeText(context, "You have already this game", Toast.LENGTH_SHORT).show();
            return;
        }
        String newGames;
        if(games != null && games.length() > 1){
            List<String> gamesList = Arrays.asList(games.split(","));
            gamesList.add(name.getText().toString());
             newGames = gamesList.toString().replace("[", "").replace("]", "");
        }
        else newGames = name.getText().toString();
        db.execSQL("update users set games = '" + newGames + "' where mail = '" + user + "';");
        db.close();
        c.close();
        Toast.makeText(context, "The game is bought", Toast.LENGTH_LONG).show();
    }

    private void HasGame(){
        UsersDB usersDB = new UsersDB(this, this);
        SQLiteDatabase db = usersDB.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where mail = '" + user + "';", null);
        c.moveToFirst();
        int gameColumn = c.getColumnIndex("games");
        String games = c.getString(gameColumn);
        if(games != null && games.contains(name.getText().toString())){
           Button buy = findViewById(R.id.buyButton);
           buy.setText("Bought");
           price.setTextColor(Color.GRAY);
        }
        c.close();
        db.close();
    }

    private void Filler(){
        name.setText(game.name);
        age.setText(String.valueOf(game.ageLimit));
        String g = Arrays.toString(game.genres).replace("[", "").replace("]", "");
        genres.setText(g.replaceAll(",", " "));
        price.setText(String.valueOf(game.price - game.sale) + "$");
        desc.setText(game.disc);
        File folder = context.getExternalFilesDir("images");
        String img = game.image.substring(game.image.lastIndexOf('/') + 1);
        File imageFile = new File(folder.getPath() + "/" + img);
        if(imageFile.exists()){
            Glide.with(context).load(imageFile).into(this.img);
        }
    }
}