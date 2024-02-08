package com.example.pcstore.Detail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import com.example.pcstore.Cart.CartFragment;
import com.example.pcstore.Home.HomeFragment;
import com.example.pcstore.MainActivity;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import com.example.pcstore.UploadArticle.UploadArticleFragment;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class DetailProductFragment extends Fragment {
    //Elementos--------------------
    ImageButton buttonHome;
    ImageButton buttonCart;
    TextView amountInCartTV;
    ImageView imageArticle;
    TextView nameTv;
    TextView priceTv;
    TextView descritionTv;
    Button buttonAdd;
    Button buttonDelete;

    Article article;
    String sizeCart;

    ArrayList<Article> cartList = new ArrayList();
    ArrayList<Article> allArticles = new ArrayList();

    boolean ownerValidation = false;
    long idToDelete = 0;
    //-----------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_detail_product, container, false);

        article = new Article();
        //Elementos Id´s--------------------------
        buttonHome = view.findViewById(R.id.ButtonHome);
        buttonCart = view.findViewById(R.id.ButtonCart);
        amountInCartTV = view.findViewById(R.id.amountInCartTV);
        imageArticle = view.findViewById(R.id.imageArticle);
        nameTv = view.findViewById(R.id.nameTV);
        priceTv = view.findViewById(R.id.priceTV);
        descritionTv = view.findViewById(R.id.descriptionTV);
        buttonAdd = view.findViewById(R.id.ButtonAdd);
        buttonDelete = view.findViewById(R.id.ButtonDelete);
        //----------------------------------------

        //Click Listeners-------------------------
        buttonHome.setOnClickListener(this::onClickHome);
        buttonCart.setOnClickListener(this::onClickCart);
        buttonAdd.setOnClickListener(this::onClickAddCart);
        buttonDelete.setOnClickListener(this::onClickDeleteArticle);
        //----------------------------------------

        if (ownerValidation) {
            buttonDelete.setVisibility(View.VISIBLE);
        }

        chargeData();
        textButtonCart();

        return view;
    }

    //Metodos onClick------------------
    public void onClickCart(View view) {
        changeCart();
    }

    public void onClickHome(View view) {
        changeHome();
    }

    private void onClickDeleteArticle(View view) {

        Log.d("DeleteID", "ID DE ARTICULO A ELMINAR: " + idToDelete);

        /////////////////////////////////////
        FragmentActivity activity = getActivity();
        MainActivity mainActivity = (MainActivity) activity;
        assert mainActivity != null;
        mainActivity.receiveIdToBeDeleted(idToDelete);

        changeHome();
    }

    public void onClickAddCart(View view) {
        cartList.add(article);
        textButtonCart();
        showCustomToast();
    }
    //---------------------------------

    //Metodos funcionales--------------

    //Método que cambia a pantalla de Home
    public void changeHome() {
        HomeFragment fragment = new HomeFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment DetailProductFragment = fragmentManager.findFragmentByTag("DetailProductFragment");
        assert DetailProductFragment != null;
        transaction.remove(DetailProductFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "HomeFragment");
        transaction.commit();
    }

    //Método que cambia a pantalla de Cart
    public void changeCart() {
        CartFragment fragment = new CartFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment DetailProductFragment = fragmentManager.findFragmentByTag("DetailProductFragment");
        assert DetailProductFragment != null;
        transaction.remove(DetailProductFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "CartFragment");
        transaction.commit();
    }

    //Método para actualizar el texto de cantidad de productos en cesta
    public void textButtonCart() {
        sizeCart = String.valueOf(cartList.size());

        if (!sizeCart.equals("0")) {
            amountInCartTV.setText(sizeCart);
        } else {
            amountInCartTV.setText("");
        }
    }

    //Método para mostrar toast de articulo añadido al acrrito
    public void showCustomToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_cart_add, null);
        final Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Eétodo que carga los datos del producto a mostrar
    public void chargeData(){
        getParentFragmentManager().setFragmentResultListener("articleSend", this, new FragmentResultListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                idToDelete = result.getLong("id");
                String name = result.getString("nameArticle");
                Float price = result.getFloat("priceArticle");
                String image = result.getString("imageArticle");
                String description = result.getString("descriptionArticle");
                descritionTv.setText(description);
                nameTv.setText(name);
                priceTv.setText((float) price + "€");
                article.setName(name);
                article.setPrice(price);
                article.setImage(image);
                article.setDescription(description);
                Picasso.get()
                        .load(image)
                        .placeholder(R.drawable.background_categories)
                        .into(imageArticle);
            }
        });
    }

    //Método que recibe el ArrayList con los productos añadidos en el carrito
    public void recieveCartList(ArrayList cartList) {
        this.cartList = cartList;
    }

    //Método que recibe el ArrayList con todos los productos
    public void recieveAllArticles(ArrayList allArticles) {
        this.allArticles = allArticles;
    }

    //Método que recibe el nombre del usuario
    public void recieveOwnerValidation(boolean ownerValidation) {
        this.ownerValidation = ownerValidation;
    }
    //---------------------------------
}
