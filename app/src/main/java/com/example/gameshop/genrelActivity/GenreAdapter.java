package com.example.gameshop.genrelActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gameshop.R;

import java.util.ArrayList;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHodler> {
    private ArrayList<Genre> gens;
    private Context context;
    private Activity activity;
    private RecyclerView rec;

    public GenreAdapter(Context context, ArrayList<Genre> gens, RecyclerView rec, Activity activity){
        this.context = context;
        this.activity = activity;
        this.gens = gens;
        this.rec = rec;
    }

    private View.OnClickListener edit = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, GenreActivity.class);
            LinearLayout ll = (LinearLayout) view;
            TextView text = (TextView) ll.getChildAt(1);
            intent.putExtra("edit", text.getText().toString());
            context.startActivity(intent);
            activity.finish();
        }
    };

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.genre_table_layout, parent, false);
        view.setOnClickListener(edit);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        if(gens.size() == 0) return;
        holder.id.setText(String.valueOf(position));
        holder.name.setText(gens.get(position).gen);
        holder.desc.setText(gens.get(position).desc);
    }

    @Override
    public int getItemCount() {
        return gens.size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView name;
        public TextView desc;

        public ViewHodler(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.genreId);
            name = itemView.findViewById(R.id.genreName);
            desc = itemView.findViewById(R.id.genreDesc);
        }
    }
}
