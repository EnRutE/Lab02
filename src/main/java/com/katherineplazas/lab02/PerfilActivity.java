package com.katherineplazas.lab02;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class PerfilActivity extends AppCompatActivity {
    String Correo;
    TextView tUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        tUsuario = findViewById(R.id.tUsuario);

        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Correo = extras.getString("correo2");
        }
        tUsuario.setText(Correo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.m2_Principal) {
            Intent intent = new Intent(PerfilActivity.this,Principal_Activity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.m2_Cerrar_Sesion) {
            Intent intent2 = new Intent(PerfilActivity.this,MainActivity.class);
            startActivity(intent2);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
