package com.katherineplazas.lab02;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.katherineplazas.lab02.Adapters.MensajesAdapter;

import com.katherineplazas.lab02.modelo.Mensajes;
import com.katherineplazas.lab02.modelo.ParqueaderosModelo;
import com.katherineplazas.lab02.modelo.Usuarios;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificacionesFragment extends Fragment {

    private ArrayList<Mensajes> mensajesList;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapterMensajes;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;


    public NotificacionesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notificaciones, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mensajesList=new ArrayList<>();
        adapterMensajes =new MensajesAdapter(mensajesList, R.layout.cardview,
                getActivity());
        recyclerView.setAdapter(adapterMensajes);


        databaseReference= FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        leer_base_de_datos_Acudiente();
        return view;
    }

    private void leer_base_de_datos_Acudiente() {

        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(firebaseAuth.getCurrentUser().getEmail())){
                         Log.d("usuario_conductor",usuarios.getConductor());
                           filtrar_mensajes(usuarios.getConductor());
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

    private void filtrar_mensajes(final String conductor) {
        final Date date = Calendar.getInstance().getTime();

        databaseReference.child("mensajes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String fecha_actual = String.valueOf(date.getDate());
                if(date.getDate()<10){
                    fecha_actual = "0"+fecha_actual+"/";
                }else {
                    fecha_actual = fecha_actual+"/";
                }
                if (date.getMonth()<10){
                    fecha_actual = fecha_actual+"0"+String.valueOf(date.getMonth()+1);
                }else {
                    fecha_actual = fecha_actual+String.valueOf(date.getMonth()+1);
                }
                mensajesList.clear();
                if(dataSnapshot.exists()){
                    for ( DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Mensajes mensajes=snapshot.getValue(Mensajes.class);
                        if(mensajes.getIdconductor().equals(conductor)){
                            String fecha= mensajes.getFecha();

                            if(fecha.substring(0,5).equals(fecha_actual)){
                                mensajesList.add(mensajes);
                            }
                        }

                    }
                    adapterMensajes.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
