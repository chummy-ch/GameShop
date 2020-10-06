package com.example.gameshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class DBHelperActivity extends AppCompatActivity {
    private EditText name;
    private EditText price;
    private ImageView image;
    private CheckBox age;
    private EditText desc;
    private EditText genres;
    private EditText sale;
    private String imageUri;
    private Button save;
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
        save = findViewById(R.id.saveGame);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddGame();
            }
        });


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

                String files[] = src.list();
                int filesLength = files.length;
                for (int i = 0; i < filesLength; i++) {
                    String src1 = (new File(src, files[i]).getPath());
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

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {
                    //Получаем URI изображения, преобразуем его в Bitmap
                    //объект и отображаем в элементе ImageView нашего интерфейса:
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
                Toast.makeText(context, "Fill all the fields", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File folder = context.getExternalFilesDir("images");
        if(!folder.exists()){
            folder.mkdir();
        }
        String imagePath = imageUri.substring(imageUri.indexOf("storage"));
        copyFileOrDirectory(imagePath, folder.getPath());
        String imageName = imageUri.substring(imageUri.lastIndexOf('/') + 1);
        ContentValues cv = new ContentValues();
        AddGame addGame = new AddGame(this, this);
        SQLiteDatabase db = addGame.getWritableDatabase();
        cv.put("name", name.getText().toString());
        cv.put("price", Integer.parseInt(price.getText().toString()));
        cv.put("image", imageName);
        cv.put("description", desc.getText().toString());
        cv.put("genres", genres.getText().toString());
        cv.put("sale", sale.getText().toString());
        cv.put("AgeLimit", age.isChecked());
        db.insert("games", null, cv);
        addGame.close();
        finish();
    }
}