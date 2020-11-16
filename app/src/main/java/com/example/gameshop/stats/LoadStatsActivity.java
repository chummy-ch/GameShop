package com.example.gameshop.stats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
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

    private void MakeTable(Cursor c){
        if(!c.moveToFirst()) return;
        Display d = getWindowManager().getDefaultDisplay();
        parent.setDividerDrawable(getDrawable(R.color.borders));
        parent.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        LinearLayout namesLinear = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        namesLinear.setLayoutParams(params);
        namesLinear.setOrientation(LinearLayout.HORIZONTAL);
        namesLinear.setId(parent.getChildCount());
        namesLinear.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        parent.addView(namesLinear);
        Point size = new Point();
        d.getSize(size);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(size.x / c.getColumnCount() , ViewGroup.LayoutParams.WRAP_CONTENT);
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
        }
    }

    private void LoadViews(){
        DataBase dataBase = new DataBase(context, "views");
        SQLiteDatabase db = dataBase.getWritableDatabase();
        Cursor c = db.rawQuery("select * from views;", null);
        MakeTable(c);
        db.close();
    }
}