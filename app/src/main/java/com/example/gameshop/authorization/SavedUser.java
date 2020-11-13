package com.example.gameshop.authorization;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class SavedUser {
    private ArrayList<String> user;
    private Context context;

    public SavedUser(Context context){
        this.context = context;
    }

    public ArrayList<String> GetSavedUser(){
        Load();
        return user;
    }

    public void SaveUser(String login, String psw){
        user = new ArrayList<>();
        user.add(login);
        user.add(psw);
        Save();
    }

    public void RemoveUser(){
        user = new ArrayList<>();
        Save();
    }

    private void Save(){
        String jsonString = new Gson().toJson(user);
        try{
            String filePath = context.getFilesDir().getPath().toString() + "SavedUser.txt";
            FileWriter file = new FileWriter(filePath);
            file.write(jsonString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void Load(){
        try{
            String filePath = context.getFilesDir().getPath().toString() + "SavedUser.txt";
            File f = new File(filePath);
            if(!f.exists()) return;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuffer stringBuffer = new StringBuffer();
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            user = new ArrayList<>();
            user = new Gson().fromJson(stringBuffer.toString(), user.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
