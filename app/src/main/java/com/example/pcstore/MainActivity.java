package com.example.pcstore;

import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.pcstore.Login.LoginFragment;
import com.example.pcstore.Retrofit.RequestApi;
import com.example.pcstore.StartImage.StartImageFragment;
import com.example.pcstore.Retrofit.Article;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    private static String AUTH;
    OkHttpClient okHttpClient;
    RequestApi requestApi;
    Retrofit retrofit;
    LoginFragment loginFragment = new LoginFragment();

    String user = "";
    String password = "";

    ArrayList<Article> allArticles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    changeStartImage();
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    changeLogin();
                }
            }
        };
        thread.start();
    }
    //Metodos funcionales--------------

    //Método que cambia a pantalla de carga de app
    public void changeStartImage() {
        // Cargar nuevo fragment
        StartImageFragment fragment = new StartImageFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_layout, fragment, "StartImageFragment");
        transaction.commit();
    }

    //Método que cambia a pantalla de Login
    public void changeLogin() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        // Borrar fragment actual
        Fragment StartImageFragment = fragmentManager.findFragmentByTag("StartImageFragment");
        assert StartImageFragment != null;
        transaction
                .setCustomAnimations(FragmentTransaction.TRANSIT_NONE, R.anim.disappear)
                .remove(StartImageFragment);
        // Cargar nuevo fragment
        transaction
                .setCustomAnimations(R.anim.appear, FragmentTransaction.TRANSIT_NONE)
                .add(R.id.main_layout, loginFragment, "LoginFragment");
        transaction.commit();
    }

    //Método que agrega a la peticion el header con los parametros del usuario logueado
    public void addHeader() {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(1500, TimeUnit.MILLISECONDS)
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request request = chain.request();

                                Request.Builder requestBuilder = request.newBuilder()
                                        .addHeader("Authorization", AUTH)
                                        .method(request.method(), request.body());

                                Request requestFinal = requestBuilder.build();
                                return chain.proceed(requestFinal);
                            }
                        }
                ).build();
    }

    //Método que crea el objeto retrofit
    public void objectRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(RequestApi.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    //Método que recibe los datos de la API y rellena los ArrayList
    public void reciveDataAPI() {
        Call<ArrayList<Article>> articlesCall = requestApi.getArticles();
        articlesCall.enqueue(new Callback<ArrayList<Article>>() {
            @Override
            public void onResponse(Call<ArrayList<Article>> call, retrofit2.Response<ArrayList<Article>> response) {
                if (response.isSuccessful()) {
                    allArticles = response.body();
                    loginFragment.recieveAllArticles(allArticles);
                    for (int i = 0; i < allArticles.size(); i++) {
                        Article article = allArticles.get(i);
                        Log.d("MainActivity", "Los nombres son: " + article.id + " " + article.name + " " + article.price);
                    }
                } else {
                    Log.d("MainActivity", "FALLO on response " + response.errorBody() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Article>> call, Throwable t) {
                Log.d("MainActivityFailure", "FALLO " + t.getMessage());
                showCustomToast(R.layout.custom_toast_connection_api);
            }
        });
    }

    // Método que realiza la petición POST para agregar un nuevo artículo
    private void sendPostRequest(Article newArticle) {
        Call<Long> addArticleCall = requestApi.addArticle(newArticle);
        addArticleCall.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, retrofit2.Response<Long> response) {
                if (response.isSuccessful()) {
                    // Éxito al agregar el artículo
                    Long articleId = response.body();
                    Log.d("MainActivity", "Nuevo artículo agregado con éxito. ID: " + articleId);
                    // AGREGAR CUSTOM TOAST
                    showCustomToast(R.layout.custom_toast_successfully_uploaded);
                } else {
                    // Error al agregar el artículo
                    Log.d("MainActivity", "FALLO en onResponse al agregar el artículo " + response.errorBody() + " " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                // Fallo en la petición POST
                Log.d("MainActivityFailure", "FALLO al agregar el artículo " + t.getMessage());
            }
        });
    }

    // Método que realiza la petición DELETE para eliminar un artículo
    private void sendDeleteRequest(Long id) {
        Call<Response<ResponseBody>> deleteArticleCall = requestApi.deleteArticle(id);
        deleteArticleCall.enqueue(new Callback<Response<ResponseBody>>() {
            @Override
            public void onResponse(Call<Response<ResponseBody>> call, retrofit2.Response<Response<ResponseBody>> response) {
                if (response.isSuccessful()) {
                    // Éxito al eliminar el artículo
                    Log.d("MainActivity", "Artículo eliminado con éxito");
                    // AGREGAR CUSTOM TOAST
                    showCustomToast(R.layout.custom_toast_successfully_delete);
                } else {
                    // Error al eliminar el artículo
                    Log.d("MainActivity", "FALLO en onResponse al eliminar el artículo " + response.errorBody() + " " + response.code());

                    // Puedes agregar aquí lógica adicional para manejar mensajes de error no JSON válido si es necesario
                }
            }

            @Override
            public void onFailure(Call<Response<ResponseBody>> call, Throwable t) {
                // Fallo en la petición DELETE
                Log.d("MainActivityFailure", "FALLO al eliminar el artículo " + t.getMessage());

                // NO DEBERÍA ESTAR AQUI, SOLO EN onResponse ------ ARREGLAR EN UN FUTURO
                showCustomToast(R.layout.custom_toast_successfully_delete);
                /////////////////////////////////////////////////////////////////////////
            }
        });
    }


    // Método para mostrar un Toast personalizado en MainActivity
    private void showCustomToast(int customToast) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(customToast, null);
        final Toast toast = new Toast(MainActivity.this);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 500);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    // Método que recibe el usuario logeado y carga los datos de la API
    public void recieveLogin(String user, String password) {
        this.user = user;
        this.password = password;

        //Carga de datos
        AUTH = "Basic " + Base64.encodeToString((user + ":" + password).getBytes(), Base64.NO_WRAP);
        addHeader();
        objectRetrofit();
        requestApi = retrofit.create(RequestApi.class);
        reciveDataAPI();
    }

    // Método que recibe los datos del nuevo artículo
    public void recieveNewArticle(Article newArticle) {
        sendPostRequest(newArticle);
    }

    // Método que recibe el ID del artículo a eliminar
    public void receiveIdToBeDeleted(long id) {
        sendDeleteRequest(id);
    }

    //---------------------------------
}