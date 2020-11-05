package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private GameCard game ;
    private TextView name, genres, price, desc, age;
    private ImageView img;
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

        Gson gson = new Gson();
        game = gson.fromJson(getIntent().getStringExtra("game"), GameCard.class);
        Filler();
    }

    public void EditGame(View view){
        Intent intent = new Intent(context, AddGameActivity.class);
        Gson json = new Gson();
        String extra = json.toJson(game);
        intent.putExtra("EditGame", extra);
        startActivity(intent);
        finish();
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