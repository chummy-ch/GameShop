package com.example.gameshop.stats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.gameshop.R;

public class StatsActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        context = this;
    }

    public void GamesViews(View view){
        Intent intent = new Intent(context, LoadStatsActivity.class);
        intent.putExtra("stat", "views");
        startActivity(intent);
        finish();
    }

    public void MonthSelling(View view){
        Intent intent = new Intent(context, LoadStatsActivity.class);
        intent.putExtra("stat", "selling");
        startActivity(intent);
        finish();
    }
}