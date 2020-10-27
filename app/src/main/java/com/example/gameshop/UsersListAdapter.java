package com.example.gameshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsersListAdapter extends RecyclerView.Adapter<UsersListAdapter.AdapterViewHolder> {
    private ArrayList<User> users;
    private Context context;
    private RecyclerView rec;

    public UsersListAdapter(Context context, ArrayList<User> users, RecyclerView rec){
        this.context = context;
        this.users = users;
        this.rec = rec;
    }

    @NonNull
    @Override
    public AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.users_list_layout, parent, false);
        return new AdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterViewHolder holder, int position) {
        if(users.size() == 0) return;
        holder.user.setText(users.get(position).user);
        holder.games.setText(users.get(position).games);
        holder.admin.setText(String.valueOf(users.get(position).admin));
        holder.age.setText(users.get(position).birthday);
        holder.psw.setText(users.get(position).password);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class AdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView user, games, admin, age, psw;

        public AdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            user = itemView.findViewById(R.id.user);
            games = itemView.findViewById(R.id.games);
            admin = itemView.findViewById(R.id.admin);
            age = itemView.findViewById(R.id.age);
            psw = itemView.findViewById(R.id.password);
        }
    }
}
