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
import com.simpragma.recipe.pojoclass.RecipePojoClass;
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
    private ArrayList<RecipePojoClass> apiResultList;
    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public DataAdapter(Context context, Recipe recipeResult, ArrayList<RecipePojoClass> arrylist) {
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
                View viewHolderLoading = inflater.inflate(R.layout.item_progress, viewGroup, false);
                viewHolder = new LoadingViewHolder(viewHolderLoading);
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
                Picasso.with(context).load(apiResultList.get(i).getThumbnail()).fit().centerCrop()
                        .placeholder(R.mipmap.ic_launcher).into(recipeVH.recipeImageView);
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
        View recyclerViewHolder = inflater.inflate(R.layout.card_row, parent, false);
        viewHolder = new ViewHolder(recyclerViewHolder);
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


    private static class ViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView recipeImageView;

        public ViewHolder(View view) {
            super(view);
            titleTextView = (TextView) view.findViewById(R.id.tv_title);
            recipeImageView = (ImageView) view.findViewById(R.id.img_recipe);
        }
    }


    private class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(RecipePojoClass addRecipeList) {
        apiResultList.add(addRecipeList);
        notifyItemInserted(apiResultList.size() - 1);
    }

    public void addAll(List<RecipePojoClass> recipeResults) {
        for (RecipePojoClass result : recipeResults) {
            add(result);
        }
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new RecipePojoClass());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = apiResultList.size() - 1;
        RecipePojoClass pojoClassresult = getItem(position);

        if (pojoClassresult != null) {
            apiResultList.remove(position);
        }
    }

    public RecipePojoClass getItem(int position) {
        return apiResultList.get(position);
    }
}
