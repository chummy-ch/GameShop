package com.example.gameshop.tables;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import com.example.gameshop.R;
import com.example.gameshop.genrelActivity.GenreAdapter;
import com.example.gameshop.genrelActivity.Genres;

import java.util.ArrayList;

public class GenresTableActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genres_table);
        context = this;
        recyclerView = findViewById(R.id.recViewGenre);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<String> ar = new ArrayList<>();
        recyclerView.setAdapter(new GenreAdapter(context, ar, recyclerView));

        Genres gens = new Genres(context);
        ar = gens.GetGenres();
        if(ar != null){
            GenreAdapter adapter = new GenreAdapter(context, ar, recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }
}