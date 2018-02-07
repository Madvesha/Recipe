package com.simpragma.recipe.recipeapplication1;

import android.content.Context;
import android.content.Intent;
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
 * Created by shubhank on 2/4/18.
 */

public class DataBaseAdapter extends RecyclerView.Adapter<DataBaseAdapter.ViewHolder> {
    ArrayList<DataBaseResult> dataBaseResults;
    Context context;

    public DataBaseAdapter(Context context, ArrayList<DataBaseResult> dataBaseResults) {
        this.context=context;
        this.dataBaseResults=dataBaseResults;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int i) {

        holder.textView.setText(dataBaseResults.get(i).getTitle());
        String str = dataBaseResults.get(i).getIngredients();
        String iamge = dataBaseResults.get(i).getThumbnail();
        if (iamge.equals("") || iamge == null) {

            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.imageView);

        } else {
            Picasso.with(context).load(iamge).error(R.mipmap.ic_launcher).into(holder.imageView);

        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent intent = new Intent(context, RecipeInformation.class);
                intent.putExtra("image", dataBaseResults.get(i).getThumbnail());
                intent.putExtra("title",dataBaseResults.get(i).getTitle());
                intent.putExtra("ingredients",dataBaseResults.get(i).getIngredients());
                intent.putExtra("href",dataBaseResults.get(i).getHref());
                // put image data in Intent
                context.startActivity(intent); // start Intent
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataBaseResults.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.title_search);
            imageView = (ImageView) view.findViewById(R.id.recipeimage);

        }
    }

}
