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
import com.facebook.FacebookSdk;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katherineplazas.lab02.modelo.Usuarios;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    static EditText eUser, ePassword;
    static String UsuarioRegistrado, PassRegistrado,UsuarioActual,PassActual;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private SignInButton btnSingInGooogle;
    private GoogleApiClient googleApiClient;

    private CallbackManager callbackManager;
    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    //    FirebaseDatabase.getInstance().setPersistenceEnabled(true); //cache

        setContentView(R.layout.activity_main);
        eUser = findViewById(R.id.eUser);
        ePassword = findViewById(R.id.ePassword);
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email","public_profile");
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Login Facebook","OK");
                sigInFacebook(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d("Login Facebook","Cancelado");

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("Login Facebook","Error");
                error.printStackTrace();

            }
        });


        btnSingInGooogle = findViewById(R.id.btnSingInGogle);

        btnSingInGooogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(i,1);
            }
        });
        inicializar();
        getHashes();
    }

    private void sigInFacebook(AccessToken accessToken) {
   AuthCredential authCredential= FacebookAuthProvider.getCredential(accessToken.getToken());
   firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
       @Override
       public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()) {
               goPrincipalAcudienteActivity();
           }
               else{
                   Toast.makeText(MainActivity.this,"Autenticcion con Facebook no exitosa",Toast.LENGTH_SHORT).show();
               }
           }

   });
    }

    private void goPrincipalAcudienteActivity() {
        crearcuenta();
        Intent intent =new Intent(MainActivity.this,PruebaActivity.class);
        startActivity(intent);
        finish();
    }

    private void crearcuenta() {
        //Crea la tabla de usuarios
        // cont=0;
        //Usuarios usuarios=new Usuarios("user"+cont) forma 1
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        final FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        FirebaseDatabase.getInstance();
        final DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();//lab02-375eb

        databaseReference.child("usuarios").child(firebaseUser.getUid()).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.d("usuario:","OK");
                }else{
                    Log.d("usuario:","NO");
                    Usuarios usuarios=new Usuarios(firebaseUser.getUid(),
                            firebaseUser.getDisplayName(),
                            firebaseUser.getPhoneNumber(),
                            0);//para verficar si un usuario ya esta registrado
                    databaseReference.child("usuarios").child(usuarios.getId()).setValue(usuarios);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
    }

    private void getHashes(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.katherineplazas.lab02",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    public void OnRegisterAcudienteCLicked(View view) {
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
                   // goPrincipalAcudienteActivity();
                    Toast.makeText(MainActivity.this,"Usuario Logeado;"+firebaseUser.getEmail().toString(),Toast.LENGTH_LONG).show();
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
                        goPrincipalAcudienteActivity();
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
        }else{
            callbackManager.onActivityResult(requestCode,resultCode,data);
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
        if(eUser.getText().toString().trim().equalsIgnoreCase("")){
            eUser.setError("Campo vacío");
        }else if(ePassword.getText().toString().trim().equalsIgnoreCase("")){
            ePassword.setError("Campo vacío");
        }else{
            iniciarSesionFirebase(eUser.getText().toString(),ePassword.getText().toString());
        }
    }

    private void iniciarSesionFirebase(String correo,String contrasena) {
        firebaseAuth.signInWithEmailAndPassword(correo,contrasena).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                   // Intent i = new Intent(MainActivity.this,PerfilActivity.class);
                    //startActivity(i); volver a usar
                    //borrar esto despues
                   goPrincipalAcudienteActivity();
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
        Intent intent2 = new Intent(MainActivity.this,RegistroConductorActivity.class);
        startActivity(intent2);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

}
