package com.thebegining.instacomdevassigment.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.thebegining.instacomdevassigment.R;

import java.util.ArrayList;

public class adapter_feed extends RecyclerView.Adapter<adapter_feed.News> {
    private ArrayList<news> list;

    public adapter_feed(ArrayList<news> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public News onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_item, parent, false);
        return new News(v);
    }

    @Override
    public void onBindViewHolder(@NonNull News holder, int position) {
        news item = list.get(position);
        holder.name.setText(item.getName());
        holder.message.setText(item.getMessage());
        holder.date.setText(item.getCreatedAt());
        Log.d("error","adapter item name: "+item.getName());
        Log.d("error","adapter item message: "+item.getMessage());
        Picasso.get().load(item.getAvatar()).placeholder(R.drawable.logo).into(holder.avatar);
    }

    @Override
    public int getItemCount() {

        Log.d("error","Adapter size: "+ list.size());

        return list.size();


    }

    static class News extends RecyclerView.ViewHolder {
        TextView name, message, date;
        ImageView avatar;
        News(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            message = itemView.findViewById(R.id.message);
            avatar = itemView.findViewById(R.id.avatar);
            date = itemView.findViewById(R.id.date);
        }
    }
}
