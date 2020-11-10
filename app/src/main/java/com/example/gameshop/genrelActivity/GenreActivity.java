package com.example.gameshop.genrelActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.gameshop.R;

public class GenreActivity extends AppCompatActivity {
    private Context context;
    private EditText gen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        context = this;
        gen = findViewById(R.id.genrename);
    }

    public void AddGen(View view){
        Genres gens = new Genres(context);
        gens.AddGenre(gen.getText().toString().toUpperCase());
        finish();
    }
}