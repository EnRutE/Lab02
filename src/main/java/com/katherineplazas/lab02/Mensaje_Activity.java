package com.katherineplazas.lab02;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Mensajes;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class Mensaje_Activity extends AppCompatActivity {

    EditText eAsunto, eMensaje;
    private DatabaseReference databaseReference;
    private Calendar calendar;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mensaje_);

        eAsunto = findViewById(R.id.eAsunto);
        eMensaje = findViewById(R.id.eMensaje);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();


    }

    @Override
    public void onBackPressed() {
        goPrincipalConductorActivity();
    }

    private void goPrincipalConductorActivity() {
        Intent intent =new Intent(Mensaje_Activity.this,Principal_Conductor_Activity.class);
        startActivity(intent);
        finish();
    }

    public void OnEnviarMensajeCLicked(View view) {
        if(eAsunto.getText().toString().trim().equalsIgnoreCase("")){
            eAsunto.setError("Campo vacío");
        }else if(eMensaje.getText().toString().trim().equalsIgnoreCase("")){
            eMensaje.setError("Campo vacío");
        }else{
            crear_base_de_datos_mensaje();
            goPrincipalConductorActivity();
            Toast.makeText(Mensaje_Activity.this, "Mensaje enviado", Toast.LENGTH_SHORT).show();
        }
    }

    private void crear_base_de_datos_mensaje() {

        Date date = Calendar.getInstance().getTime();
        FirebaseAuth firebaseAuth;
        firebaseAuth = FirebaseAuth.getInstance();
        fecha = String.valueOf(date.getDate());
        if(date.getDate()<10){
            fecha = "0"+fecha+"/";
        }else {
            fecha = fecha+"/";
        }
        if (date.getMonth()<10){
            fecha = fecha+"0"+String.valueOf(date.getMonth()+1);
        }else {
            fecha = fecha+String.valueOf(date.getMonth()+1);
        }

        leer_base_de_datos_Conductor(firebaseAuth.getCurrentUser().getEmail());



    }

    private void leer_base_de_datos_Conductor(final String correo) {

        databaseReference.child("conductores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Date date = Calendar.getInstance().getTime();
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(correo)){
                            Mensajes mensajes = new Mensajes(databaseReference.push().getKey(),
                                    eAsunto.getText().toString(),
                                    eMensaje.getText().toString(),
                                    correo,
                                    conductores.getFoto_cond(),
                                    fecha+" | "+String.valueOf(date.getHours())+":"+String.valueOf(date.getMinutes()));

                            databaseReference.child("mensajes").child(mensajes.getId()).setValue(mensajes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("Base_Datos_mensajes","OK");
                                    }
                                    else{
                                        Log.d("Base_Datos_mensajes","ERROR");
                                    }
                                }
                            });
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
