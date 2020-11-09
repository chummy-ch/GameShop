package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SqlCodeActivity extends AppCompatActivity {
    private Context context;
    private EditText code;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sql_code);
        code = findViewById(R.id.code);
        context = this;
        tv = findViewById(R.id.table);
    }

    public void Clear(View view){ tv.setText("");}

    public void Exit(View view){finish();}

    public void Push(View view){
        String sql = code.getText().toString();
        String name = "null";
        if(sql.contains("users")) name = "users";
        else if (sql.contains("games")) name = "games";
        else if (sql.contains("transactions")) name = "transactions";
        DataBase dataBase = new DataBase(context, name);
        SQLiteDatabase db = dataBase.getWritableDatabase();
        try {
            Cursor c = db.rawQuery(sql, null);
            PrintTable(c);
        }
        catch (Exception e){
            TextView tv = findViewById(R.id.table);
            tv.setText(e.toString());
        }
        finally {
            db.close();
        }
    }

    public void PrintTable(Cursor c){
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
    }
}