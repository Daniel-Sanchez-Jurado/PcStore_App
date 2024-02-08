package com.example.pcstore.Cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.pcstore.Home.HomeFragment;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class CartFragment extends Fragment {
    //Elementos--------------------
    ImageButton buttonHome;
    TextView textCart;
    Button buttonEmptyCart;
    RecyclerView recyclerView;
    TextView textDeletedArticles;
    TextView textTotal;
    TextView textMount;
    Button buttonFinishOrder;
    CartAdapter cartAdapter;
    ArrayList<Article> cartList = new ArrayList();
    ArrayList<Article> allArticles = new ArrayList();
    //Formato de solo dos decimales
    DecimalFormat twoDecimalFormat = new DecimalFormat("#.##");
    boolean ownerValidation = false;
    //-----------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        //Elementos Id´s--------------------------
        buttonHome = view.findViewById(R.id.ButtonHome);
        textCart = view.findViewById(R.id.cartTV);
        buttonEmptyCart = view.findViewById(R.id.ButtonEmptyCart);
        recyclerView = view.findViewById(R.id.recyclerView);
        textDeletedArticles = view.findViewById(R.id.deletedArticlesTV);
        textTotal = view.findViewById(R.id.totalTV);
        textMount = view.findViewById(R.id.mountTV);
        buttonFinishOrder = view.findViewById(R.id.ButtonFinishOrder);
        //----------------------------------------

        //Click Listeners-------------------------
        buttonHome.setOnClickListener(this::onClickHome);
        buttonEmptyCart.setOnClickListener(this::onClickEmptyCart);
        buttonFinishOrder.setOnClickListener(this::onClickEndOrder);
        //----------------------------------------

        //Adapter de la lista del carro
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        cartAdapter = new CartAdapter(LayoutInflater.from(getContext()), cartList, new CartAdapter.ItemClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onItemClick(int position) {
                if (cartList.size() <= 1) {
                    cartDelete();
                } else {
                    cartList.remove(position);
                    totalPrice();
                    cartAdapter.notifyDataSetChanged();
                }
                showCustomToast(R.layout.custom_toast_article_delete);
                recyclerView.setAdapter(cartAdapter);
            }
        });

        recyclerView.setAdapter(cartAdapter);
        //----------------------------------------

        if (cartList.size() == 0) {
            buttonEmptyCart.setEnabled(false);
            buttonFinishOrder.setEnabled(false);
        }

        //Actualiza el total del carrito al cargar con los articulos que tiene
        totalPrice();

        return view;
    }

    //Metodos onClick------------------
    public void onClickHome(View view) {
        changeHome();
    }

    public void onClickEmptyCart(View view) {
        if (cartList.size() != 0) {
            cartDelete();
            showCustomToast(R.layout.custom_toast_cart_delete);
        }
    }

    public void onClickEndOrder(View view) {
        changeOrderCheck();
    }
    //---------------------------------

    ///Metodos funcionales-------------

    //Método que cambia a pantalla de Home
    public void changeHome() {
        HomeFragment fragment = new HomeFragment();
        fragment.recieveCartList(cartList);
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment CartFragment = fragmentManager.findFragmentByTag("CartFragment");
        assert CartFragment != null;
        transaction.remove(CartFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que cambia a pantalla de OrderCheck
    public void changeOrderCheck() {
        OrderCheckFragment fragment = new OrderCheckFragment();
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment CartFragment = fragmentManager.findFragmentByTag("CartFragment");
        assert CartFragment != null;
        transaction.remove(CartFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "OrderCheckFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que vacia el carro y deshabilita los botones
    public void cartDelete() {
        cartList.clear();
        textMount.setText("0€");
        buttonEmptyCart.setEnabled(false);
        buttonFinishOrder.setEnabled(false);
        recyclerView.setVisibility(View.GONE);
        textDeletedArticles.setVisibility(View.VISIBLE);
    }

    //Método que actualiza el texto de precio total
    @SuppressLint("SetTextI18n")
    public void totalPrice() {
        float total = 0;
        for (int i = 0; i < cartList.size(); i++) {
            total += cartList.get(i).price;
        }
        textMount.setText(twoDecimalFormat.format(total) + "€");
    }

    //Método para mostrar toast
    public void showCustomToast(int customToast) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(customToast, null);
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

    //Método que recibe el nombre del usuario
    public void recieveOwnerValidation(boolean ownerValidation) {
        this.ownerValidation = ownerValidation;
    }
    //---------------------------------
}
