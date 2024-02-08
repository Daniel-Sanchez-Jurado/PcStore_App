package com.example.pcstore.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.pcstore.Home.HomeFragment;
import com.example.pcstore.MainActivity;
import com.example.pcstore.Popup.InfoOwnerPopup;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;
import java.util.ArrayList;

public class LoginFragment extends Fragment {
    //Elementos--------------------
    EditText usernameET;
    EditText passwordET;
    Button buttonLogin;

    boolean validateUser;
    String username;
    String password;

    ArrayList<Article> allArticles = new ArrayList();
    String[] users = {"admin", "owner", "user", "dani", "javi", "david"};
    //-----------------------------

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        //Elementos Id´s--------------------------
        usernameET = view.findViewById(R.id.usernameET);
        passwordET = view.findViewById(R.id.passwordET);
        buttonLogin = view.findViewById(R.id.ButtonLogin);
        //----------------------------------------

        //Click Listeners-------------------------
        buttonLogin.setOnClickListener(this::onClickLogin);
        //----------------------------------------

        return view;
    }

    //Metodos onClick------------------
    public void onClickLogin(View view) {
        username = (usernameET.getText().toString()).toLowerCase();
        password = (passwordET.getText().toString()).toLowerCase();

        validateUser();
        checkoutLogin(view);
    }
    //---------------------------------

    //Metodos funcionales--------------

    //Método que cambia a pantalla de Home
    public void changeHome(ArrayList allArticles) {
        if (username.equals("owner") && password.equals("owner")){
            startActivity(new Intent(getActivity(), InfoOwnerPopup.class));
        }

        HomeFragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment.recieveAllArticles(allArticles);
        fragment.recieveUser(username);
        // Borrar fragment actual
        Fragment LoginFragment = fragmentManager.findFragmentByTag("LoginFragment");
        assert LoginFragment != null;
        transaction.remove(LoginFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que comprueba que los campos estén rellenos y la validación del usuario
    public void checkoutLogin(View view) {
        if (usernameET.getText().toString().isEmpty()) {
            showCustomToast(R.layout.custom_toast_empty_user);
        } else if (passwordET.getText().toString().isEmpty()) {
            showCustomToast(R.layout.custom_toast_empty_password);
        } else if (validateUser) {
            validateUser = false;
            FragmentActivity activity = getActivity();
            MainActivity mainActivity = (MainActivity) activity;
            assert mainActivity != null;
            mainActivity.recieveLogin(username, password);
        } else {
            showCustomToast(R.layout.custom_toast_wrong_login);
        }
    }

    //Método que valida el usuario
    public void validateUser() {
        for (String user : users) {
            if (username.equals(user) && password.equals(user)) {
                validateUser = true;
            }
        }
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

    //Método que recibe el ArrayList con todos los productos
    public void recieveAllArticles(ArrayList allArticles) {
        this.allArticles = allArticles;
        changeHome(allArticles);
        validateUser = false;
    }
    //---------------------------------
}

