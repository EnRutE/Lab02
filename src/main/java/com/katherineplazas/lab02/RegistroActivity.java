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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Usuarios;

import java.nio.channels.Channels;

public class RegistroActivity extends AppCompatActivity {

    EditText eMail, ePassword, ePassword2,eNombre,eCedula,eTelefono,eNombreEstud,eDocumentoEstud;
    EditText eDireccionCasa, eDireccionCoelgio, eColegio,eRh;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eMail = findViewById(R.id.eMail);
        ePassword = findViewById(R.id.ePassword);
        ePassword2 = findViewById(R.id.ePassword2);
        eNombre = findViewById(R.id.eNombreAcud);
        eCedula = findViewById(R.id.eCedulaAcud);
        eTelefono = findViewById(R.id.eTelefonoAcu);
        eNombreEstud = findViewById(R.id.eNombreEst);
        eDocumentoEstud = findViewById(R.id.eDocEst);
        eColegio = findViewById(R.id.eColegio);
        eDireccionCoelgio = findViewById(R.id.eDircol);
        eRh = findViewById(R.id.eRh);
        eDireccionCasa = findViewById(R.id.eDirEst);

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


    public void OnRegisterCLicked(View view) {

        char ucaracter;
        int i;
        int cont=0;
        char flagcaracter = '3';

        if(eMail.getText().toString().trim().equalsIgnoreCase("")){
            eMail.setError(getResources().getString(R.string.mensaje));
        }else if(ePassword.getText().toString().trim().equalsIgnoreCase("")){
            ePassword.setError(getResources().getString(R.string.mensaje));
        }else if(ePassword2.getText().toString().trim().equalsIgnoreCase("")){
            ePassword2.setError(getResources().getString(R.string.mensaje));
        }else if(eNombre.getText().toString().trim().equalsIgnoreCase("")){
            eNombre.setError(getResources().getString(R.string.mensaje));
        }else if(eCedula.getText().toString().trim().equalsIgnoreCase("")){
            eCedula.setError(getResources().getString(R.string.mensaje));
        }else if(eTelefono.getText().toString().trim().equalsIgnoreCase("")){
            eTelefono.setError(getResources().getString(R.string.mensaje));
        }else if(eNombreEstud.getText().toString().trim().equalsIgnoreCase("")){
            eNombreEstud.setError(getResources().getString(R.string.mensaje));
        }else if(eDocumentoEstud.getText().toString().trim().equalsIgnoreCase("")){
            eDocumentoEstud.setError(getResources().getString(R.string.mensaje));
        }else if(eDireccionCasa.getText().toString().trim().equalsIgnoreCase("")){
            eDireccionCasa.setError(getResources().getString(R.string.mensaje));
        }else if(eColegio.getText().toString().trim().equalsIgnoreCase("")){
            eColegio.setError(getResources().getString(R.string.mensaje));
        }else if(eDireccionCoelgio.getText().toString().trim().equalsIgnoreCase("")){
            eDireccionCoelgio.setError(getResources().getString(R.string.mensaje));
        }else if(eRh.getText().toString().trim().equalsIgnoreCase("")){
            eRh.setError(getResources().getString(R.string.mensaje));
        }else{
            for(i=0;i<=eMail.length()-1;i++) {
                ucaracter = eMail.getText().charAt(i);
                if (ucaracter == '@') {
                    cont = cont + 1;

                }
            }
            if(cont>=2){
                eMail.setError("Correo no válido");
            }else if(ePassword.getText().toString().equalsIgnoreCase(ePassword2.getText().toString())){
                for(i=0;i<=ePassword.length()-1;i++){
                    ucaracter = ePassword.getText().charAt(i);
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
                    ePassword.setError("Espacios o caracteres no permitidos");
                }else{
                    if(ePassword.length()-1 < 5){
                        ePassword.setError("Longitud mínima de 6 caracteres");
                    }else {
                        crearCuentaFirebase(eMail.getText().toString(),ePassword.getText().toString());
                    }
                }
            }else{
                Toast.makeText(this, getResources().getString(R.string.mensaje2), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearBasedeDados_Acudiente() {

        Usuarios usuarios = new Usuarios(databaseReference.push().getKey(),
                eNombre.getText().toString(),
                eTelefono.getText().toString(),
                eMail.getText().toString(),
                eCedula.getText().toString(),
                eNombreEstud.getText().toString(),
                eDocumentoEstud.getText().toString(),
                eDireccionCasa.getText().toString(),
                eColegio.getText().toString(),
                eDireccionCoelgio.getText().toString(),
                eRh.getText().toString(),
                "N",
                "N",
                "foto_estudiante",
                "foto_conductor");

        databaseReference.child("acudientes").child(usuarios.getId()).setValue(usuarios).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Log.d("Base_Datos_Acudiente","OK");
                }
                else{
                    Log.d("Base_Datos_Acudiente","Error");
                }
            }
        });




    }

    private void crearCuentaFirebase(String correo,String contrasena) {
        firebaseAuth.createUserWithEmailAndPassword(correo,contrasena)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegistroActivity.this,"Cuenta creada",Toast.LENGTH_SHORT).show();
                    crearBasedeDados_Acudiente();
                    //goLoginActivity();
                    goPrincipalAcudiente();
                }else {
                    Toast.makeText(RegistroActivity.this,"Correo ya existe",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goPrincipalAcudiente() {
        Intent intent =new Intent(RegistroActivity.this,Principal_AcudienteActivity.class);
        startActivity(intent);
        finish();
    }

    private void goLoginActivity() {
        Intent intent =new Intent(RegistroActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
