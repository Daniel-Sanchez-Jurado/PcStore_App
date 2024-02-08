package com.example.pcstore.Categorie;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final ItemClickListener mItemClickListener;
    ArrayList<Article> articlesCategoriesAdapter;

    public CategoriesAdapter(LayoutInflater inflater, ArrayList<Article> articlesCategoriesAdapter, ItemClickListener itemClickListener) {
        this.inflater = inflater;
        this.articlesCategoriesAdapter = articlesCategoriesAdapter;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_categories, parent, false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get()
                .load(articlesCategoriesAdapter.get(position).getImage())
                .placeholder(R.drawable.background_categories)
                .into(holder.imageArticle);
        holder.nameArticle.setText(articlesCategoriesAdapter.get(position).name);
        holder.priceArticle.setText((float) articlesCategoriesAdapter.get(position).price + "€");
        holder.itemView.setOnClickListener(view -> {
            mItemClickListener.onItemClick(articlesCategoriesAdapter.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return articlesCategoriesAdapter.size();
    }

    public interface ItemClickListener {
        void onItemClick(Article article);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArticle;
        TextView nameArticle;
        TextView priceArticle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArticle = itemView.findViewById(R.id.imageItem);
            nameArticle = itemView.findViewById(R.id.nameTV);
            priceArticle = itemView.findViewById(R.id.priceTV);
        }
    }
}

