package com.example.gameshop.shopActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.TransitionOptions;
import com.example.gameshop.DataBase;
import com.example.gameshop.GameActivity;
import com.example.gameshop.R;
import com.google.gson.Gson;

import java.io.File;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.Arrays;

public class CardViewAdapter extends  RecyclerView.Adapter<CardViewAdapter.AdapterViewHolder> {
    private ArrayList<GameCard> games;
    private Context context;
    private RecyclerView rec;
    private String user;

    public CardViewAdapter(ArrayList games, Context context, RecyclerView rec, String user){
        this.context = context;
        this.user = user;
        this.games = games;
        this.rec = rec;
    }

    View.OnClickListener openCard = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int pos = rec.getChildLayoutPosition(view);
            GameCard game = games.get(pos);
            Intent intent = new Intent(context, GameActivity.class);
            String jsonGame = new Gson().toJson(game);
            intent.putExtra("game", jsonGame);
            intent.putExtra("user", user);
            context.startActivity(intent);
            DataBase database = new DataBase(context, "views");
            SQLiteDatabase db = database.getWritableDatabase();
            Cursor c = db.rawQuery("select * from views where game = '" + game.name + "';", null);
            if(c.moveToFirst())
            db.execSQL("update views set times = times + 1 where game = '" + game.name + "';");
            else db.execSQL("insert into views (game, times) values ('" + game.name + "', 0)");
            c.close();
            db.close();
        }
    };

    @Override
    public AdapterViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card, parent, false);
        view.setOnClickListener(openCard);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AdapterViewHolder holder, int position) {
        if(games.size() == 0) return;
        holder.name.setText(games.get(position).name);
        String g = Arrays.toString(games.get(position).genres).replace("[", "").replace("]", "");
        holder.genres.setText(g.replaceAll(",", " "));
        holder.price.setText(String.valueOf(games.get(position).price - games.get(position).sale) + "$");
        if(games.get(position).sale > 0) holder.price.setTextColor(Color.RED);
        File folder = context.getExternalFilesDir("images");
        if(games.get(position).image == null) return;
        if(games.get(position).image.contains("/")) games.get(position).image = games.get(position).image.substring(games.get(position).image.lastIndexOf('/') + 1);
        String img = games.get(position).image;
        File image = new File(folder.getPath() + "/" + img);
            if(image.exists()){
                Glide.with(context).load(image).into(holder.image);
            }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView name;
        TextView genres;
        TextView price;

        public AdapterViewHolder(View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            genres = itemView.findViewById(R.id.gens);
            price = itemView.findViewById(R.id.gameprice);
        }

    }
}
