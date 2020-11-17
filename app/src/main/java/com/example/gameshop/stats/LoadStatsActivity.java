package com.example.gameshop.stats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameshop.DataBase;
import com.example.gameshop.R;
import com.example.gameshop.authorization.MainActivity;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class LoadStatsActivity extends AppCompatActivity {
    private Context context;
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games_views);
        parent = findViewById(R.id.statsParent);
        context = this;


        Chooser();
    }

    private void MakeTable(Cursor c, int orientation){
        if(!c.moveToFirst()) return;
        parent.setOrientation(orientation);
        Display d = getWindowManager().getDefaultDisplay();
        parent.setDividerDrawable(getDrawable(R.color.borders));
        parent.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);

        Point size = new Point();
        d.getSize(size);
        LinearLayout.LayoutParams textParams;
        LinearLayout.LayoutParams params;
        LinearLayout namesLinear = new LinearLayout(context);
        if(orientation == LinearLayout.VERTICAL){
            textParams = new LinearLayout.LayoutParams(size.x / c.getColumnCount() , ViewGroup.LayoutParams.WRAP_CONTENT);
            params =  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        else {
            textParams = new LinearLayout.LayoutParams(size.x / c.getColumnCount() / 2 , ViewGroup.LayoutParams.WRAP_CONTENT);
            params = new LinearLayout.LayoutParams(size.x / c.getColumnCount(), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        namesLinear.setLayoutParams(params);
        namesLinear.setOrientation(LinearLayout.HORIZONTAL);
        namesLinear.setId(parent.getChildCount());
        namesLinear.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        parent.addView(namesLinear);

        for(int i = 0; i < c.getColumnCount(); i ++){
            TextView name = new TextView(context);
            name.setLayoutParams(textParams);
            name.setId(namesLinear.getChildCount());
            name.setGravity(Gravity.CENTER);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
            name.setText(c.getColumnName(i).toUpperCase());
            namesLinear.addView(name);
        }
        do{
            LinearLayout row = new LinearLayout(context);
            row.setLayoutParams(params);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setDividerDrawable(getDrawable(R.color.borders));
            row.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            row.setId(parent.getChildCount());
            parent.addView(row);
            for(int i = 0; i < c.getColumnCount(); i++){
                TextView view = new TextView(context);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                view.setText(c.getString(i));
                view.setId(row.getChildCount());
                view.setGravity(Gravity.CENTER);
                view.setTextColor(getColor(R.color.borders));
                view.setLayoutParams(textParams);
                row.addView(view);
            }
        }while(c.moveToNext());
        c.close();
    }

    private void Chooser(){
        String stat = getIntent().getStringExtra("stat");
        switch (stat){
            case "views":
                LoadViews();
                break;
            case "selling":
              LoadSelling();
              break;
        }
    }

    private void LoadSelling(){
        DataBase dataBase = new DataBase(context, "transactions");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select substr(date, 4 , 2) as 'month num' , sum(price) as 'total spent $' from transactions group by substr (date, 4, 2);", null );
        MakeTable(c, LinearLayout.VERTICAL);
        db.close();
    }

    private void LoadViews(){
        DataBase dataBase = new DataBase(context, "views");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from views;", null);
        MakeTable(c, LinearLayout.VERTICAL);
        db.close();
    }
}