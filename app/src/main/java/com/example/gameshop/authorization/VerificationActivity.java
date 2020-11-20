package com.example.gameshop.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameshop.DataBase;
import com.example.gameshop.R;
import com.example.gameshop.shopActivity.ShopActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class VerificationActivity extends AppCompatActivity {
    private EditText code;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        code = findViewById(R.id.secretcode);

        WrongMail();
    }

    private void WrongMail(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                TextView view = findViewById(R.id.wrongmail);
                view.setVisibility(View.VISIBLE);
            }
        }, 30000);

    }

    private void ContinueReg(){
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = new DataBase(this, "users").getWritableDatabase();
        if(db.rawQuery("select * from users;", null).getCount() == 0)
            cv.put("admin", 1);
        cv.put("mail", getIntent().getStringExtra("mail"));
        cv.put("age", getIntent().getStringExtra("brth"));
        cv.put("password", getIntent().getStringExtra("psw"));
        String regDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
        cv.put("regday", regDate);
        db.insert("users", null, cv);
        db.close();
    }

    public void Ok(View view){
        if(getIntent().getStringExtra("code").equals(code.getText().toString()))
        {
            ContinueReg();
            Intent intent = new Intent(this, ShopActivity.class);
            intent.putExtra("user", getIntent().getStringExtra("mail"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else Toast.makeText(context, "Wrong code", Toast.LENGTH_LONG).show();
    }

    public void Cancel(View view){
        finish();
    }
}