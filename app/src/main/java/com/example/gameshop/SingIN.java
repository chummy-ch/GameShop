package com.example.gameshop;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class SingIN {
    private HashMap<String, String> users;
    private Context context;


    public SingIN(Context context){
        users = new HashMap<>();
        this.context = context;
        LoadUsers();
    }

    public boolean CheckUser(String user, String password){
        return users.containsKey(user) && users.get(user).equals(password);
    }

    public boolean AddUser(String login, String password){
        if(!users.containsKey(login)) {
            users.put(login, password);
            SaveUsers();
            return true;
        }
        else return false;
    }

    private void SaveUsers(){
        String jsonString = new Gson().toJson(users);
        try{
            String filePath = context.getFilesDir().getPath() + "users.txt";
            FileWriter file = new FileWriter(filePath);
            file.write(jsonString);
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void LoadUsers(){
        try{
            String filePath = context.getFilesDir().getPath() + "users.txt";
            File f = new File(filePath);
            if(!f.exists()) return ;
            BufferedReader br = new BufferedReader(new FileReader(filePath));
            String line;
            StringBuilder stringBuffer = new StringBuilder();
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            users = new Gson().fromJson(stringBuffer.toString(), users.getClass());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
