package com.example.pcstore.Home;

import android.content.Intent;
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

import com.example.pcstore.Popup.InfoOwnerPopup;
import com.example.pcstore.Retrofit.Article;
import com.example.pcstore.Cart.CartFragment;
import com.example.pcstore.Categorie.CategoriesFragment;
import com.example.pcstore.Detail.DetailProductFragment;
import com.example.pcstore.R;
import com.example.pcstore.UploadArticle.UploadArticleFragment;

import java.util.ArrayList;

public class HomeFragment<articles> extends Fragment {
    //Elementos--------------------
    ImageButton buttonCart;
    TextView amountInCartTV;
    EditText searchET;
    Button buttonSearch;
    ImageButton buttonNewArticle;
    TextView textCategories1;
    RecyclerView recyclerView1;
    TextView textCategories2;
    RecyclerView recyclerView2;
    TextView textCategories3;
    RecyclerView recyclerView3;
    TextView textCategories4;
    RecyclerView recyclerView4;

    HomeAdapter homeAdapter1;
    HomeAdapter homeAdapter2;
    HomeAdapter homeAdapter3;
    HomeAdapter homeAdapter4;
    //-----------------------------

    //Variables--------------------
    String sizeCart;
    String ctgNameSend = "";
    String ctgNameSearch = "";

    ArrayList<Article> allArticles = new ArrayList();
    ArrayList<Article> catg1Articles = new ArrayList();
    ArrayList<Article> catg2Articles = new ArrayList();
    ArrayList<Article> catg3Articles = new ArrayList();
    ArrayList<Article> catg4Articles = new ArrayList();
    ArrayList<Article> articlesSend = new ArrayList();
    ArrayList<Article> cartList = new ArrayList();
    ArrayList<Article> searchList = new ArrayList();

    String user = "";
    boolean ownerValidation = false;
    //-----------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Elementos Id´s--------------------------
        buttonCart = view.findViewById(R.id.ButtonCart);
        amountInCartTV = view.findViewById(R.id.amountInCartTV);
        searchET = view.findViewById(R.id.searchET);
        buttonSearch = view.findViewById(R.id.ButtonSearch);
        buttonNewArticle = view.findViewById(R.id.ButtonHome);
        textCategories1 = view.findViewById(R.id.categorie1TV);
        recyclerView1 = view.findViewById(R.id.categorie1RV);
        textCategories2 = view.findViewById(R.id.categorie2TV);
        recyclerView2 = view.findViewById(R.id.categorie2RV);
        textCategories3 = view.findViewById(R.id.categorie3TV);
        recyclerView3 = view.findViewById(R.id.categorie3RV);
        textCategories4 = view.findViewById(R.id.categorie4TV);
        recyclerView4 = view.findViewById(R.id.categorie4RV);
        //----------------------------------------

        if (user.equals("owner")){
            ownerValidation = true;
        }

        if (ownerValidation){
            buttonNewArticle.setVisibility(View.VISIBLE);
        }

        //Adapter de las categorías-------------------------
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        layoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        layoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        layoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        layoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView3.setLayoutManager(layoutManager3);
        recyclerView4.setLayoutManager(layoutManager4);

