package com.katherineplazas.lab02;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
public class Informacion_ConductorFragment extends Fragment {

    private TextView tNombreCond, tCedulaCond,tTelefonoCond,tCorreoCond,tCiudadCond,tPlaca;
    FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    static String correo_conductor;
    private Button bIrRunt;


    public Informacion_ConductorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_informacion__conductor, container, false);
        tCorreoCond = view.findViewById(R.id.tCorreoCond);
        tNombreCond = view.findViewById(R.id.tNombreCond);
        tCedulaCond = view.findViewById(R.id.tCedulaCond);
        tCiudadCond = view.findViewById(R.id.tCiudadCond);
        tTelefonoCond = view.findViewById(R.id.tTelefonoCond);
        tPlaca = view.findViewById(R.id.tPlaca);
        bIrRunt = view.findViewById(R.id.bIrRunt);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        //Para capturar correo del conductor asignado
        leer_base_de_datos_Acudiente(firebaseAuth.getCurrentUser().getEmail());
        //info_conductor();
        bIrRunt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.runt.com.co/consultaCiudadana/#/consultaPersona");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
            }
        });


        return view;
    }

    private void info_conductor(){

        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        leer_base_de_datos_Conductor(firebaseUser.getEmail());
    }

    private void leer_base_de_datos_Acudiente(final String correo) {
        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);

                        if(usuarios.getCorreo().equals(correo)){
                            leer_base_de_datos_Conductor(usuarios.getConductor());
                            break;
                        }
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }


    private void leer_base_de_datos_Conductor(final String correo) {
        databaseReference.child("conductores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(correo)){
                            tNombreCond.setText("Nombre: "+conductores.getEnombrecond());
                            tCedulaCond.setText("Cédula: "+conductores.getEcedulacond());
                            tTelefonoCond.setText("Teléfono: "+conductores.getEtelefono());
                            tPlaca.setText("Placa: "+conductores.getEplaca());
                            tCiudadCond.setText("Ciudad: "+conductores.getEcidudad());
                            tCorreoCond.setText("Correo electrónico:\n"+conductores.getEcorreo());
                            break;
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }

}
