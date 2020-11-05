package com.example.gameshop;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Genres {
    private ArrayList<String> gen;
    private Context context;

    public Genres(Context context){
        this.context = context;
        gen = new ArrayList<>();
        LoadGen();
    }

    public ArrayList<String> GetGenres(){return gen;}

    public void AddGenre(String genre){
        if(gen.contains(genre)) return;
        gen.add(genre);
        SaveGen();
    }

    public void RemoveGenre(String genre){
        if(!gen.contains(genre)) return;
        gen.remove(genre);
        SaveGen();
    }

    private void SaveGen(){
        String jsonString = new Gson().toJson(gen);
        try{
            String filePath = context.getFilesDir().getPath().toString() + "genres.txt";
            FileWriter file = new FileWriter(filePath);
            file.write(jsonString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadGen(){
        try{
            String filePath = context.getFilesDir().getPath().toString() + "genres.txt";
            File f = new File(filePath);
            if(!f.exists()) return ;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            gen = new Gson().fromJson(stringBuffer.toString(), gen.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
