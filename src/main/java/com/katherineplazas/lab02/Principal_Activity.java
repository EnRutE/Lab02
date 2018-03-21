package com.katherineplazas.lab02;

import android.content.Intent;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class Principal_Activity extends AppCompatActivity {
    static String Correo;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.activity_principal_);
        super.onCreate(savedInstanceState);


        Bundle extras = getIntent().getExtras();
        if(extras!=null) {
            Correo = extras.getString("correo");

        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mPerfil) {
            Intent intent = new Intent(Principal_Activity.this, PerfilActivity.class);
           intent.putExtra("correo2",Correo);
            startActivityForResult(intent, 3);

            Toast.makeText(this,Correo,Toast.LENGTH_SHORT).show();

        } else if (id == R.id.mCerrar_Sesion) {


            Intent intent2 = new Intent(Principal_Activity.this, MainActivity.class);
            startActivityForResult(intent2, 4);

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        setResult(RESULT_OK);
        finish();
    }
}
