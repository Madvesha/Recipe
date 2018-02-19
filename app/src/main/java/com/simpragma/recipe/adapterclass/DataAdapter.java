package com.simpragma.recipe.adapterclass;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.simpragma.recipe.pojoclass.Recipe;
import com.simpragma.recipe.pojoclass.RecipeList;
import com.simpragma.recipe.recipeapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madvesha  on 2/1/18.
 */

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Recipe recipeResult;
    Context context;
    private ArrayList<RecipeList> apiResultList;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public DataAdapter(Context context, Recipe recipeResult, ArrayList<RecipeList> arrylist) {
        this.recipeResult = recipeResult;
        this.context = context;
        apiResultList = arrylist;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(viewGroup, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingViewHolder(v2);
                break;
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int i) {

        switch (getItemViewType(i)) {

            case ITEM:
                final ViewHolder recipeVH = (ViewHolder) holder;
                Log.d("DATAADAPTER", apiResultList.get(i).getTitle().trim());
                recipeVH.titleTextView.setText(apiResultList.get(i).getTitle().trim());
                Picasso.with(context).load(apiResultList.get(i).getThumbnail())
                        .placeholder(R.mipmap.ic_launcher).resize(85, 85).into(recipeVH.recipeImageView);
                break;
            case LOADING:
//               Do nothing
                break;
        }

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


    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.card_row, parent, false);
        viewHolder = new ViewHolder(v1);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return apiResultList == null ? 0 : apiResultList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == apiResultList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.tv_title);
            recipeImageView = (ImageView) view.findViewById(R.id.img_recipe);
        }
    }


    protected class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(RecipeList r) {
        apiResultList.add(r);
        notifyItemInserted(apiResultList.size() - 1);
    }

    public void addAll(List<RecipeList> moveResults) {
        for (RecipeList result : moveResults) {
            add(result);
        }
    }

    public void remove(RecipeList r) {
        int position = apiResultList.indexOf(r);
        if (position > -1) {
            apiResultList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, apiResultList.size());
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new RecipeList());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = apiResultList.size() - 1;
        RecipeList result = getItem(position);

        if (result != null) {
            apiResultList.remove(position);
        }
    }

    public RecipeList getItem(int position) {
        return apiResultList.get(position);
    }


}
