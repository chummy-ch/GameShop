package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.zip.CheckedOutputStream;

public class PickAddingActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_adding);
        context = this;

    }

    public void AddGame(View view){
        Intent intent = new Intent(context, GamesDBHelperActivity.class);
        startActivity(intent);
        finish();
    }

    public void AddGenre(View view){
        Intent intent = new Intent(context, GenreActivity.class);
        startActivity(intent);
        finish();
    }
}