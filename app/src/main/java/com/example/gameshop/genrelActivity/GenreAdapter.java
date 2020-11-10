package com.example.gameshop.genrelActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameshop.R;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHodler> {
    private ArrayList<String> gens;
    private Context context;
    private RecyclerView rec;

    public GenreAdapter(Context context, ArrayList<String> gens, RecyclerView rec){
        this.context = context;
        this.gens = gens;
        this.rec = rec;
    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.genre_table_layout, parent, false);

        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        if(gens.size() == 0) return;
        holder.id.setText(String.valueOf(position));
        holder.name.setText(gens.get(position));
    }

    @Override
    public int getItemCount() {
        return gens.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView name;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.genreId);
            name = itemView.findViewById(R.id.genreName);
        }
    }
}
