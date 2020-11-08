package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class TablePickerActivity extends AppCompatActivity {
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table_picker);
        context = this;
    }

    public void UsersTable(View view){
        Intent intent = new Intent(context, UsersListActivity.class);
        startActivity(intent);
        finish();
    }

    public void TransactionsTable(View view){
        Intent intent = new Intent(context, TransactionsActivity.class);
        startActivity(intent);
        finish();
    }

    public void GenresTable(View view){
        Intent intent = new Intent(context, GenresTableActivity.class);
        startActivity(intent);
        finish();
    }
}