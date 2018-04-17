package com.katherineplazas.lab02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class RegistroConductorActivity extends AppCompatActivity {

    EditText eCorreo,eContrasena,eContrasena2, ecedula,eplaca,eciudad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_conductor);
        eCorreo=findViewById(R.id.eMailConductor);
        eContrasena=findViewById(R.id.ePasswordCond);
        eContrasena2=findViewById(R.id.ePassword2Cond);
        ecedula=findViewById(R.id.eCedulaCond);
        eciudad=findViewById(R.id.eCiudad);
        eplaca=findViewById(R.id.ePlaca);
    }


    public void OnRegistrarConductorClicked(View view) {

    }
}
