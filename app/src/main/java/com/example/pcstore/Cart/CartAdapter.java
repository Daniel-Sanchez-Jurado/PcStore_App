package com.example.pcstore.Cart;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ItemClickListener itemClickListener;
    ArrayList<Article> cartItem;

    public CartAdapter(LayoutInflater inflater, ArrayList<Article> cartItem, ItemClickListener itemClickListener) {
        this.inflater = inflater;
        this.cartItem = cartItem;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_cart, parent, false);
        return new CartAdapter.ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String image = cartItem.get(position).getImage();

        Picasso.get()
                .load(image)
                .placeholder(R.drawable.background_categories)
                .into(holder.imageArticle);
        holder.nameArticle.setText(cartItem.get(position).name);
        holder.priceArticle.setText((float) cartItem.get(position).price + "€");
        holder.delete.setOnClickListener(view -> {
            itemClickListener.onItemClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return cartItem.size();
    }

    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageArticle;
        public TextView nameArticle;
        public TextView priceArticle;
        public ImageButton delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArticle = itemView.findViewById(R.id.imageItem);
            nameArticle = itemView.findViewById(R.id.nameTV);
            priceArticle = itemView.findViewById(R.id.priceTV);
            delete = itemView.findViewById(R.id.ButtonDelete);
        }
    }
}
