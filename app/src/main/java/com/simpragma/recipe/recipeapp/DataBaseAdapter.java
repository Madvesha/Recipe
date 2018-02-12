package com.simpragma.recipe.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpragma.recipe.roomDatabase.RecipeRoomDB;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madvesha on 2/4/18.
 */

public class DataBaseAdapter extends RecyclerView.Adapter<DataBaseAdapter.ViewHolder> {
    ArrayList<ResultList> dataBaseList;
    Context context;

    public DataBaseAdapter(Context context, ArrayList<ResultList> dataBaseList) {
        this.context = context;
        this.dataBaseList = dataBaseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {
        holder.titleTextView.setText(dataBaseList.get(i).getTitle());
        String recipeIamge = dataBaseList.get(i).getThumbnail();
        if (recipeIamge.equals("") || recipeIamge == null) {
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.recipeImageView);
        } else {
            Picasso.with(context).load(recipeIamge).error(R.mipmap.ic_launcher).into(holder.recipeImageView);
        }

        holder.recipeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(dataBaseList.get(i).getHref().toString().trim()));
                context.startActivity(browserIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBaseList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.titleTextview);
            recipeImageView = (ImageView) view.findViewById(R.id.recipeimage);
        }
    }

}
