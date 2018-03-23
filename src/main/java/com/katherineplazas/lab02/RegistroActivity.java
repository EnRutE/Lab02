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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.channels.Channels;

public class RegistroActivity extends AppCompatActivity {

    EditText eMail, ePassword, ePassword2;
    String pass,rpass,correo;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eMail = findViewById(R.id.eMail);
        ePassword = findViewById(R.id.ePassword);
        ePassword2 = findViewById(R.id.ePassword2);

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


    public void OnRegisterCLicked(View view) {

        Intent intent = new Intent();

        if(eMail.getText().toString().trim().equalsIgnoreCase("")){
            eMail.setError(getResources().getString(R.string.mensaje));
        }else if(ePassword.getText().toString().trim().equalsIgnoreCase("")){
            ePassword.setError(getResources().getString(R.string.mensaje));
        }else if(ePassword2.getText().toString().trim().equalsIgnoreCase("")){
            ePassword2.setError(getResources().getString(R.string.mensaje));
        }else{
            pass = ePassword.getText().toString();
            rpass = ePassword2.getText().toString();
            if(pass.equalsIgnoreCase(rpass)){
                correo = eMail.getText().toString();
                intent.putExtra("User",correo);
                intent.putExtra("Pass",pass);
                setResult(RESULT_OK,intent);
                crearCuentaFirebase(eMail.getText().toString(),ePassword.getText().toString());
                finish();
            }else{
                Toast.makeText(this, getResources().getString(R.string.mensaje2), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearCuentaFirebase(String correo,String contrasena) {
        firebaseAuth.createUserWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistroActivity.this,"Cuenta creada",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(RegistroActivity.this,"Error al crear la cuenta",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
