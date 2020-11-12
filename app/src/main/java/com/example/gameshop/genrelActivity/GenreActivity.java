package com.example.gameshop.genrelActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bumptech.glide.util.ByteBufferUtil;
import com.example.gameshop.DataBase;
import com.example.gameshop.R;

public class GenreActivity extends AppCompatActivity {
    private Context context;
    private EditText gen;
    private String oldGenre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre);
        context = this;
        gen = findViewById(R.id.genrename);
        if(getIntent().hasExtra("edit")){
            Button remove = findViewById(R.id.removeGenre);
            remove.setVisibility(View.VISIBLE);
            oldGenre = getIntent().getStringExtra("edit");
            gen.setText(oldGenre);
        }
    }

    public void AddGen(View view){
        Genres gens = new Genres(context);
        if(oldGenre == null)
        gens.AddGenre(gen.getText().toString().toUpperCase());
        else {
            EditGen();
        }
        finish();
    }

    private void EditGen(){
        Remove(oldGenre);
        Genres genres = new Genres(context);
        genres.AddGenre(gen.getText().toString());
        finish();
    }

    private void Remove(String genre){
        DataBase dataBase = new DataBase(context, "genres");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        db.execSQL("delete from genres where genre = '" + genre + "';");
        db.close();
    }

    public void RemoveGen(View view){
        Remove(gen.getText().toString());
        finish();
    }

}