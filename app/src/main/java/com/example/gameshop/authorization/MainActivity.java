package com.example.gameshop.authorization;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gameshop.R;
import com.example.gameshop.genrelActivity.Genres;
import com.example.gameshop.shopActivity.ShopActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public EditText login;
    public EditText psw;
    public Button singin;
    public Context context;
    public TextView singup;
    public TextView regTV;
    public LinearLayout additional;
    public EditText age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_et);
        psw = findViewById(R.id.password_et);
        additional = findViewById(R.id.additionalinfo);
        singin = findViewById(R.id.sing_in);
        singup = findViewById(R.id.sing_up);
        regTV = findViewById(R.id.reg);
        age = findViewById(R.id.ageET);
        context = this;


    }

    public void ShowPsw(View view){
        final ImageView eye = findViewById(R.id.eye);
        psw.setTransformationMethod(null);
        eye.setBackgroundResource(R.drawable.openedeye);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                eye.setBackgroundResource(R.drawable.closedeye);
                psw.setTransformationMethod(new PasswordTransformationMethod());
            }
        }, 3000);

    }

    public void RegToLogin(View view){
        regTV.setVisibility(View.GONE);
        additional.setVisibility(View.GONE);
        singup.setText("Sing up");
        singin.setText("SING IN");
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logining(singin);
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginToReg(singup);
            }
        });
        findViewById(R.id.parentPanel).setBackground(null);
    }

    public void LoginToReg(View view){
        regTV.setVisibility(View.VISIBLE);
        additional.setVisibility(View.VISIBLE);
        singup.setText("Sing in");
        singin.setText("SING UP");
        singin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Registration(singin);
            }
        });
        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RegToLogin(singup);
            }
        });
        findViewById(R.id.parentPanel).setBackgroundResource(R.drawable.round_view);
    }

    private boolean isValidPassword(CharSequence target){
        return target.length() >= 8;
    }

    private boolean isValidEmail(CharSequence target) {
        if (target == null)  return false;
         else
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    private boolean isValidBirthday(String b){
        if(b.contains("D") || b.contains("Y") || b.contains("M")) return false;
        if(Integer.parseInt(b.substring(0, 2)) > 31 || Integer.parseInt(b.substring(3,5)) > 12) return false;
        return Calendar.getInstance().get(Calendar.YEAR) - Integer.parseInt(b.substring(6)) >= 6;
    }

    public void Logining(View view){
        SingIN in = new SingIN(this, this);
        if(in.Logining(login.getText().toString().toLowerCase(), psw.getText().toString())) {
            Toast.makeText(context, "You are in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(), ShopActivity.class);
            intent.putExtra("user", login.getText().toString().toLowerCase());
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else Toast.makeText(context, "Wrong login or passwrod", Toast.LENGTH_SHORT).show();
    }

    public void Registration(View view){
        SingIN in = new SingIN(this, this);
        if(!isValidEmail(login.getText())){
            Toast.makeText(context, "Invalid mail", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!isValidPassword(psw.getText())){
            Toast.makeText(context, "Invalid password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!isValidBirthday(age.getText().toString())){
            Toast.makeText(context, "Invalid birthday", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!in.AddUser(login.getText().toString().toLowerCase(), psw.getText().toString(), age.getText().toString())){
            Toast.makeText(context, "There is already user with the same email", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(context, "You have been registrated", Toast.LENGTH_SHORT).show();
        RegToLogin(singup);
    }
}