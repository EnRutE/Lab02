package com.katherineplazas.lab02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

   static EditText eUser, ePassword;
   static String UsuarioRegistrado, PassRegistrado,UsuarioActual,PassActual;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        eUser = findViewById(R.id.eUser);
        ePassword = findViewById(R.id.ePassword);
    }

    public void OnRegisterCLicked(View view) {
        Intent intent = new Intent(MainActivity.this,RegistroActivity.class);
        startActivityForResult(intent,123);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==123 && resultCode == RESULT_OK){
            UsuarioRegistrado = data.getExtras().getString("User");
            PassRegistrado = data.getExtras().getString("Pass");
        }else if(requestCode==123 && resultCode == RESULT_CANCELED){
            Toast.makeText(this,"No se completo el registro",Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public void OnLogInClicked(View view) {
        UsuarioActual = eUser.getText().toString();
        PassActual = ePassword.getText().toString();
        if(UsuarioRegistrado.equalsIgnoreCase(UsuarioActual) && PassRegistrado.equalsIgnoreCase(PassActual)){
            Intent intent_principal = new Intent(MainActivity.this,Principal_Activity.class);
            intent_principal.putExtra("correo",UsuarioRegistrado);
            startActivityForResult(intent_principal,2);

        }else {
            Toast.makeText(this,"Usuario no registrado",Toast.LENGTH_SHORT).show();
        }
    }
}
