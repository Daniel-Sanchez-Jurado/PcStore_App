package com.example.pcstore.UploadArticle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.pcstore.Home.HomeFragment;
import com.example.pcstore.MainActivity;
import com.example.pcstore.R;
import com.example.pcstore.Retrofit.Article;

import java.util.ArrayList;

public class UploadArticleFragment extends Fragment {
    //Elementos--------------------
    ImageButton buttonHome;
    EditText urlET;
    EditText nameET;
    EditText priceET;
    EditText descriptionET;
    AutoCompleteTextView auto_complete_txt;
    ArrayAdapter<String> adapterItems;
    Button buttonUpload;
    //-----------------------------

    //Variables--------------------
    ArrayList<Article> allArticles = new ArrayList();
    String[] items;
    String item;
    boolean ownerValidation = false;
    //-----------------------------

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Cargar vista con el fragment
        View view = inflater.inflate(R.layout.fragment_upload_article, container, false);

        //Elementos Id´s--------------------------
        buttonHome = view.findViewById(R.id.ButtonHome);
        urlET = view.findViewById(R.id.urlET);
        nameET = view.findViewById(R.id.nameET);
        priceET = view.findViewById(R.id.priceET);
        descriptionET = view.findViewById(R.id.descriptionET);
        auto_complete_txt = view.findViewById(R.id.auto_complete_txt);
        buttonUpload = view.findViewById(R.id.ButtonUpload);
        //----------------------------------------

        //Adaptador desplegable-------------------------
        items = new String[]{
                getResources().getString(R.string.mice),
                getResources().getString(R.string.keyboards),
                getResources().getString(R.string.monitors),
                getResources().getString(R.string.headphones)
        };
        adapterItems = new ArrayAdapter<>(requireContext(), R.layout.list_item, items);
        auto_complete_txt.setAdapter(adapterItems);

        auto_complete_txt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                item = parent.getItemAtPosition(position).toString();
                //Toast.makeText(requireContext(),"Item: "+item,Toast.LENGTH_SHORT).show();
            }
        });
        //----------------------------------------

        //Click Listeners-------------------------
        buttonHome.setOnClickListener(this::onClickHome);
        buttonUpload.setOnClickListener(this::onClickUpload);
        //----------------------------------------

        return view;
    }

    private void onClickHome(View view) {
        changeHome();
    }

    //Método que cambia a pantalla de Home
    public void changeHome() {
        HomeFragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        fragment.recieveAllArticles(allArticles);
        fragment.recieveOwnerValidation(ownerValidation);
        // Borrar fragment actual
        Fragment UploadArticleFragment = fragmentManager.findFragmentByTag("UploadArticleFragment");
        assert UploadArticleFragment != null;
        transaction.remove(UploadArticleFragment);
        // Cargar nuevo fragment
        transaction.add(R.id.main_layout, fragment, "HomeFragment");
        transaction.addToBackStack(null);
        transaction.commit();
    }

    //Método que envia los datos del nuevo artículo
    public void onClickUpload(View view) {

        String dataError = getResources().getString(R.string.toastErrorUpload);
        String image = urlET.getText().toString();
        String name = nameET.getText().toString();
        String price = priceET.getText().toString();
        String description = descriptionET.getText().toString();

        Article NewArticle = new Article();

        if (image.equals("")){
            Toast.makeText(requireContext(),dataError,Toast.LENGTH_SHORT).show();
        } else if (name.equals("")) {
            Toast.makeText(requireContext(),dataError,Toast.LENGTH_SHORT).show();
        } else if (price.equals("")){
            Toast.makeText(requireContext(),dataError,Toast.LENGTH_SHORT).show();
        } else if (description.equals("")){
            Toast.makeText(requireContext(),dataError,Toast.LENGTH_SHORT).show();
        } else if (!containsItem(items, item)) {
            Toast.makeText(requireContext(),dataError,Toast.LENGTH_SHORT).show();
        } else {
            NewArticle.image = image;
            NewArticle.name = name;
            NewArticle.price = Float.parseFloat(price);
            NewArticle.description = description;

            if (item.equals(getResources().getString(R.string.mice))){
                NewArticle.category = "Ratones";
            } else if (item.equals(getResources().getString(R.string.keyboards))) {
                NewArticle.category = "Teclados";
            } else if (item.equals(getResources().getString(R.string.monitors))){
                NewArticle.category = "Monitores";
            } else if (item.equals(getResources().getString(R.string.headphones))) {
                NewArticle.category = "Auriculares";
            }

            FragmentActivity activity = getActivity();
            MainActivity mainActivity = (MainActivity) activity;
            assert mainActivity != null;
            mainActivity.recieveNewArticle(NewArticle);

            changeHome();
        }
    }

    //Método para verificar si un elemento está en un array de strings
    private static boolean containsItem(String[] array, String item) {
        for (String element : array) {
            if (element.equals(item)) {
                return true;
            }
        }
        return false;
    }

    //Método que recibe el ArrayList con todos los productos
    public void recieveAllArticles(ArrayList allArticles) {
        this.allArticles = allArticles;
    }

    //Método que recibe el nombre del usuario
    public void recieveOwnerValidation(boolean ownerValidation) {
        this.ownerValidation = ownerValidation;
    }
}


