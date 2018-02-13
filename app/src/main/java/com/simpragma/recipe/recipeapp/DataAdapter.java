package com.simpragma.recipe.recipeapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Madvesha  on 2/1/18.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Recipe recipeResult;
    Context context;
    private ArrayList<ResultList> apiResultList;


    public DataAdapter(Context context, Recipe recipeResult, ArrayList<ResultList> arrylist) {
        this.recipeResult = recipeResult;
        this.context = context;
        apiResultList = arrylist;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, final int i) {

        holder.titleTextView.setText(apiResultList.get(i).getTitle());
        String recipeImage = apiResultList.get(i).getThumbnail();
        if (recipeImage.equals("") || recipeImage == null) {
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.recipeImageView);
        } else {
            Picasso.with(context).load(recipeImage).error(R.mipmap.ic_launcher).into(holder.recipeImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Log.d("DataAdapter","onClick");
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(apiResultList.get(i).getHref().toString().trim()));
                context.startActivity(browserIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return apiResultList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.titleTextview);
            recipeImageView = (ImageView) view.findViewById(R.id.recipeimage);
        }
    }

}
