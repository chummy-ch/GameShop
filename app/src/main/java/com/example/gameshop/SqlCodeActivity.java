package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SqlCodeActivity extends AppCompatActivity {
    private Context context;
    private EditText code;
    private TextView tv;
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_code);
        code = findViewById(R.id.code);
        context = this;
        parent = findViewById(R.id.parentInscroll);
        Display display = getWindowManager().getDefaultDisplay();
        code.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(), display.getHeight() / 8));
        parent.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void Clear(View view){ parent.removeViews(0, parent.getChildCount());}

    public void Exit(View view){finish();}

    public void RunSQL(Cursor c){
        c.moveToFirst();
        if(parent.getChildCount() != 0)
        parent.removeViews(0, parent.getChildCount());
        LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setId(parent.getChildCount());
        l.setShowDividers(LinearLayout.SHOW_DIVIDER_END);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        l.setLayoutParams(params);
        LinearLayout p = findViewById(R.id.buttons);
        parent.addView(l);
        for(int i = 0; i < c.getColumnCount(); i++){
            String text = c.getColumnName(i);
            TextView view = new TextView(context);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams( parent.getWidth() / c.getColumnCount() - 10, p.getHeight());
            params2.setMarginStart(3);
            view.setLayoutParams(params2);
            view.setText(text);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.BLACK);
            l.addView(view);
        }
        do{
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setId(parent.getChildCount());
            ll.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            ll.setDividerDrawable(getDrawable(R.drawable.borders));
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(param);
            parent.addView(ll);
            for(int i = 0; i < c.getColumnCount(); i++){
                String text = c.getString(i);
                TextView view = new TextView(context);
                view.setGravity(Gravity.CENTER);
                LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams( parent.getWidth() / c.getColumnCount() - 10, p.getHeight());
                params1.setMarginStart(3);
                view.setLayoutParams(params1);
                view.setText(text);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                ll.addView(view);
            }
        }while(c.moveToNext());
    }

    public void Push(View view){
        String sql = code.getText().toString();
        String name = "null";
        if(sql.contains("users")) name = "users";
        else if (sql.contains("games")) name = "games";
        else if (sql.contains("transactions")) name = "transactions";
        else if(sql.contains("genres")) name = "genres";
        DataBase dataBase = new DataBase(context, name);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        /*Cursor c = db.rawQuery(sql, null);
        RunSQL(c);*/
        try {
            Cursor c = db.rawQuery(sql, null);
            RunSQL(c);
        }
        catch (Exception e){
            TextView text = new TextView(context);
            text.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            text.setText("wrong code " + e.toString());
            parent.addView(text);
        }
        finally {
            db.close();
        }
    }

    /*public void PrintTable(Cursor c){
        tv.setText("");
        if(!c.moveToFirst()){
            tv.setText("Wrong sql code");
            return;
        }
        do{
            for(int i = 0; i < c.getColumnCount(); i++){
                String text = c.getString(i);
                if(text.length() > 12) text = text.substring(0, 12);
                tv.setText(tv.getText().toString() + "    " + text);
            }
            tv.setText(tv.getText() + "\n");
        }while(c.moveToNext());
    }*/
}