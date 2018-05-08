package com.katherineplazas.lab02;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Usuarios;

public class Perfil_Conductror_Activity extends AppCompatActivity {

    private TextView tNombre,tCedula,tTelefono,tPlaca,tCiudad,tCorreo;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_perfil__conductror_);

        tNombre = findViewById(R.id.tNombreCond);
        tCedula = findViewById(R.id.tCedulaCond);
        tTelefono = findViewById(R.id.tTelefonoCond);
        tPlaca = findViewById(R.id.tPlaca);
        tCiudad = findViewById(R.id.tCiudadCond);
        tCorreo = findViewById(R.id.tCorreoCond);

        datos_usuario_logueado();
    }

    private void datos_usuario_logueado(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        leer_base_de_datos_Conductor(firebaseUser.getEmail());
    }

    private void leer_base_de_datos_Conductor(final String correo) {

        databaseReference.child("conductores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(correo)){
                            tNombre.setText("Nombre: "+conductores.getEnombrecond());
                            tCedula.setText("Cédula: "+conductores.getEcedulacond());
                            tTelefono.setText("Teléfono: "+conductores.getEtelefono());
                            tPlaca.setText("Placa: "+conductores.getEplaca());
                            tCiudad.setText("Ciudad: "+conductores.getEcidudad());
                            tCorreo.setText(conductores.getEcorreo());
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }

    @Override
    public void onBackPressed() {
        goPrincipalConductorActivity();
    }

    private void goPrincipalConductorActivity() {
        Intent intent =new Intent(Perfil_Conductror_Activity.this,Principal_Conductor_Activity.class);
        startActivity(intent);
        finish();
    }
}
