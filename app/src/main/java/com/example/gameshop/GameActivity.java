package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileReader;

public class GameActivity extends AppCompatActivity {
    private GameCard game ;
    private TextView name, genres, price, desc, age;
    private ImageView img;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);
        name = findViewById(R.id.game);
        genres = findViewById(R.id.genres);
        price = findViewById(R.id.price);
        age = findViewById(R.id.age);
        desc = findViewById(R.id.desc);
        img = findViewById(R.id.img);
        context = this;

        Gson gson = new Gson();
        game = gson.fromJson(getIntent().getStringExtra("game"), GameCard.class);
        Filler();
    }

    private void Filler(){
        System.out.println(game.ageLimit);
        name.setText(game.name);
        age.setText(String.valueOf(game.ageLimit));
        genres.setText(game.genres);
        price.setText(String.valueOf(game.price) + "$");
        desc.setText(game.disc);
        File folder = context.getExternalFilesDir("images");
        File imageFile = new File(folder.getPath() + "/" + game.image);
        if(imageFile.exists()){
            Glide.with(context).load(imageFile).into(img);
        }
    }
}