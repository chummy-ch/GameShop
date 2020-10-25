package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.TooManyListenersException;

public class MainActivity extends AppCompatActivity {
    public EditText login;
    public EditText psw;
    public Button sing;
    public Context context;
    public TextView singup;
    public TextView r;
    public EditText age;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_et);
        psw = findViewById(R.id.password_et);
        sing = findViewById(R.id.sing_in);
        singup = findViewById(R.id.sing_up);
        r = findViewById(R.id.reg);
        age = findViewById(R.id.ageET);
        context = this;

        Toast.makeText(context, "You are in",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), ShopActivity.class);
        intent.putExtra("user", "jaja@gmail.com");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();


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

    public boolean isValidPassword(CharSequence target){
        if(target.length() >= 8) return true;
        else return false;
    }

    public  boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void Registration(View view){
        SingIN in = new SingIN(this, this);
        in.AddUser(login.getText().toString(), psw.getText().toString(), age.getText().toString());
    }
}