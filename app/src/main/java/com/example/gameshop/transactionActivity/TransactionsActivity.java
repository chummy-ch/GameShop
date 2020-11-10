package com.example.gameshop.transactionActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.gameshop.DataBase;
import com.example.gameshop.R;

import java.util.ArrayList;

public class TransactionsActivity extends AppCompatActivity {
    private Context context;
    private RecyclerView rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        context = this;

        rec = findViewById(R.id.transactionsRecView);
        rec.setLayoutManager(new LinearLayoutManager(context));
        rec.setAdapter(new TransactionsAdapter(context, rec, new ArrayList<Transaction>()));

        LoadDB();
    }

    private void LoadDB(){
        DataBase transactionsDB = new DataBase(context, "transactions");
        SQLiteDatabase db = transactionsDB.getWritableDatabase();
        Cursor c = db.rawQuery("select * from transactions;", null);

        if(!c.moveToFirst()) return;
        int idIndex = c.getColumnIndex("id");
        int gameIndex = c.getColumnIndex("game");
        int userIndex = c.getColumnIndex("user");
        int priceIndex = c.getColumnIndex("price");
        int dateIndex = c.getColumnIndex("date");
        ArrayList<Transaction> trans = new ArrayList<>();
        do{
            Transaction t = new Transaction();
            t.id = c.getInt(idIndex);
            t.game = c.getString(gameIndex);
            t.user = c.getString(userIndex);
            t.price = c.getString(priceIndex);
            t.date = c.getString(dateIndex);
            trans.add(t);
        }
        while(c.moveToNext());
        c.close();
        db.close();
        TransactionsAdapter adapter = new TransactionsAdapter(context,rec, trans);
        rec.setAdapter(adapter);
    }
}