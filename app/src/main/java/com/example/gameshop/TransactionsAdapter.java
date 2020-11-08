package com.example.gameshop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {
    private Context context;
    private RecyclerView rec;
    private ArrayList<Transaction> trans;

    public TransactionsAdapter (Context context, RecyclerView rec, ArrayList<Transaction> trans){
        this.context = context;
        this.rec = rec;
        this.trans = trans;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.transaction_layout,parent,  false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (trans.size() == 0) return;
        holder.id.setText(String.valueOf(trans.get(position).id));
        holder.game.setText(trans.get(position).game);
        holder.user.setText(trans.get(position).user);
        holder.price.setText(trans.get(position).price + "$");
        holder.date.setText((trans.get(position).date));
    }

    @Override
    public int getItemCount() {
        return trans.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView id, game, user, price, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.transId);
            game = itemView.findViewById(R.id.transGame);
            user = itemView.findViewById(R.id.transUser);
            price = itemView.findViewById(R.id.transPrice);
            date = itemView.findViewById(R.id.transDate);
        }
    }
}
