package com.katherineplazas.lab02;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Usuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static EditText eUser, ePassword;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private boolean FlagAcudiente=false;
    private boolean FlagConductor=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    FirebaseDatabase.getInstance().setPersistenceEnabled(true); //cache
        databaseReference=FirebaseDatabase.getInstance().getReference();

        setContentView(R.layout.activity_main);
        eUser = findViewById(R.id.eUser);
        ePassword = findViewById(R.id.ePassword);
        inicializar();

    }
    private void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance();
        Log.d("jjjjjjjj",":");
        FlagAcudiente=false;
        FlagConductor=false;
        //para escuchar si alguien esta logeado
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    buscar_perfil(firebaseAuth.getCurrentUser().getEmail());

                    Log.d("jjjjjjjj0",firebaseUser.getEmail());
                }else{
                }
            }
        };





    }





    private void buscar_perfil(final String correo){

        databaseReference.child("conductores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                Log.d("jjjjjjjj1:",correo);
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                        Conductores conductores = snapshot.getValue(Conductores.class);
                        Log.d("jjjjjjjj2:",conductores.getEcorreo());
                       if(conductores.getEcorreo().equals(correo)&& !FlagConductor){
                           Log.d("jjjjjjjj3:",conductores.getEcorreo());
                           FlagConductor=true;
                          goPrincipalConductorActivity();
                       }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }

        });

        databaseReference.child("acudientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        Log.d("jjjjjjjj4:",usuarios.getCorreo());
                        if(usuarios.getCorreo().equals(correo)&&!FlagAcudiente){
                            FlagAcudiente=true;
                            Log.d("jjjjjjjj5:",usuarios.getCorreo());
                            goPrincipalAcudienteActivity();
                        }
                    }

                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError){

            }

        });
    }
    private void goPrincipalAcudienteActivity() {
        //crearcuenta();
        Intent intent =new Intent(MainActivity.this,Principal_AcudienteActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }



    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }


    public void OnRegisterAcudienteCLicked(View view) {
        Intent intent = new Intent(MainActivity.this,RegistroActivity.class);
        startActivityForResult(intent,123);
    }





    private void goPrincipalConductorActivity() {

        Intent intent =new Intent(MainActivity.this,Principal_Conductor_Activity.class);
        startActivity(intent);
        finish();
    }



    public void OnLogInClicked(View view) {

        if(eUser.getText().toString().trim().equalsIgnoreCase("")){
            eUser.setError("Campo vacío");
        }else if(ePassword.getText().toString().trim().equalsIgnoreCase("")){
            ePassword.setError("Campo vacío");
        }else{

            iniciarSesionFirebase(eUser.getText().toString(),ePassword.getText().toString());


        }
    }

    private void iniciarSesionFirebase(final String correo, String contrasena) {
        firebaseAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {

                    buscar_perfil(correo);


                }else {

                    Toast.makeText(MainActivity.this,"Usuario no registrado",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void OnRegisterConductorCLicked(View view) {
        goRegistroConductorActivity();
    }

    private void goRegistroConductorActivity() {
        Intent intent =new Intent(MainActivity.this,RegistroConductorActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

}
