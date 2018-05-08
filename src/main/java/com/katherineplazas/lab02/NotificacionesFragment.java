package com.katherineplazas.lab02;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.katherineplazas.lab02.Adapters.MensajesAdapter;

import com.katherineplazas.lab02.modelo.Mensajes;
import com.katherineplazas.lab02.modelo.ParqueaderosModelo;

import java.util.ArrayList;


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

        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference();

        databaseReference.child("mensajes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mensajesList.clear();
                if(dataSnapshot.exists()){
                    for ( DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Mensajes mensajes=snapshot.getValue(Mensajes.class);
                        mensajesList.add(mensajes);
                    }
                    adapterMensajes.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return view;
    }

}
