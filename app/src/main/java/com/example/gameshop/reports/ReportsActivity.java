package com.example.gameshop.reports;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.example.gameshop.R;
import com.example.gameshop.mailsender.MarketMail;
import com.example.gameshop.mailsender.SendMail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.spi.FileTypeDetector;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class ReportsActivity extends AppCompatActivity {
    private Context context;
    private LinearLayout parent;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        context = this;
        parent = findViewById(R.id.reportparent);
    }

    private TextView CreateField(){
        ImageButton button = findViewById(R.id.downloadreport);
        button.setVisibility(View.VISIBLE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateFile("Income");
            }
        });
        parent.setGravity(Gravity.CENTER_HORIZONTAL);
        parent.removeViews(1,2);
        user = getIntent().getStringExtra("user");
        TextView tv = new TextView(context);
        tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
        tv.setMovementMethod(new ScrollingMovementMethod());
        parent.addView(tv);
        return tv;
    }

    private void CreateFile(String name){
            /*FileWriter file = new FileWriter(filepath);
            file.write(new ReportFormation(context).GetSalesReport());
            file.close();*/
        File folder = context.getExternalFilesDir("reports");
        String reportName = "(" + name + ")" + new Date().toString();
        String path = folder.getPath() + "/" + reportName + ".txt";
        if(!folder.exists()) folder.mkdir();
        FileWriter report = null;
        try {
            report = new FileWriter(path);
            report.write(new ReportFormation(context).GetSalesReport());
            report.close();
            SendMail send = new SendMail(context, user, "Report", "Here is your report", path);
            send.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void MonthReport(View view){
        TextView tv = (TextView) CreateField();
        tv.setText(new ReportFormation(context).GetSalesReport());
    }
}