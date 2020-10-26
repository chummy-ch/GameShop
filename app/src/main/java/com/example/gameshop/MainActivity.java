package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

        /*Toast.makeText(context, "You are in",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), ShopActivity.class);
        intent.putExtra("user", "jaja@gmail.com");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        */


        /* reg = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = login.getText().toString().toLowerCase();
                String p = psw.getText().toString();
                EditText date = findViewById(R.id.ageET);
                String d = date.getText().toString();
                if(isValidEmail(l) && isValidPassword(p)){

                }
                else  if(p.length() < 8) {
                    Toast.makeText(context,"Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }
                else if(d.length() != 10) {
                    Toast.makeText(context, "Put in your birthday", Toast.LENGTH_SHORT).show();
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }
                else{
                    Toast.makeText(context, "Invalid login", Toast.LENGTH_LONG).show();
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }
            }
        };*/
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

    public boolean isValidPassword(CharSequence target){
        return target.length() >= 8;
    }

    public  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void Logining(View view){
        Toast.makeText(context, "Logining....", Toast.LENGTH_LONG).show();
    }

    public void Registration(View view){
        SingIN in = new SingIN(this, this);
        if(!isValidEmail(login.getText()) || !isValidPassword(psw.getText())) {
            Toast.makeText(context, "Incorrect login or password", Toast.LENGTH_LONG).show();
            return;
        }
        if(!in.AddUser(login.getText().toString(), psw.getText().toString(), age.getText().toString())){
            Toast.makeText(context, "There is already user with the same email", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(context, "You have been registrated", Toast.LENGTH_LONG).show();
        RegToLogin(singup);
    }
}