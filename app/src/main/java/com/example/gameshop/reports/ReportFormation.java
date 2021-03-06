package com.example.gameshop.reports;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import com.example.gameshop.DataBase;

import java.util.Date;

public class ReportFormation {
    private Context context;

    public ReportFormation(Context context){
        this.context = context;
    }

    public String GetCheck(String id, String user){
        StringBuilder rep = new StringBuilder();
        rep.append(String.format(" %30s%n", "Чек №" + id));
        rep.append(String.format(" %30s%n", user));
        rep.append(String.format(" %30s%n", new Date().toString()));
        rep.append(String.format("%5s %19s %13s%n", "Название", "Количество", "Цена"));
        rep.append(String.format(" %-20s %-20s %-20s%n", "--------------", "--------------", "--------------"));
        SQLiteDatabase db = new DataBase(context, "transactions").getWritableDatabase();
        Cursor c = db.rawQuery("select game, price from transactions where id = '" + id + "';", null);
        c.moveToFirst();
        do{
            rep.append(String.format(" %5s %15s %20s%n", c.getString(0), 1,
                    c.getString(1) + "$"));
        }while(c.moveToNext());
        db.close();
        c.close();

        rep.append(String.format(" %-20s %-20s %-20s%n", "--------------", "--------------", "--------------"));
        rep.append("Спасибо за покупку в нашем магазине. Будем рады вам снова!");
        rep.append("\n\t\t ©DETAGamesMarket");
        return rep.toString();
    }

    public String GetSalesReport(){
        StringBuilder rep = new StringBuilder();

        rep.append(String.format(" %30s%n", "Monthly Report"));
        rep.append(String.format(" %30s%n", "Sales Report"));
        rep.append(String.format("%5s %19s %13s%n", "Name", "Quantity", "Income"));
        rep.append(String.format(" %-20s %-20s %-20s%n", "--------------", "--------------", "--------------"));

        SQLiteDatabase db = new DataBase(context, "transactions").getWritableDatabase();
        String sqlite = "select game, count(game) as Quantity, sum(price) as price " +
                "from transactions group by game order by count(game) desc;";
        Cursor c = db.rawQuery(sqlite, null);
        if(!c.moveToFirst()) return rep.toString();
        do{
            String name = c.getString(0);
            if(name.length() > 8) name = name.substring(0, 8) + "\n\t" + name.substring(8);
            if(name.length() < 8) name +="\t\t";
            rep.append(String.format(" %5s %15s %20s%n", name, c.getString(1),
                    c.getString(2) + "$"));
            rep.append("\n");
        }while(c.moveToNext());
        c = db.rawQuery("select count(game), sum(price) from transactions;", null);
        c.moveToFirst();
        rep.append(String.format(" %-20s %-20s %-20s%n", "--------------", "--------------", "--------------"));
        rep.append(String.format(" %5s %18s %20s%n", "TOTAL", c.getString(0), c.getString(1) + "$"));
        rep.append(String.format(" %-20s %-20s %-20s%n", "--------------", "--------------", "--------------"));
        db.close();
        c.close();
        return rep.toString();
    }
}
