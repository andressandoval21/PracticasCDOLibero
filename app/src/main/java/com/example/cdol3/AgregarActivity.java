package com.example.cdol3;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.cdol3.model.Articulos;
import com.google.firebase.FirebaseApp;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;


public class AgregarActivity extends AppCompatActivity {
    private FirebaseFirestore db;

    String[] colegio2 = {"Lozano", "himalaya","Maria Auxiliadora","Santander","Rosario","San Francisco","Sueños Maravillosos","San José","Normal de Pesca"
    ,"Manuel Aya","Los Periquillos","Humanos Competentes","Promoción Social"};
    String[] categoria2 = {"Camisa", "Pantalon","Chaqueta","Pantaloneta","Camibusos","Sudadera","Pantalon Azul"};
    String[] tallas2 = {"2","4","6","8","10","12","14","16","S","M","L","XL"};


    AutoCompleteTextView colegio;
    AutoCompleteTextView categoria;

    AutoCompleteTextView talla;
    Button limpiar;
    Button guardar;

    EditText cantidad;


    ////////////////////////////////////////////////////////
    ArrayAdapter<String> listaC;
    ArrayAdapter<String> categoriac;
    ArrayAdapter<String> tallac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
        colegio = findViewById(R.id.colegio);
        categoria = findViewById(R.id.categoria);
        talla = findViewById(R.id.talla);
        limpiar = findViewById(R.id.limpiar);
        guardar = findViewById(R.id.guardar);
        cantidad = findViewById(R.id.cantidad);

        db = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);

        listaC = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, colegio2);
        categoriac = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, categoria2);
        tallac = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, tallas2);

        colegio.setAdapter(listaC);
        categoria.setAdapter(categoriac);
        talla.setAdapter(tallac);


        colegio.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedColegio = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Colegio: " + selectedColegio, Toast.LENGTH_SHORT).show();
            }
        });

        categoria.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategoria = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Categoría: " + selectedCategoria, Toast.LENGTH_SHORT).show();
            }
        });


        talla.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedTalla = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), "Talla: " + selectedTalla, Toast.LENGTH_SHORT).show();
            }
        });

        cantidad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción a realizar
                String selectedCantidad = cantidad.getText().toString();
                Toast.makeText(getApplicationContext(), "Cantidad: " + selectedCantidad, Toast.LENGTH_SHORT).show();
            }
        });

        limpiar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colegio.setText("");  // Limpiar colegio
                categoria.setText(""); // Limpiar categoría
                talla.setText("");     // Limpiar talla
                cantidad.setText("");     // Limpiar cantidad

                Toast.makeText(getApplicationContext(), "AGREGAR NUEVO PRODUCTO", Toast.LENGTH_SHORT).show();
            }
        });

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCamposLlenos()) {
                    // Crear el objeto Articulos
                    Articulos articulo = new Articulos(
                            colegio.getText().toString(),
                            categoria.getText().toString(),
                            talla.getText().toString(),
                            Integer.parseInt(cantidad.getText().toString())
                    );

                    // Guardar en Firestore
                    db.collection("Articulos")
                            .add(articulo)
                            .addOnSuccessListener(documentReference -> {
                                Toast.makeText(getApplicationContext(), "Producto guardado: ID " + documentReference.getId(), Toast.LENGTH_SHORT).show();

                                // Limpiar campos
                                colegio.setText("");
                                categoria.setText("");
                                talla.setText("");
                                cantidad.setText("");
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(getApplicationContext(), "Error al guardar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                e.printStackTrace();  // Log para depuración
                            });
                }
            }
        });


    }


    public void volver(View view) {
        Intent volver = new Intent(this, InicoActivity.class);
        startActivity(volver);
    }
    private boolean validarCamposLlenos() {
        if (colegio.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona un colegio", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (categoria.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una categoría", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (talla.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una talla", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (cantidad.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor, selecciona una cantidad", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}





