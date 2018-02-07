package com.simpragma.recipe.recipeapplication1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
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
    ArrayList<Result> results;
    private ArrayList<Result> mArrayList;
    private ArrayList<Result> mFilteredList;
    ArrayList<Result> dataBaseResult;
//     private List<Result> worldpopulationlist = null;
//    private ArrayList<Result> arraylist;

    public DataAdapter(Context context, Recipie recipeResult, ArrayList<Result> arrylist) {
        this.recipeResult = recipeResult;
        this.context=context;
        mArrayList = arrylist;
        mFilteredList = arrylist;

    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, final int i) {

            holder.textView.setText(mFilteredList.get(i).getTitle());
            String str = mFilteredList.get(i).getIngredients();
            String image = mFilteredList.get(i).getThumbnail();
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
//                Intent intent = new Intent(context, RecipeInformation.class);
//                intent.putExtra("image", mFilteredList.get(i).getThumbnail());
//                intent.putExtra("title",mFilteredList.get(i).getTitle());
//                intent.putExtra("ingredients",mFilteredList.get(i).getIngredients());
//                intent.putExtra("href",mFilteredList.get(i).getHref());
//
//                // put image data in Intent
//                context.startActivity(intent); // start Intent
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(mFilteredList.get(i).getHref().toString().trim()));
                context.startActivity(browserIntent);

            }
        });
    }

    @Override
    public int getItemCount() {
       return mFilteredList.size();    }


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
