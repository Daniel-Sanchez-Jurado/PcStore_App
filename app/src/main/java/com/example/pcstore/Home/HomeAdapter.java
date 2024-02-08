package com.example.pcstore.Home;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pcstore.Retrofit.Article;
import com.example.pcstore.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    ArrayList<Article> articlesHomeAdapter;
    private LayoutInflater inflater;
    private ItemClickListener mItemClickListener;

    public HomeAdapter(LayoutInflater inflater, ArrayList<Article> articlesHomeAdapter, ItemClickListener itemClickListener) {
        this.inflater = inflater;
        this.articlesHomeAdapter = articlesHomeAdapter;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_home, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nameArticle.setText(articlesHomeAdapter.get(position).name);
        holder.priceArticle.setText((float) articlesHomeAdapter.get(position).price + "€");
        Picasso.get()
                .load(articlesHomeAdapter.get(position).getImage())
                .placeholder(R.drawable.background_categories)
                .into(holder.imageArticle);
        holder.itemView.setOnClickListener(view -> {
            mItemClickListener.onItemClick(articlesHomeAdapter.get(position));// Esto obtendrá la posición de su elemento en RecyclerView
        });
    }

    @Override
    public int getItemCount() {
        return articlesHomeAdapter.size();
    }

    public interface ItemClickListener {
        void onItemClick(Article article);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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