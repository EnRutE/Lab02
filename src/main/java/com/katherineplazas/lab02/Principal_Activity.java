package com.katherineplazas.lab02;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Principal_Activity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    static String Correo;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleApiClient googleApiClient;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_principal_);
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
           // Correo = extras.getString("correo");

        }

        inicializar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    private void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance();
        //para escuchar si alguien esta logeado
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("Usuario_logueado",firebaseUser.getEmail());
                    //Toast.makeText(Principal_Activity.this,"Usuario Logeado;"+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
                }else{
                    Log.d("Usuario_logueado","NO");
                    //Toast.makeText(Principal_Activity.this,"Usuario No Logeado;"+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mPerfil) {
            Intent intent = new Intent(Principal_Activity.this, PerfilActivity.class);
           intent.putExtra("correo2",Correo);
            startActivityForResult(intent, 3);

            //Toast.makeText(this,Correo,Toast.LENGTH_SHORT).show();

        } else if (id == R.id.mCerrar_Sesion) {

            CerrarSesionGoogle();
        }
        return super.onOptionsItemSelected(item);
    }

    private void CerrarSesionGoogle() {
        firebaseAuth.signOut();
        if(Auth.GoogleSignInApi!=null) {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {


                @Override
                public void onResult(@NonNull Status status) {
                    if (status.isSuccess()) {
                        goToLoginActivity();
                        finish();

                    } else {
                        Log.d("Cerrar_sesion_google:", "ERROR");

                    }
                }
            });
        }
        if(LoginManager.getInstance()!=null){
            LoginManager.getInstance().logOut();
        }
    }

    private void goToLoginActivity() {
        Intent intent3= new Intent(Principal_Activity.this,MainActivity.class);
        startActivity(intent3);
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(authStateListener);
        googleApiClient.disconnect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        googleApiClient.stopAutoManage(this);
        googleApiClient.disconnect();
    }
}
