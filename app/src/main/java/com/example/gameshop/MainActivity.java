package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.TooManyListenersException;

public class MainActivity extends AppCompatActivity {
    public EditText login;
    public EditText psw;
    public Button sing;
    public Context context;
    public TextView singup;
    public TextView r;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login = findViewById(R.id.login_et);
        psw = findViewById(R.id.password_et);
        sing = findViewById(R.id.sing_in);
        singup = findViewById(R.id.sing_up);
        r = findViewById(R.id.reg);
        context = this;

        r.setVisibility(View.GONE);

        sing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = login.getText().toString();
                String p = psw.getText().toString();
                if(p.length() > 3 && l.length() > 3) {
                    SingIN in = new SingIN(context);
                    if(in.CheckUser(l, p) == true) Toast.makeText(context, "You are in",Toast.LENGTH_LONG).show();
                    else Toast.makeText(context,"Wrong login or password", Toast.LENGTH_LONG).show();
                }
                else Toast.makeText(context, "Fill the fields", Toast.LENGTH_LONG).show();
            }
        });


        final View.OnClickListener reg = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String l = login.getText().toString();
                String p = psw.getText().toString();
                if(isValidEmail(l) && isValidPassword(p)){
                    Toast.makeText(context, "You have register", Toast.LENGTH_LONG).show();
                }
                else  if(p.length() < 8) {
                    Toast.makeText(context,"Password must be at least 8 characters", Toast.LENGTH_LONG).show();
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }
                else{
                    Toast.makeText(context, "Invalid login", Toast.LENGTH_LONG).show();
                    Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                    vib.vibrate(100);
                }
            }
        };

        singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Vibrator vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                vib.vibrate(100);
                login.setHint("Put here your mail adress");
                psw.setHint("Create your password");
                singup.setVisibility(View.GONE);
                r.setVisibility(View.VISIBLE);
                LinearLayout l = (LinearLayout) r.getParent();
                l.setBackgroundResource(R.drawable.round_view);
                sing.setText("Register");
                sing.setOnClickListener(reg);
            }
        });

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


}