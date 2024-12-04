package com.example.cdol3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InicoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_inico);

    }

    public void agregarProducto(View view){
        Intent agrgarproducto = new Intent(this, AgregarActivity.class);
        startActivity(agrgarproducto );

    }

    public void editarProducto(View view){
        Intent editarProducto = new Intent(this, GestionProductosActivity.class);
        startActivity(editarProducto );

    }
    public void listap(View view){
        Intent listap = new Intent(this, TablaProductosActivity.class);
        startActivity(listap );

    }


}