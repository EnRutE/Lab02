package com.katherineplazas.lab02;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil_AcudienteFragment extends Fragment {

    TextView tNombre, tCedula,tTelefono,tNombre_est,tDocumento_est,tCiudad_est,tColegio,tDireccion_cole,tRh,tCorreo;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;


    public Perfil_AcudienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_perfil__acudiente, container, false);
       tNombre = view.findViewById(R.id.tNombre);
       tCedula = view.findViewById(R.id.tCedula);
       tTelefono = view.findViewById(R.id.tTelefono);
       tNombre_est = view.findViewById(R.id.tNombre_est);
       tDocumento_est = view.findViewById(R.id.tDocumento_est);
       tCiudad_est = view.findViewById(R.id.tCiudad);
       tColegio = view.findViewById(R.id.tColegio);
       tDireccion_cole = view.findViewById(R.id.tDireccion_cole);
       tRh = view.findViewById(R.id.tRh);
       tCorreo = view.findViewById(R.id.tCorreo);
       databaseReference= FirebaseDatabase.getInstance().getReference();
       inicializar();


       return view;

    }

    private void leer_base_de_datos_Acudiente(final String correo) {


        databaseReference.child("acudientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(correo)){
                           tNombre.setText("Nombre: "+usuarios.getNombre());
                           tCedula.setText("Cédula: "+usuarios.getCedula());
                           tTelefono.setText("Teléfono: "+usuarios.getTelefono());
                           tNombre_est.setText("Estudiante: "+usuarios.getEstudiante());
                           tDocumento_est.setText("Dcumento estudiante: "+usuarios.getDocumentoestudiante());
                           tCiudad_est.setText("Ciudad de residencia: "+usuarios.getDireccioncasa());
                           tColegio.setText("Colegio: "+usuarios.getColegio());
                           tDireccion_cole.setText("Dirección colegio: "+usuarios.getDireccioncolegio());
                           tRh.setText("RH: "+usuarios.getRh());
                           tCorreo.setText(usuarios.getCorreo());
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }

    private void inicializar(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        leer_base_de_datos_Acudiente(firebaseUser.getEmail());
    }
}
