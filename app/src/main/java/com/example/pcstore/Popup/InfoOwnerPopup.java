package com.example.pcstore.Popup;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;

import com.example.pcstore.R;

public class InfoOwnerPopup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_info_owner);

        DisplayMetrics sizeWindow = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(sizeWindow);

        int width = sizeWindow.widthPixels;
        int height = sizeWindow.heightPixels;

        getWindow().setLayout((int)(width * 0.8), (int)(height * 0.45));

        // Cierra la actividad después de 5 segundos
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish(); // Cierra la actividad
            }
        }, 5000);
    }
}