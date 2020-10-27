package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class GamesDBHelperActivity extends AppCompatActivity {
    private EditText name, price, desc, genres, sale;
    private ImageView image;
    private EditText age;
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
        genres = findViewById(R.id.genres);
        sale = findViewById(R.id.sale);
        Button save = findViewById(R.id.saveGame);

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
        price.setText(String.valueOf(game.price));
        sale.setText(String.valueOf(game.sale));
        age.setText(String.valueOf(game.ageLimit));
        File folder = context.getExternalFilesDir("images");
        File imageFile = new File(folder.getPath() + "/" + game.image);
        if(imageFile.exists()){
            Glide.with(context).load(imageFile).into(image);
        }
        genres.setText(game.genres);
        imageUri = game.image;
    }

    public void ChooseImage(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, Pick_image);
    }

    public static void copyFileOrDirectory(String srcDir, String dstDir) {

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

    private void AddGame(){
        EditText[] arr = new EditText[]{name, price, desc, genres, sale};
        for (EditText et : arr) {
            if(et.getText().toString().trim().length() < 1) {
                Toast.makeText(context, "Fill the '" + et.getHint().toString() + "' field", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(imageUri == null){
            Toast.makeText(context, "Choose an image for  the game", Toast.LENGTH_SHORT).show();
            return;
        }
        File folder = context.getExternalFilesDir("images");
        if(!folder.exists()){
            folder.mkdir();
        }
        String imagePath = imageUri.substring(imageUri.indexOf("storage"));
        copyFileOrDirectory(imagePath, folder.getPath());
        String imageName = imageUri.substring(imageUri.lastIndexOf('/') + 1);
        ContentValues cv = new ContentValues();
        GamesDB gamesDB = new GamesDB(this, this);
        SQLiteDatabase db = gamesDB.getWritableDatabase();


        cv.put("game", name.getText().toString());
        cv.put("price", Integer.parseInt(price.getText().toString()));
        cv.put("image", imageName);
        cv.put("description", desc.getText().toString());
        cv.put("genres", genres.getText().toString());
        cv.put("sale", sale.getText().toString());
        cv.put("AgeLimit", Integer.valueOf(age.getText().toString()));

        db.insert("games", null, cv);
        gamesDB.close();
        finish();
    }

    private void EditGame(){
        if(imageUri != null){
            File folder = context.getExternalFilesDir("images");
            if(!folder.exists()){
                folder.mkdir();
            }
            String imagePath = imageUri.substring(imageUri.indexOf("storage"));
            copyFileOrDirectory(imagePath, folder.getPath());
            game.image = imageUri.substring(imageUri.lastIndexOf('/') + 1);
        }
        ContentValues cv = new ContentValues();
        GamesDB gamesDB = new GamesDB(this, this);
        SQLiteDatabase db = gamesDB.getWritableDatabase();
        Cursor c = db.query("games", null, null, null, null, null, null);
        if(!c.moveToFirst()) return;
        cv.put("game", name.getText().toString());
        cv.put("price", Integer.parseInt(price.getText().toString()));
        cv.put("image", imageUri);
        cv.put("description", desc.getText().toString());
        cv.put("genres", genres.getText().toString());
        cv.put("sale", sale.getText().toString());
        cv.put("AgeLimit", Integer.valueOf(age.getText().toString()));
        /*db.execSQL("UPDATE games SET price = 110 WHERE game = " + "'" + game.name + "'" + ";");*/
        db.update("games", cv, "game=" + "'" + game.name + "'", null);
        gamesDB.close();
        finish();
    }
}