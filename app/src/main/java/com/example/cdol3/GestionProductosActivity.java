package com.example.cdol3;

import static com.example.cdol3.R.*;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cdol3.model.Articulos;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class GestionProductosActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private ProductoAdapter adapter;
    private List<Articulos> listaProductos;

    private EditText colegio, categoria, talla, cantidad;
    private Button guardar, eliminar;
    private LinearLayout formularioEdicion;
    private Articulos productoActual; // Artículo en edición

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gestion_productos);

        db = FirebaseFirestore.getInstance();

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewProductos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listaProductos = new ArrayList<>();
        adapter = new ProductoAdapter(listaProductos);
        recyclerView.setAdapter(adapter);

        // Campos de entrada
        colegio = findViewById(R.id.colegio);
        categoria = findViewById(R.id.categoria);
        talla = findViewById(R.id.talla);
        cantidad = findViewById(R.id.cantidad);

        // Botones
        guardar = findViewById(R.id.guardar);
        eliminar = findViewById(R.id.eliminar);
        formularioEdicion = findViewById(R.id.formularioEdicion);
        cargarProductos();

        // Acción de guardar
        // Acción de guardar
        guardar.setOnClickListener(v -> {
            if (validarCamposLlenos() && productoActual != null) { // Validar que los campos estén llenos y sea edición
                // Actualizar valores del producto
                productoActual.setColegio(colegio.getText().toString());
                productoActual.setCategoria(categoria.getText().toString());
                productoActual.setTalla(talla.getText().toString());
                productoActual.setCantidad(Integer.parseInt(cantidad.getText().toString()));

                // Actualizar en la base de datos
                db.collection("Articulos").document(productoActual.getId())
                        .set(productoActual)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Producto actualizado", Toast.LENGTH_SHORT).show();
                            int index = listaProductos.indexOf(productoActual);
                            listaProductos.set(index, productoActual);
                            adapter.notifyItemChanged(index); // Actualizar vista
                            finish(); // Regresar
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error al actualizar producto", Toast.LENGTH_SHORT).show()
                        );

                formularioEdicion.setVisibility(View.GONE); // Ocultar formulario
                limpiarFormulario(); // Limpiar campos
            } else {
                Toast.makeText(this, "Complete todos los campos o seleccione un producto válido", Toast.LENGTH_SHORT).show();
            }
        });


// Acción de eliminar
        eliminar.setOnClickListener(v -> {
            if (productoActual != null) {
                db.collection("Articulos").document(productoActual.getId())
                        .delete()
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Producto eliminado", Toast.LENGTH_SHORT).show();
                            listaProductos.remove(productoActual);
                            adapter.notifyDataSetChanged();
                            finish(); // Regresar
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(this, "Error al eliminar producto", Toast.LENGTH_SHORT).show()
                        );
                formularioEdicion.setVisibility(View.GONE);
                limpiarFormulario();
            }
        });

    }

    private void cargarProductos() {
        db.collection("Articulos").get()
                .addOnSuccessListener(querySnapshot -> {
                    listaProductos.clear();
                    for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                        Articulos articulo = document.toObject(Articulos.class);
                        if (articulo != null) {
                            articulo.setId(document.getId());
                            listaProductos.add(articulo);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Error al cargar productos", Toast.LENGTH_SHORT).show()
                );
    }

    private boolean validarCamposLlenos() {
        if (colegio.getText().toString().trim().isEmpty() ||
                categoria.getText().toString().trim().isEmpty() ||
                talla.getText().toString().trim().isEmpty() ||
                cantidad.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void limpiarFormulario() {
        colegio.setText("");
        categoria.setText("");
        talla.setText("");
        cantidad.setText("");
        productoActual = null;
    }

    // Adaptador para RecyclerView
    class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ProductoViewHolder> {
        private List<Articulos> productos;

        public ProductoAdapter(List<Articulos> productos) {
            this.productos = productos;
        }

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(layout.item_producto, parent, false);
            return new ProductoViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            Articulos producto = productos.get(position);
            holder.txtProductoInfo.setText("Colegio: " + producto.getColegio() +
                    ", Categoría: " + producto.getCategoria() +
                    ", Talla: " + producto.getTalla() +
                    ", Cantidad: " + producto.getCantidad());

            // Acción de editar
            holder.editar.setOnClickListener(v -> {
                formularioEdicion.setVisibility(View.VISIBLE);
                productoActual = producto;
                colegio.setText(producto.getColegio());
                categoria.setText(producto.getCategoria());
                talla.setText(producto.getTalla());
                cantidad.setText(String.valueOf(producto.getCantidad()));
            });
        }

        @Override
        public int getItemCount() {
            return productos.size();
        }

        public class ProductoViewHolder extends RecyclerView.ViewHolder {
            public Button editar, eliminar;
            public TextView txtProductoInfo;

            public ProductoViewHolder(@NonNull View itemView) {
                super(itemView);
                txtProductoInfo = itemView.findViewById(R.id.txtProductoInfo);
                editar = itemView.findViewById(R.id.editar);
            }
        }
    }

    public void regreso(View view) {
        finish();
    }
}
