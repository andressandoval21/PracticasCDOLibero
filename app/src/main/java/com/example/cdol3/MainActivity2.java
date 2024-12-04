package com.example.cdol3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {

    private EditText correo, contrasena;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        correo = findViewById(R.id.correo);
        contrasena = findViewById(R.id.contrasena);
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // Redirigir al usuario a la actividad de inicio
            Intent ingresoLogin = new Intent(MainActivity2.this, InicoActivity.class);
            startActivity(ingresoLogin);
            finish();
        }
    }

    public void ingresoLogin(View view) {
        String email = correo.getText().toString().trim();
        String password = contrasena.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(MainActivity2.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent ingresoLogin = new Intent(MainActivity2.this, InicoActivity.class);
                            startActivity(ingresoLogin);
                            finish();
                        } else {
                            String errorMessage = "Error de autenticación";
                            if (task.getException() != null) {
                                errorMessage = task.getException().getMessage();
                            }
                            Toast.makeText(MainActivity2.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
    public void olvideContrasena(View view) {
        // URL de recuperación de contraseña
        String url = "https://firebase.google.com/?hl=es-419";

        // Crear un intent para abrir el navegador con la URL
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        // Iniciar el intent
        startActivity(intent);
    }

}
