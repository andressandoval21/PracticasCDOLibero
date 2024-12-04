package com.example.cdol3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cdol3.model.Articulos;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TablaProductosActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private TableLayout tablaProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_productos);

        db = FirebaseFirestore.getInstance();
        tablaProductos = findViewById(R.id.tablaProductos);

        cargarProductos();
    }

    private void cargarProductos() {
        db.collection("Articulos").get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Articulos articulo = document.toObject(Articulos.class);
                        agregarFilaATabla(articulo);
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                );
    }

    private void agregarFilaATabla(Articulos articulo) {
        TableRow fila = new TableRow(this);

        TextView txtColegio = new TextView(this);
        txtColegio.setText(articulo.getColegio());
        txtColegio.setPadding(8, 8, 8, 8);
        txtColegio.setGravity(android.view.Gravity.CENTER);

        TextView txtCategoria = new TextView(this);
        txtCategoria.setText(articulo.getCategoria());
        txtCategoria.setPadding(8, 8, 8, 8);
        txtCategoria.setGravity(android.view.Gravity.CENTER);

        TextView txtTalla = new TextView(this);
        txtTalla.setText(articulo.getTalla());
        txtTalla.setPadding(8, 8, 8, 8);
        txtTalla.setGravity(android.view.Gravity.CENTER);

        TextView txtCantidad = new TextView(this);
        txtCantidad.setText(String.valueOf(articulo.getCantidad()));
        txtCantidad.setPadding(8, 8, 8, 8);
        txtCantidad.setGravity(android.view.Gravity.CENTER);

        fila.addView(txtColegio);
        fila.addView(txtCategoria);
        fila.addView(txtTalla);
        fila.addView(txtCantidad);

        tablaProductos.addView(fila);
    }
    public void devuelta(View view){
        Intent devuelta = new Intent(this, InicoActivity.class);
        startActivity(devuelta );

    }
}
