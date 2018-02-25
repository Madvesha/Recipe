package com.simpragma.recipe.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpragma.recipe.recipeapp.R;
import com.simpragma.recipe.roomDatabase.RecipeRoomEntityClass;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Madvesha on 2/12/18.
 */

public class RoomViewAdapter extends RecyclerView.Adapter<RoomViewAdapter.ViewHolder> {

    Context context;
    private List<RecipeRoomEntityClass> recipeRoomDBList;


    public RoomViewAdapter(Context context, List<RecipeRoomEntityClass> recipeRoomDBList) {
        this.recipeRoomDBList = recipeRoomDBList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_row, viewGroup,
                false);
        return new RoomViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        holder.titleTextView.setText(recipeRoomDBList.get(position).getTitle().trim());

        Picasso.with(context).load(recipeRoomDBList.get(position).getThumbnail())
                .placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.recipeImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open another activity on item click
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(recipeRoomDBList.get(position).getHref().toString().trim()));
                context.startActivity(browserIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeRoomDBList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.tv_title);
            recipeImageView = (ImageView) view.findViewById(R.id.img_recipe);
        }
    }
}
