package com.example.pcstore.Cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.pcstore.Home.HomeFragment;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import java.util.ArrayList;

public class OrderCheckFragment extends Fragment {

    ImageButton buttonHome;
    ImageButton buttonExit;
    ArrayList<Article> allArticles = new ArrayList();

    boolean ownerValidation = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_order_check, container, false);

        //Elementos Id´s--------------------------
        buttonHome = view.findViewById(R.id.ButtonHome);
        buttonExit = view.findViewById(R.id.ButtonExit);
        //----------------------------------------

        //Click Listeners-------------------------
        buttonHome.setOnClickListener(this::onClickHome);
        buttonExit.setOnClickListener(this::onClickExit);
        //----------------------------------------

        return view;
    }

    //Metodos onClick------------------
    public void onClickHome(View view) {
        changeHome();
    }

    public void onClickExit(View view) {
        System.exit(0);
    }
    //---------------------------------

    ///Metodos funcionales-------------
    public void changeHome() {
        HomeFragment fragment = new HomeFragment();
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment OrderCheckFragment = fragmentManager.findFragmentByTag("OrderCheckFragment");
        assert OrderCheckFragment != null;
        transaction.remove(OrderCheckFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
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


