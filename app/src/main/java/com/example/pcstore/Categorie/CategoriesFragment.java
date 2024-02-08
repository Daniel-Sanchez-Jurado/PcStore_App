package com.example.pcstore.Categorie;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.pcstore.Cart.CartFragment;
import com.example.pcstore.Detail.DetailProductFragment;
import com.example.pcstore.Home.HomeFragment;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import java.util.ArrayList;

public class CategoriesFragment extends Fragment {
    //Elementos--------------------
    ImageButton buttonHome;
    ImageButton buttonCart;
    TextView amountInCartTV;
    EditText searchET;
    Button buttonSearch;
    TextView textCategorie;
    RecyclerView recyclerView;
    String categorieName;
    String sizeCart;

    ArrayList<Article> articlesShow = new ArrayList();
    ArrayList<Article> cartList = new ArrayList();
    ArrayList<Article> searchList = new ArrayList();
    ArrayList<Article> allArticles = new ArrayList();

    CategoriesAdapter categoriesAdapter;

    boolean ownerValidation = false;
    //-----------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        //Elementos Id´s--------------------------
        buttonHome = view.findViewById(R.id.ButtonHome);
        buttonCart = view.findViewById(R.id.ButtonCart);
        amountInCartTV = view.findViewById(R.id.amountInCartTV);
        searchET = view.findViewById(R.id.searchET);
        buttonSearch = view.findViewById(R.id.ButtonSearch);
        textCategorie = view.findViewById(R.id.categorieTV);
        recyclerView = view.findViewById(R.id.recyclerView);
        //----------------------------------------

        //Click Listeners-------------------------
        buttonHome.setOnClickListener(this::onClickHome);
        buttonCart.setOnClickListener(this::onClickCart);
        buttonSearch.setOnClickListener(this::onClickSearch);
        //----------------------------------------

        // Carga de datos de nombre de categoría que mostrar
        textCategorie.setText(categorieName);

        //Adapter de las categorías-------------------------
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);

        categoriesAdapter = new CategoriesAdapter(LayoutInflater.from(getContext()), articlesShow, new CategoriesAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Log.d("articleClicked", "pulsaste:" + article.name);
                Bundle bundle = new Bundle();
                bundle.putLong("id", article.id);
                bundle.putString("nameArticle", article.name);
                bundle.putFloat("priceArticle", article.price);
                bundle.putString("imageArticle", article.image);
                bundle.putString("descriptionArticle", String.valueOf(article.description));
                getParentFragmentManager().setFragmentResult("articleSend", bundle);
                changeDetail();
            }
        });

        recyclerView.setAdapter(categoriesAdapter);
        //---------------------------------------------------

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

    private void onClickSearch(View view) {
        String categoriesNameSearch = (searchET.getText().toString()).toUpperCase();

        for (int i = 0; i < allArticles.size(); i++) {
            if ((allArticles.get(i).name).toUpperCase().contains(categoriesNameSearch) || (allArticles.get(i).categorie).toUpperCase().contains(categoriesNameSearch)) {
                searchList.add(allArticles.get(i));
            }
        }

        if (searchList.size() == 0 || categoriesNameSearch.equals("")) {
            showCustomToast();
        } else {
            updateCategories(searchList, searchET.getText().toString());
        }
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
        Fragment CategoriesFragment = fragmentManager.findFragmentByTag("CategoriesFragment");
        assert CategoriesFragment != null;
        transaction.remove(CategoriesFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que cambia a pantalla de Detail
    public void changeDetail() {
        DetailProductFragment fragment = new DetailProductFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment CategoriesFragment = fragmentManager.findFragmentByTag("CategoriesFragment");
        assert CategoriesFragment != null;
        transaction.remove(CategoriesFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "DetailProductFragment");
        transaction.addToBackStack(null);
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
        Fragment CategoriesFragment = fragmentManager.findFragmentByTag("CategoriesFragment");
        assert CategoriesFragment != null;
        transaction.remove(CategoriesFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "CartFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que actualiza la pantalla de Categorie
    public void updateCategories(ArrayList arraySend, String categoriesNameSend) {
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        fragment.receiveArticles(arraySend, categoriesNameSend);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment categorieFragment = fragmentManager.findFragmentByTag("CategoriesFragment");
        assert categorieFragment != null;
        transaction.remove(categorieFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "CategoriesFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método para actualizar el texto de cantidad de productos en cesta
    public void textButtonCart() {
        // Carga de datos de cantidad de productos
        sizeCart = String.valueOf(cartList.size());

        if (!sizeCart.equals("0")) {
            amountInCartTV.setText(sizeCart);
        } else {
            amountInCartTV.setText("");
        }
    }

    //Método para mostrar toast de texto no encontrado
    public void showCustomToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_search, null);
        final Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Método que recibe el ArrayList con los productos añadidos en el carrito
    public void recieveCartList(ArrayList cartList) {
        this.cartList = cartList;
    }

    //Método que recibe el ArrayList con todos los productos
    public void recieveAllArticles(ArrayList allArticles) {
        this.allArticles = allArticles;
    }

    //Metodo que recibe el ArrayList con los productos de la categoría a mostar
    public void receiveArticles(ArrayList articleReceived, String categorieName) {
        this.articlesShow = articleReceived;
        this.categorieName = categorieName;
    }

    //Método que recibe el nombre del usuario
    public void recieveOwnerValidation(boolean ownerValidation) {
        this.ownerValidation = ownerValidation;
    }
    //---------------------------------
}

