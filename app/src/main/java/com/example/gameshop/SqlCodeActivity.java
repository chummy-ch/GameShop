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
import android.widget.Toast;

import org.w3c.dom.Text;

public class SqlCodeActivity extends AppCompatActivity {
    private Context context;
    private EditText code;
    private LinearLayout parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_code);
        code = findViewById(R.id.code);
        context = this;
        parent = findViewById(R.id.parentScroll);
        Display display = getWindowManager().getDefaultDisplay();
        code.setLayoutParams(new LinearLayout.LayoutParams(display.getWidth(), display.getHeight() / 8));
        parent.setLayoutParams(new FrameLayout.LayoutParams(display.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void Clear(View view){ parent.removeViews(0, parent.getChildCount());}

    public void Exit(View view){finish();}

    public void RunSQL(Cursor c){
        c.moveToFirst();
        if(parent.getChildCount() != 0)
        Clear(parent);
        LinearLayout.LayoutParams namesParams = new LinearLayout.LayoutParams(parent.getWidth() / c.getColumnCount(), ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        parent.setDividerDrawable(getDrawable(R.color.borders));
        parent.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        LinearLayout l = new LinearLayout(context);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setId(parent.getChildCount());
        l.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        l.setDividerDrawable(getDrawable(R.color.borders));
        l.setLayoutParams(linearParams);
        parent.addView(l);
        for(int i = 0; i < c.getColumnCount(); i++){
            String text = c.getColumnName(i);
            TextView view = new TextView(context);
            view.setLayoutParams(namesParams);
            view.setText(text);
            view.setGravity(Gravity.CENTER);
            view.setTextColor(Color.BLACK);
            l.addView(view);
        }
        do{
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setId(parent.getChildCount());
            ll.setDividerDrawable(getDrawable(R.color.borders));
            ll.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
            ll.setLayoutParams(linearParams);
            parent.addView(ll);
            for(int i = 0; i < c.getColumnCount(); i++){
                String text = c.getString(i);
                TextView view = new TextView(context);
                view.setGravity(Gravity.CENTER);
                view.setLayoutParams(namesParams);
                view.setText(text);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                ll.addView(view);
            }
        }while(c.moveToNext());
        c.close();
    }

    public void Push(View view){
        String sql = code.getText().toString();
        String lastIndexOf = ";";
        if(!sql.contains(";")) sql += ";";
        while(sql.substring(sql.lastIndexOf(";") - 1, sql.length() - 1).equals(" "))
        sql = sql.substring(0, sql.lastIndexOf(" ")) + ";";
        if(sql.length() - sql.replaceAll(" ", "").length() > 3) lastIndexOf = " ";
        String table = sql.substring(sql.indexOf("from") + 5, sql.substring(sql.indexOf("from") + 5).indexOf(lastIndexOf) + sql.indexOf("from") + 5);
        DataBase dataBase = new DataBase(context, table);
        SQLiteDatabase db = dataBase.getWritableDatabase();
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
}