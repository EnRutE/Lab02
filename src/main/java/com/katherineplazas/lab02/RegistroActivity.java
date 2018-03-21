package com.katherineplazas.lab02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegistroActivity extends AppCompatActivity {

    EditText eMail, ePassword, ePassword2;
    String pass,rpass,correo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        eMail = findViewById(R.id.eMail);
        ePassword = findViewById(R.id.ePassword);
        ePassword2 = findViewById(R.id.ePassword2);
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
                finish();
            }else{
                Toast.makeText(this, getResources().getString(R.string.mensaje2), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        super.onBackPressed();
    }
}
