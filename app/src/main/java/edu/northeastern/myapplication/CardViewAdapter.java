package edu.northeastern.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.ViewHolder> {

    private List<Tip> tipDataList;

    public CardViewAdapter(List<Tip> tips) {
        this.tipDataList = tips;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_card_view, parent,false);
        final ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Tip tipItem = tipDataList.get(position);
//        holder.image.setImageResource(currentTip.getUrl());
        holder.image.setImageResource(tipItem.getImageID());
        holder.title.setText(tipItem.getTitle());
        holder.username.setText(tipItem.getUsername());

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View tipView;

        public ImageView image;
        public TextView title;
        public TextView username;

        public ViewHolder(View itemView) {
            super(itemView);
            tipView = itemView;
            image = itemView.findViewById(R.id.imageView);
            title = itemView.findViewById(R.id.titleView);
            username = itemView.findViewById(R.id.usernameView);
        }
    }

    @Override
    public int getItemCount() {
        return tipDataList.size();
    }
}
