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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Mensajes;

public class Mensaje_Activity extends AppCompatActivity {

    EditText eAsunto, eMensaje;
    private DatabaseReference databaseReference;

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

        Mensajes mensajes = new Mensajes(databaseReference.push().getKey(),
                eAsunto.getText().toString(),
                eMensaje.getText().toString(),
                "Id_Conductor",
                "https://firebasestorage.googleapis.com/v0/b/lab02-375eb.appspot.com/o/Parqueaderos%2Ffamilia_2.png?alt=media&token=5e85d8fb-640e-4629-adf6-3873adc5dda6");

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
    }
}
