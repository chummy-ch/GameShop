package com.example.gameshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.internal.$Gson$Preconditions;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class CardViewAdapter extends  RecyclerView.Adapter<CardViewAdapter.AdapterViewHolder> {
    private ArrayList<GameCard> games;
    private Context context;

    public CardViewAdapter(ArrayList games, Context context){
        this.context = context;
        this.games = games;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if(games.size() == 0) return;
        holder.name.setText(games.get(position).name);
        holder.genres.setText(games.get(position).genres.toString());
        holder.price.setText(String.valueOf(games.get(position).price) + "$");
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
