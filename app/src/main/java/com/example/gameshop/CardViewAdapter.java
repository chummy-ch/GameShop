package com.example.gameshop;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.util.ArrayList;

public class CardViewAdapter extends  RecyclerView.Adapter<CardViewAdapter.AdapterViewHolder> {
    private ArrayList<GameCard> games;
    private Context context;
    private RecyclerView rec;

    public CardViewAdapter(ArrayList games, Context context, RecyclerView rec){
        this.context = context;
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
            context.startActivity(intent);
        }
    };

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, parent, false);
        view.setOnClickListener(openCard);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if(games.size() == 0) return;
        holder.name.setText(games.get(position).name);
        holder.genres.setText(games.get(position).genres.toString());
        holder.price.setText(String.valueOf(games.get(position).price) + "$");
        File folder = context.getExternalFilesDir("images");
        String img = games.get(position).image.substring(games.get(position).image.lastIndexOf('/') + 1);
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

        public AdapterViewHolder(@NonNull View itemView){
            super(itemView);
            image = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.name);
            genres = itemView.findViewById(R.id.gens);
            price = itemView.findViewById(R.id.gameprice);
        }

    }
}
