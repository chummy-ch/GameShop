    package com.example.gameshop.shopActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gameshop.DataBase;
import com.example.gameshop.PickAddingActivity;
import com.example.gameshop.R;
import com.example.gameshop.SqlCodeActivity;
import com.example.gameshop.tables.TablePickerActivity;
import com.example.gameshop.UserInfoActivity;

import java.io.File;
import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {
    public RecyclerView recyclerView;
    public Context context;
    public Button users;
    private String user;
    public ImageButton addButton;
    private int priceSort = 0;
    private int recSort = 0;
    public TextView text;
    private EditText searchField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        user = getIntent().getStringExtra("user");
        recyclerView = findViewById(R.id.recycler);
        addButton = findViewById(R.id.addNewGame);
        context = this;
        searchField = findViewById(R.id.gameSearch);
        users = findViewById(R.id.usersList);
        text = findViewById(R.id.text);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        ArrayList<GameCard> ar = new ArrayList<>();
        recyclerView.setAdapter(new CardViewAdapter(ar, context, recyclerView, getIntent().getStringExtra("user")));

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PickAddingActivity.class);
                startActivity(intent);
            }
        });

        users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TablePickerActivity.class);
                startActivity(intent);
            }
        });

        Button sql = findViewById(R.id.sqlcode);
        sql.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SqlCodeActivity.class);
                startActivity(intent);
            }
        });

        View.OnKeyListener enterPress = new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)){
                    Search();
                    return true;
                }
                return false;
            }
        };
        searchField.setOnKeyListener(enterPress);
        AdminCheck();
    }

    private void AdminCheck(){
        DataBase dataBase = new DataBase(context, "users");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select admin from users where mail = '" + user + "';", null);
        c.moveToFirst();
        if(c.getInt(0) == 0){
            LinearLayout ll = findViewById(R.id.adminPanel);
            ll.setVisibility(View.GONE);
        }
        db.close();
        c.close();
    }

    private void Search(){
        DataBase dataBase = new DataBase(context, "games");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from games where game like '%" + searchField.getText().toString() + "%';", null);
        if(c.getCount() != 0)
        CursorToRecycler(c);
        else {
            c = db.rawQuery("select * from games where genres like '%" + searchField.getText().toString().toUpperCase() + "%';", null);
            CursorToRecycler(c);
        }
        db.close();
    }

    public void SortByPrice(View view){
        DataBase database = new DataBase(context, "games");
        SQLiteDatabase db = database.getWritableDatabase();
        String order;
        if(priceSort == 0){
            order = "asc";
            priceSort = 1;
        }
        else {
            order = "desc";
            priceSort = 0;
        }
        Cursor c = db.rawQuery("select *, price - sale as total from games order by total " + order + ";", null);
        CursorToRecycler(c);
        db.close();
    }

    public void SortByRecency(View view){
        DataBase dataBase = new DataBase(context, "games");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        String order;
        if(recSort == 0){
            order = "asc";
            recSort = 1;
        }
        else {
            order = "desc";
            recSort = 0;
        }
        Cursor c = db.rawQuery("select * from games order by id " + order + ";", null);
        CursorToRecycler(c);
        db.close();
    }

    public void Profile(View view){
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LoadDB();
    }

    private void CursorToRecycler(Cursor c){
        if (c.moveToFirst()) {
            // определяем номера столбцов по имени в выборке
            int nameColIndex = c.getColumnIndex("game");
            int priceColIndex = c.getColumnIndex("price");
            int imageColIndex = c.getColumnIndex("image");
            int descColIndex = c.getColumnIndex("description");
            int genColIndex = c.getColumnIndex("genres");
            int saleColIndex = c.getColumnIndex("sale");
            int ageColIndex = c.getColumnIndex("AgeLimit");

            ArrayList<GameCard> cardsList = new ArrayList<>();

            do {
                String name = c.getString(nameColIndex);
                int price = c.getInt(priceColIndex);
                String image = c.getString(imageColIndex);
                String desc = c.getString(descColIndex);
                String genres = c.getString(genColIndex);
                int sale = c.getInt(saleColIndex);
                int age = c.getInt(ageColIndex);
                GameCard card = new GameCard();

                card.ageLimit = age;
                card.sale = sale;
                card.name = name;
                card.image = image;
                card.disc = desc;
                card.price = price;
                card.genres = genres.split(",");
                cardsList.add(card);
            } while (c.moveToNext());
            c.close();
            CardViewAdapter adapter = new CardViewAdapter(cardsList, context, recyclerView, getIntent().getStringExtra("user"));
            recyclerView.setAdapter(adapter);
        }
        else {
            recyclerView.setAdapter(null);
        }
    }

    public void LoadDB(){
        DataBase gamesDB = new DataBase(this, "games");
        SQLiteDatabase db = gamesDB.getWritableDatabase();
        Cursor c = db.rawQuery("select * from games;", null);
        CursorToRecycler(c);
        db.close();

    }
}