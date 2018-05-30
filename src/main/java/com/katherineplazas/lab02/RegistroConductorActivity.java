package com.katherineplazas.lab02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Usuarios;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class RegistroConductorActivity extends AppCompatActivity {

    private EditText eCorreo,eContrasena,eContrasena2, ecedula,eplaca,eciudad,eTelefono,eNombreCond;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_conductor);

        eCorreo=findViewById(R.id.eMailConductor);
        eContrasena=findViewById(R.id.ePasswordCond);
        eContrasena2=findViewById(R.id.ePassword2Cond);
        eNombreCond = findViewById(R.id.eNombreCond);
        ecedula=findViewById(R.id.eCedulaCond);
        eciudad=findViewById(R.id.eCiudad);
        eplaca=findViewById(R.id.ePlaca);
        eTelefono = findViewById(R.id.eTelefono);


        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        inicializar();

    }

    private void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance();
        //para escuchar si alguien esta logeado
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("Usuario Logeado;",firebaseUser.getEmail());
                }else{
                    Log.d("Usuario Logeado;","No hay");
                }
            }
        };
    }


    public void OnRegistrarConductorClicked(View view) {

        char ucaracter;
        int i;
        int cont=0;
        char flagcaracter = '3';
        char flagcaracter1 = '3';

        if(eCorreo.getText().toString().trim().equalsIgnoreCase("")){
            eCorreo.setError("Campo vacío");
        }else if(eContrasena.getText().toString().trim().equalsIgnoreCase("")){
            eContrasena.setError("Campo vacío");
        }else if(eContrasena2.getText().toString().trim().equalsIgnoreCase("")){
            eContrasena2.setError("Campo vacío");
        }else if(eNombreCond.getText().toString().trim().equalsIgnoreCase("")){
            eNombreCond.setError("Campo vacío");
        }else if(ecedula.getText().toString().trim().equalsIgnoreCase("")){
            ecedula.setError("Campo vacío");
        }else if(eplaca.getText().toString().trim().equalsIgnoreCase("")){
            eplaca.setError("Campo vacío");
        }else if(eciudad.getText().toString().trim().equalsIgnoreCase("")){
            eciudad.setError("Campo vacío");
        }else if(eTelefono.getText().toString().trim().equalsIgnoreCase("")){
            eTelefono.setError("Campo vacío");
        }else{
            for(i=0;i<=eCorreo.length()-1;i++) {
                ucaracter = eCorreo.getText().charAt(i);
                if (ucaracter == '@') {
                    cont = cont + 1;

                }
            }
            if(cont>=2){
                eCorreo.setError("Correo no válido");
            }else if(eContrasena.getText().toString().equalsIgnoreCase(eContrasena2.getText().toString())){
                for(i=0;i<=eContrasena.length()-1;i++){
                    ucaracter = eContrasena.getText().charAt(i);
                    if(ucaracter == ' ' || ucaracter == '`'|| ucaracter == '~' || ucaracter == '!'|| ucaracter == '@' || ucaracter == '#'
                            ||ucaracter == '$'|| ucaracter == '%' || ucaracter == '^'|| ucaracter == '*' || ucaracter == '('
                            ||ucaracter == ')'|| ucaracter == '_' || ucaracter == '+'|| ucaracter == '-' || ucaracter == '='
                            ||ucaracter == '{'|| ucaracter == '}' || ucaracter == '|'|| ucaracter == '[' || ucaracter == ']'
                            ||ucaracter == ':'|| ucaracter == '"' || ucaracter == ';'|| ucaracter == '\'' || ucaracter == '<'
                            ||ucaracter == '>'|| ucaracter == '?' || ucaracter == ','|| ucaracter == '.' || ucaracter == '/'){
                        flagcaracter = '1';
                    }
                }
                //Log.d("flag",Character.toString(flagcaracter));
                if(flagcaracter == '1'){
                    eContrasena.setError("Espacios o caracteres no permitidos");
                }else{
                    if(eContrasena.length()-1 < 5){
                        eContrasena.setError("Longitud mínima de 6 caracteres");
                    }else {
                        crearCuentaFirebase(eCorreo.getText().toString(),eContrasena.getText().toString());
                    }

                }
            }else{
                //Toast.makeText(this, getResources().getString(R.string.mensaje2), Toast.LENGTH_SHORT).show();
                eContrasena.setError("Contraseñas diferentes");
            }
        }
    }

    private void crear_base_de_datos_conductor() {

        Conductores conductores = new Conductores(databaseReference.push().getKey(),
                eCorreo.getText().toString(),
                eNombreCond.getText().toString(),
                ecedula.getText().toString(),
                eplaca.getText().toString(),
                eciudad.getText().toString(),
                eTelefono.getText().toString(),
                "000",
                "000",
                "https://firebasestorage.googleapis.com/v0/b/lab02-375eb.appspot.com/o/usuariosFotos%2Fperfil_conductor.png?alt=media&token=da6efefe-d5f6-424a-9865-0411c8608f3f",
                "0");

        databaseReference.child("conductores").child(conductores.getId()).setValue(conductores).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d("Base_Datos_Conductores","OK");
                }
                else{
                    Log.d("Base_Datos_conductores","ERROR");
                }
            }
        });
    }

    private void crearCuentaFirebase(String correo, String contrasena) {

        firebaseAuth.createUserWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(RegistroConductorActivity.this,"Cuenta creada",Toast.LENGTH_SHORT).show();
                            crear_base_de_datos_conductor();
                            //goLoginActivity();
                            goPriciapalConductor();
                        }else {

                            Toast.makeText(RegistroConductorActivity.this,"Correo ya existe",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void goPriciapalConductor() {
        Intent intent =new Intent(RegistroConductorActivity.this,Principal_Conductor_Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        goLoginActivity();
    }

    private void goLoginActivity() {
        Intent intent =new Intent(RegistroConductorActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }



}
