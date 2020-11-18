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
import com.example.gameshop.shopActivity.GameCard;
import com.google.gson.Gson;
import com.sun.mail.imap.protocol.INTERNALDATE;

import java.io.File;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.DuplicateFormatFlagsException;
import java.util.Locale;

public class GameActivity extends AppCompatActivity {
    private GameCard game ;
    private TextView name, genres, price, desc, age, sale;
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
        sale = findViewById(R.id.sale);
        desc = findViewById(R.id.desc);
        img = findViewById(R.id.img);
        context = this;

        user = getIntent().getStringExtra("user");
        Gson gson = new Gson();
        game = gson.fromJson(getIntent().getStringExtra("game"), GameCard.class);
        Filler();
        AdminCheck();
        HasGame();
    }

    private void AdminCheck(){
        DataBase dataBase = new DataBase(context, "users");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select admin from users where mail = '" + user + "';", null);
        c.moveToFirst();
        if(c.getInt(0) == 0) edit.setVisibility(View.GONE);
        c.close();
        db.close();
    }

    public void EditGame(View view){
        Intent intent = new Intent(context, AddGameActivity.class);
        Gson json = new Gson();
        String extra = json.toJson(game);
        intent.putExtra("EditGame", extra);
        startActivity(intent);
        finish();
    }

    private int CheckAge(String age){
        String current = new SimpleDateFormat("dd/MM/YYYY").format(new Date());
        int totalDays = 0;
        int f = Integer.parseInt(current.substring(0, 2));
        int s = Integer.parseInt(age.substring(0, 2));
        if(f > s) totalDays += f - s;
        else totalDays += s -f;
        f = Integer.parseInt(current.substring(3, 5));
        s = Integer.parseInt(age.substring(3, 5));
        if(f > s) totalDays += (f - s) * 30;
        else totalDays += (s - f) * 30;
        f = Integer.parseInt(current.substring(6, 10));
        s = Integer.parseInt(age.substring(6, 10));
        if(f > s) totalDays += (f - s) * 365;
        else totalDays += (s - f) * 365;
        return totalDays / 365;
    }

    public void BuyGame(View view){
        DataBase usersDB = new DataBase(this, "users");
        SQLiteDatabase db = usersDB.getWritableDatabase();
        Cursor c = db.rawQuery("select * from users where mail = '" + user + "';", null);
        if(!c.moveToFirst()){
            Toast.makeText(context, "Null", Toast.LENGTH_SHORT).show();
            return;
        }

        if(CheckAge(c.getString(c.getColumnIndex("age"))) < game.ageLimit) {
            Toast.makeText(context, "Sorry, but you are under " + game.ageLimit, Toast.LENGTH_LONG).show();
            return ;
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
            newGames = games + ", " + name.getText().toString();
        }
        else newGames = name.getText().toString();
        db.execSQL("update users set games = '" + newGames + "' where mail = '" + user + "';");
        Toast.makeText(context, "The game is bought", Toast.LENGTH_LONG).show();
        c.close();
        DataBase transactionsDB = new DataBase(context, "transactions");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyy; HH:mm", Locale.getDefault());
        String date = sdf.format(new Date());
        db = transactionsDB.getWritableDatabase();
        db.execSQL("insert into transactions (game, user, price, date) values ('" + game.name + "','" + user + "','" + game.price
                            + "','" + date + "');");
        db.close();
        price.setTextColor(Color.GRAY);
        Button buy = findViewById(R.id.buyButton);
        buy.setText("Bought");
    }

    private void HasGame(){
        DataBase usersDB = new DataBase(this, "users");
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
        price.setText(String.valueOf(game.price) + "$");
        desc.setText(game.disc);
        File folder = context.getExternalFilesDir("images");
        if(game.image == null) return;
        String img = game.image.substring(game.image.lastIndexOf('/') + 1);
        File imageFile = new File(folder.getPath() + "/" + img);
        if(imageFile.exists()){
            Glide.with(context).load(imageFile).into(this.img);
        }
        if(game.sale > 0) sale.setText("- " + game.sale + "$");
    }
}