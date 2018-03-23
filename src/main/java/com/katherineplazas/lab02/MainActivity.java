package com.katherineplazas.lab02;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static EditText eUser, ePassword;
    static String UsuarioRegistrado, PassRegistrado,UsuarioActual,PassActual;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SignInButton btnSingInGooogle;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        eUser = findViewById(R.id.eUser);
        ePassword = findViewById(R.id.ePassword);

        btnSingInGooogle = findViewById(R.id.btnSingInGogle);

        btnSingInGooogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i,1);
            }
        });
        inicializar();
    }



    public void OnRegisterCLicked(View view) {
        Intent intent = new Intent(MainActivity.this,RegistroActivity.class);
        startActivityForResult(intent,123);
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
        GoogleSignInOptions gso = new GoogleSignInOptions.
                Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestIdToken(getString(R.string.default_web_client_id)).
                requestEmail().build();

        googleApiClient = new GoogleApiClient.Builder(this).
                enableAutoManage(this, this).
                addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();
    }



    private void signInGoogle(GoogleSignInResult googleSignInResult){
        if(googleSignInResult.isSuccess()){
            AuthCredential authCredential = GoogleAuthProvider.
                    getCredential(googleSignInResult.getSignInAccount().getIdToken(),null);

            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Intent i = new Intent(MainActivity.this,PerfilActivity.class);
                        startActivity(i);
                    }else {
                        Toast.makeText(MainActivity.this,"Error de inicio de sesion",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*if(requestCode==123 && resultCode == RESULT_OK){
            UsuarioRegistrado = data.getExtras().getString("User");
            PassRegistrado = data.getExtras().getString("Pass");
        }else if(requestCode==123 && resultCode == RESULT_CANCELED){
            Toast.makeText(this,"No se completo el registro",Toast.LENGTH_SHORT).show();
        }*/
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.
                    getSignInResultFromIntent(data);
            signInGoogle(googleSignInResult);
        }
    }

    public void OnLogInClicked(View view) {
        /*
        UsuarioActual = eUser.getText().toString();
        PassActual = ePassword.getText().toString();
        if(UsuarioRegistrado.equalsIgnoreCase(UsuarioActual) && PassRegistrado.equalsIgnoreCase(PassActual)){
            Intent intent_principal = new Intent(MainActivity.this,Principal_Activity.class);
            intent_principal.putExtra("correo",UsuarioRegistrado);
            startActivityForResult(intent_principal,2);

        }else {
            Toast.makeText(this,"Usuario no registrado",Toast.LENGTH_SHORT).show();
        }*/
        iniciarSesionFirebase(eUser.getText().toString(),ePassword.getText().toString());
    }

    private void iniciarSesionFirebase(String correo,String contrasena) {
        firebaseAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent i = new Intent(MainActivity.this,PerfilActivity.class);
                    startActivity(i);
                }else {
                    Toast.makeText(MainActivity.this,"Error al iniciar",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
