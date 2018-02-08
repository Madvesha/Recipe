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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by shubhank on 2/1/18.
 */

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    Recipie recipeResult;
    Context context;
    private ArrayList<ResultList> apiResultList;


    public DataAdapter(Context context, Recipie recipeResult, ArrayList<ResultList> arrylist) {
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

        holder.textView.setText(apiResultList.get(i).getTitle());
        String image = apiResultList.get(i).getThumbnail();
        if (image.equals("") || image == null) {
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.imageView);
        } else {
            Picasso.with(context).load(image).error(R.mipmap.ic_launcher).into(holder.imageView);
        }
            /*Picasso.with(context).load(results.get(i).getThumbnail())
                    .placeholder(R.drawable.ic_search).into(holder.imageView);*/

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
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

        TextView textView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.title_search);
            imageView = (ImageView) view.findViewById(R.id.recipeimage);
        }
    }

}
