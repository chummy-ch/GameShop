package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.gameshop.genrelActivity.Genre;
import com.example.gameshop.genrelActivity.Genres;
import com.example.gameshop.shopActivity.GameCard;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddGameActivity extends AppCompatActivity {
    private EditText name, price, desc, sale, creator;
    private AutoCompleteTextView genres;
    private ImageView image;
    private EditText age;
    private String user;
    private GameCard game;
    private String imageUri;
    private final int Pick_image = 1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);

        context = this;
        name = findViewById(R.id.gameName);
        price = findViewById(R.id.price);
        image = findViewById(R.id.image);
        desc = findViewById(R.id.desc);
        age = findViewById(R.id.ageLimit);
        genres = findViewById(R.id.genre);
        sale = findViewById(R.id.sale);
        Button save = findViewById(R.id.saveGame);
        user = getIntent().getStringExtra("user");
        creator = findViewById(R.id.creator);

        Genres gen = new Genres(context);
        ArrayList<String> genString = new ArrayList<>();
        for(Genre g : gen.GetGenres()) genString.add(g.gen);
        genres.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, genString));

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGame();
            }
        });

        if(getIntent().hasExtra("EditGame")){
            Gson json = new Gson();
            game = json.fromJson(getIntent().getStringExtra("EditGame"), GameCard.class);
            Filler();
            findViewById(R.id.removeGame).setVisibility(View.VISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditGame();
                }
            });
        }
    }

    private void Filler(){
        name.setText(game.name);
        desc.setText(game.disc);
        creator.setText(game.creator);
        price.setText(String.valueOf(game.price));
        sale.setText(String.valueOf(game.sale));
        age.setText(String.valueOf(game.ageLimit));
        File folder = context.getExternalFilesDir("images");
        if(game.image == null) return;
        String img = game.image.substring(game.image.lastIndexOf('/') + 1);
        File image = new File(folder.getPath() + "/" + img);
        if(image.exists()){
            Glide.with(context).load(image).into(this.image);
        }
        genres.setText(game.genres[0]);
        LinearLayout ll = findViewById(R.id.genres);
        for(int i = 1; i < game.genres.length; i++) {
            AddGenET(genres);
            EditText text = (EditText) ll.getChildAt(i);
            text.setText(game.genres[i]);
        }
        imageUri = image.getPath();
    }

    public void ChooseImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, Pick_image);
    }

    public void AddGenET(View view){
        LinearLayout ll = findViewById(R.id.genres);
        AutoCompleteTextView text = new AutoCompleteTextView(context);
        text.setLayoutParams(genres.getLayoutParams());
        text.setId(ll.getChildCount());
        text.setHint("Genre");
        Genres gen = new Genres(context);
        ArrayList<String> genString = new ArrayList<>();
        for(Genre g : gen.GetGenres()) genString.add(g.gen);
        text.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, genString));
        ll.addView(text, (int)text.getId() - 1);
    }

    public void copyFileOrDirectory(String srcDir, String dstDir) {

        try {
            File src = new File(srcDir);
            File dst = new File(dstDir, src.getName());
            if (src.isDirectory()) {
                for (String file : src.list()) {
                    String src1 = (new File(src, file).getPath());
                    String dst1 = dst.getPath();
                    copyFileOrDirectory(src1, dst1);
                }
            } else {
                copyFile(src, dst);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.getParentFile().exists())
            destFile.getParentFile().mkdirs();

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        FileChannel source = null;
        FileChannel destination = null;

        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());
        } finally {
            if (source != null) {
                source.close();
            }
            if (destination != null) {
                destination.close();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == Pick_image) {
            if (resultCode == RESULT_OK) {
                final Uri imageUri = imageReturnedIntent.getData();
                this.imageUri = imageUri.getPath();
                Glide.with(context).load(imageUri).into(image);
            }
        }
    }

    public void RemoveGame(View view){
        DataBase gamesDB = new DataBase(this, "games");
        SQLiteDatabase db = gamesDB.getWritableDatabase();
        db.execSQL("DELETE FROM games WHERE game = '" + game.name + "'");
        db.close();
        finish();
        DeleteImage(game.image.substring(game.image.lastIndexOf('/') + 1));
    }

    private void AddGame(){
        if(IsFilled()) return;
        File folder = context.getExternalFilesDir("images");
        if(!folder.exists()){
            folder.mkdir();
        }
        LinearLayout ll = findViewById(R.id.genres);
        String gens = "";
        Genres genres = new Genres(context);
        ArrayList<Genre> genArray = genres.GetGenres();
        ArrayList<String> g = new ArrayList<>();
        for(int i = 0; i < genArray.size(); i++){ g.add(genArray.get(i).gen); }
        for(int i = 0 ; i < ll.getChildCount() - 1; i++){
            AutoCompleteTextView t = (AutoCompleteTextView) ll.getChildAt(i);
            if(!gens.contains(t.getText().toString()) && g.contains(t.getText().toString()))
            gens += t.getText().toString() + ",";
        }
        if(gens.trim().replaceAll(",", "").length() < 2){
            Toast.makeText(context, "There are no such genres in the list", Toast.LENGTH_LONG).show();
            return;
        }
        String imagePath = "";
        if(imageUri.contains("storage")) imagePath = imageUri.substring(imageUri.indexOf("storage"));
        else imagePath = imageUri.substring(imageUri.indexOf("external"));
        copyFileOrDirectory(imagePath, folder.getPath());
        String imageName = imageUri.substring(imageUri.lastIndexOf('/') + 1);
        ContentValues cv = new ContentValues();
        DataBase gamesDB = new DataBase(this, "games");
        SQLiteDatabase db = gamesDB.getWritableDatabase();

        Cursor c = db.rawQuery("select * from games where game = " + "'" + name.getText().toString() + "';", null);
        if(c.moveToFirst()){
            Toast.makeText(context, "There is already game with the same name", Toast.LENGTH_LONG).show();
            db.close();
            return;
        }

        cv.put("game", name.getText().toString());
        cv.put("price", Integer.parseInt(price.getText().toString()));
        cv.put("image", imageName);
        cv.put("description", desc.getText().toString());
        cv.put("genres", gens);
        cv.put("sale", sale.getText().toString());
        cv.put("ageLimit", Integer.valueOf(age.getText().toString()));
        cv.put("addedday", new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        cv.put("addedby", user);
        cv.put("creator",creator.getText().toString());

        db.insert("games", null, cv);
        gamesDB.close();

        gamesDB = new DataBase(context, "view");
        db = gamesDB.getWritableDatabase();
        db.execSQL("insert into views (times) values (0);");
        db.close();
        finish();
    }

    private void DeleteImage(String imgName){
        File img = context.getExternalFilesDir("images" + "/" + imgName);
        if(img.exists()) img.delete();
    }

    private boolean IsFilled(){
        EditText[] arr = new EditText[]{name, price, desc, genres, sale};
        for (EditText et : arr) {
            if(et.getText().toString().trim().length() < 1) {
                Toast.makeText(context, "Fill the '" + et.getHint().toString() + "' field", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        if(imageUri == null){
            Toast.makeText(context, "Choose an image for  the game", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void EditGame(){
        if(IsFilled()) return;
        if(imageUri != null){
            File folder = context.getExternalFilesDir("images");
            if(!folder.exists()){
                folder.mkdir();
            }
            String imagePath = imageUri.substring(imageUri.indexOf("storage"));
            copyFileOrDirectory(imagePath, folder.getPath());
            imageUri = imageUri.substring(imageUri.lastIndexOf('/') + 1);
            if(!game.image.equals(imageUri)){
                DeleteImage(game.image.substring(game.image.lastIndexOf('/') + 1));
                game.image = imageUri;
            }
        }
        String gens = "";
        ArrayList<Genre> gs = new Genres(context).GetGenres();
        ArrayList<String> g = new ArrayList<>();
        for(int i = 0; i < gs.size(); i++){ g.add(gs.get(i).gen); }
        LinearLayout ll = findViewById(R.id.genres);
        for(int i = 0; i < ll.getChildCount() - 1; i++){
            AutoCompleteTextView t = (AutoCompleteTextView) ll.getChildAt(i);
            if(g.contains(t.getText().toString())) {
                if(gens.length() > 0) gens += "," + t.getText().toString();
                else gens += t.getText().toString();
            }
        }
        if(gens.trim().replaceAll(",", "").length() < 2){
            Toast.makeText(context, "There are no such genres in the list", Toast.LENGTH_LONG).show();
            return;
        }
        ContentValues cv = new ContentValues();
        DataBase gamesDB = new DataBase(this, "games");
        SQLiteDatabase db = gamesDB.getWritableDatabase();
        cv.put("game", name.getText().toString());
        cv.put("price", Integer.parseInt(price.getText().toString()));
        cv.put("image", game.image);
        cv.put("description", desc.getText().toString());
        cv.put("genres", gens);
        cv.put("sale", sale.getText().toString());
        cv.put("ageLimit", Integer.valueOf(age.getText().toString()));
        cv.put("creator",creator.getText().toString());
        /*db.execSQL("UPDATE games SET price = 110 WHERE game = " + "'" + game.name + "'" + ";");*/
        db.update("games", cv, "game=" + "'" + game.name + "'", null);
        gamesDB.close();
        if(!name.getText().toString().equals(game.name)){
            gamesDB = new DataBase(context, "view");
            db = gamesDB.getWritableDatabase();
            db.execSQL("update views set game = '" + name.getText().toString() + "' where game = '" + game.name + "';");
            db.close();
        }
        finish();
    }
}