        homeAdapter1 = new HomeAdapter(LayoutInflater.from(getContext()), catg1Articles, new HomeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Bundle bundle = new Bundle();
                bundle.putLong("id", article.id);
                bundle.putString("nameArticle", article.name);
                bundle.putFloat("priceArticle", article.price);
                bundle.putString("imageArticle", String.valueOf(article.image));
                bundle.putString("descriptionArticle", String.valueOf(article.description));
                getParentFragmentManager().setFragmentResult("articleSend", bundle);
                changeDetail();
            }
        });

        homeAdapter2 = new HomeAdapter(LayoutInflater.from(getContext()), catg2Articles, new HomeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Bundle bundle = new Bundle();
                bundle.putLong("id", article.id);
                bundle.putString("nameArticle", article.name);
                bundle.putFloat("priceArticle", article.price);
                bundle.putString("imageArticle", String.valueOf(article.image));
                bundle.putString("descriptionArticle", String.valueOf(article.description));
                getParentFragmentManager().setFragmentResult("articleSend", bundle);
                changeDetail();
            }
        });

        homeAdapter3 = new HomeAdapter(LayoutInflater.from(getContext()), catg3Articles, new HomeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Bundle bundle = new Bundle();
                bundle.putLong("id", article.id);
                bundle.putString("nameArticle", article.name);
                bundle.putFloat("priceArticle", article.price);
                bundle.putString("imageArticle", String.valueOf(article.image));
                bundle.putString("descriptionArticle", String.valueOf(article.description));
                getParentFragmentManager().setFragmentResult("articleSend", bundle);
                changeDetail();
            }
        });

        homeAdapter4 = new HomeAdapter(LayoutInflater.from(getContext()), catg4Articles, new HomeAdapter.ItemClickListener() {
            @Override
            public void onItemClick(Article article) {
                Bundle bundle = new Bundle();
                bundle.putLong("id", article.id);
                bundle.putString("nameArticle", article.name);
                bundle.putFloat("priceArticle", article.price);
                bundle.putString("imageArticle", String.valueOf(article.image));
                bundle.putString("descriptionArticle", String.valueOf(article.description));
                getParentFragmentManager().setFragmentResult("articleSend", bundle);
                changeDetail();
            }
        });

        recyclerView1.setAdapter(homeAdapter1);
        recyclerView2.setAdapter(homeAdapter2);
        recyclerView3.setAdapter(homeAdapter3);
        recyclerView4.setAdapter(homeAdapter4);
        //---------------------------------------------------

        //Click Listeners-------------------------
        buttonCart.setOnClickListener(this::onClickCart);
        buttonSearch.setOnClickListener(this::onClickSearch);
        buttonNewArticle.setOnClickListener(this::onClickNewArticle);
        textCategories1.setOnClickListener(this::onClickCategories1);
        textCategories2.setOnClickListener(this::onClickCategories2);
        textCategories3.setOnClickListener(this::onClickCategories3);
        textCategories4.setOnClickListener(this::onClickCategories4);
        //----------------------------------------

        textButtonCart();

        return view;
    }

    //Metodos onClick------------------
    public void onClickCart(View view) {
        changeCart();
    }

    private void onClickSearch(View view) {
        ctgNameSearch = (searchET.getText().toString()).toUpperCase();
        ctgNameSend = searchET.getText().toString();

        for (int i = 0; i < allArticles.size(); i++) {
            if ((allArticles.get(i).name).toUpperCase().contains(ctgNameSearch) || (allArticles.get(i).categorie).toUpperCase().contains(ctgNameSearch)) {
                searchList.add(allArticles.get(i));
            }
        }

        if (searchList.size() == 0 || ctgNameSearch.equals("")) {
            showCustomToast();
        } else {
            changeCategories(searchList);
        }
    }

    public void onClickNewArticle(View view) {
        changeUploadArticle();
    }

    private void onClickCategories1(View view) {
        ctgNameSend = textCategories1.getText().toString();

        for (int i = 0; i < allArticles.size(); i++) {
            if (allArticles.get(i).categorie.equals("Ratones")) {
                articlesSend.add(allArticles.get(i));
            }
        }
        changeCategories(catg1Articles);
    }

    private void onClickCategories2(View view) {
        ctgNameSend = textCategories2.getText().toString();

        for (int i = 0; i < allArticles.size(); i++) {
            if (allArticles.get(i).categorie.equals("Monitores")) {
                articlesSend.add(allArticles.get(i));
            }
        }
        changeCategories(catg2Articles);
    }

    private void onClickCategories3(View view) {
        ctgNameSend = textCategories3.getText().toString();

        for (int i = 0; i < allArticles.size(); i++) {
            if (allArticles.get(i).categorie.equals("Auriculares")) {
                articlesSend.add(allArticles.get(i));
            }
        }
        changeCategories(catg3Articles);
    }

    private void onClickCategories4(View view) {
        ctgNameSend = textCategories4.getText().toString();

        for (int i = 0; i < allArticles.size(); i++) {
            if (allArticles.get(i).categorie.equals("Teclados")) {
                articlesSend.add(allArticles.get(i));
            }
        }
        changeCategories(catg4Articles);
    }
    //---------------------------------

    //Metodos funcionales--------------

    //Método que cambia a pantalla de Detail
    public void changeDetail() {
        DetailProductFragment fragment = new DetailProductFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment HomeFragment = fragmentManager.findFragmentByTag("HomeFragment");
        assert HomeFragment != null;
        transaction.remove(HomeFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "DetailProductFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que cambia a pantalla de Categorie
    public void changeCategories(ArrayList arraySend) {
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        fragment.receiveArticles(arraySend, ctgNameSend);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment HomeFragment = fragmentManager.findFragmentByTag("HomeFragment");
        assert HomeFragment != null;
        transaction.remove(HomeFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "CategoriesFragment");
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
        Fragment HomeFragment = fragmentManager.findFragmentByTag("HomeFragment");
        assert HomeFragment != null;
        transaction.remove(HomeFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "CartFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que cambia a pantalla de Nuevo artículo
    public void changeUploadArticle() {
        UploadArticleFragment fragment = new UploadArticleFragment();
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment HomeFragment = fragmentManager.findFragmentByTag("HomeFragment");
        assert HomeFragment != null;
        transaction.remove(HomeFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "UploadArticleFragment");
        transaction.addToBackStack(null);
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

    //Método para mostrar toast
    public void showCustomToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast_search, null);
        final Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    //Método para rellenar los arrays de categorías
    public void categoryFill() {
        for (int i = 0; i < allArticles.size(); i++) {
            if (allArticles.get(i).categorie.equals("Ratones")) {
                catg1Articles.add(allArticles.get(i));
            } else if (allArticles.get(i).categorie.equals("Monitores")) {
                catg2Articles.add(allArticles.get(i));
            } else if (allArticles.get(i).categorie.equals("Auriculares")) {
                catg3Articles.add(allArticles.get(i));
            } else if (allArticles.get(i).categorie.equals("Teclados")) {
                catg4Articles.add(allArticles.get(i));
            }
        }
    }

    //Método que recibe el ArrayList con los productos añadidos en el carrito
    public void recieveCartList(ArrayList cartList) {
        this.cartList = cartList;
    }

    //Método que recibe el ArrayList con todos los productos
    public void recieveAllArticles(ArrayList allArticles) {
        this.allArticles = allArticles;
        categoryFill();
    }

    //Método que recibe el nombre del usuario
    public void recieveUser(String user) {
        this.user = user;//ARREGLAR CON OTRO RECIEVE ownerValidation
    }

    //Método que recibe el bboleano de validación de propietario
    public void recieveOwnerValidation(boolean ownerValidation) {
        this.ownerValidation = ownerValidation;
    }

    //---------------------------------
}
