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
 * Created by MadveshaK on 2/4/18.
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
        holder.textView.setText(dataBaseList.get(i).getTitle());
        String iamge = dataBaseList.get(i).getThumbnail();
        if (iamge.equals("") || iamge == null) {
            Picasso.with(context).load(R.mipmap.ic_launcher).into(holder.imageView);
        } else {
            Picasso.with(context).load(iamge).error(R.mipmap.ic_launcher).into(holder.imageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
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

        TextView textView;
        ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            textView = (TextView) view.findViewById(R.id.title_search);
            imageView = (ImageView) view.findViewById(R.id.recipeimage);
        }
    }

}
