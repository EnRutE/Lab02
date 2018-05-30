package com.katherineplazas.lab02;

import android.*;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Usuarios;

import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class Principal_Conductor_Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private EditText eDocumento;

    private ImageView iEscanear,iRecoegerColegio,iRecoegerCasa,iDejarColegio,iDejarCasa;
    private ZXingScannerView zXingScannerView;
    private String dato, nombreCond, correoCond;
    private TextView tNombreCond, tCorreoCond,tRecoger,tDejar;
    private LocationManager locationM;
    private String mejor_proveedor;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_principal__conductor_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        eDocumento = findViewById(R.id.eDocumento);
        iDejarCasa = findViewById(R.id.iDejarCasa);
        iDejarColegio = findViewById(R.id.iDejarColegio);
        iRecoegerCasa = findViewById(R.id.iRecoegerCasa);
        iRecoegerColegio = findViewById(R.id.iRecoegerColegio);
        tRecoger = findViewById(R.id.tRecoger);
        tDejar = findViewById(R.id.tDejar);
        iRecoegerCasa.setVisibility(View.VISIBLE);
        iDejarCasa.setVisibility(View.VISIBLE);
        iDejarColegio.setVisibility(View.VISIBLE);
        iRecoegerColegio.setVisibility(View.VISIBLE);
        eDocumento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iRecoegerCasa.setVisibility(View.VISIBLE);
                iDejarCasa.setVisibility(View.VISIBLE);
                iDejarColegio.setVisibility(View.VISIBLE);
                iRecoegerColegio.setVisibility(View.VISIBLE);
            }
        });
        //iEscanear = findViewById(R.id.iEscanear);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView = navigationView.getHeaderView(0);
        tNombreCond = hView.findViewById(R.id.tNombreCond);
        tCorreoCond = hView.findViewById(R.id.tCorreoCond);
        inicializar();

        if (correoCond.trim().equalsIgnoreCase("")) {
            Log.d("USUARIOS", "no hay");
        } else {
            tCorreoCond.setText(correoCond);
            leer_base_de_datos_Conductor(correoCond);
        }
        //Captura dato del lector de codigo
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            dato = extras.getString("DATO");
            eDocumento.setText(dato);
        }
        locationM = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        isLocationEnabled();
        mejor_proveedor = getbestprovider(locationM);
        locationM.requestLocationUpdates( mejor_proveedor,
                5000,
                10, locationListenermejor);

    }

    private void isLocationEnabled() {

        if(!locationM.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder alertDialog=new AlertDialog.Builder(this);
            alertDialog.setTitle("Habilitar localización");
            alertDialog.setMessage("Por favor habilitar su localización.");
            alertDialog.setPositiveButton("Configuración", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            AlertDialog alert=alertDialog.create();
            alert.show();
        }
        else{

        }
    }

    //  @SuppressWarnings("MissingPermission")
  LocationListener locationListenermejor=new LocationListener() {
      @Override
      public void onLocationChanged(android.location.Location location) {
          double latitude=location.getLatitude();
          double longitude=location.getLongitude();
          String posicion="New Latitude: "+latitude + "New Longitude: "+longitude;
        //  Toast.makeText(Principal_Conductor_Activity.this,"colll",Toast.LENGTH_LONG).show();
          SubirPosicionBasedeDatos(location);
      }

      @Override
      public void onStatusChanged(String provider, int status, Bundle extras) {

      }

      @Override
      public void onProviderEnabled(String provider) {

      }

      @Override
      public void onProviderDisabled(String provider) {

      }
  };

    private void SubirPosicionBasedeDatos(final Location location) {

        databaseReference.child("conductores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(firebaseAuth.getCurrentUser().getEmail())){
                            conductores.setLatitud_cond(String.valueOf(location.getLatitude()));
                            conductores.setLongitud_cond(String.valueOf(location.getLongitude()));
                            conductores.setVelocidad(String.valueOf((location.getSpeed())));
                            databaseReference.child("conductores").child(conductores.getId()).setValue(conductores);
                            break;
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }


    public void Escanear(View view) {
        goLectorCodigo();
    }

    private void goLectorCodigo() {
        Intent intent = new Intent(Principal_Conductor_Activity.this, zsingscanner.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("ONdestroy","ok");
    }


    private String getbestprovider(LocationManager locationManager) {
        Criteria criteria = new Criteria();
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        String bestprovider = locationManager.getBestProvider(criteria, true);
        return bestprovider;
    }



    private void inicializar() {
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        //Log.d("Correo1:",firebaseUser.getEmail()+"**********************");
        correoCond = firebaseUser.getEmail();
        nombreCond = firebaseUser.getDisplayName();

        //para escuchar si alguien esta logeado
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    Log.d("Correo1:",firebaseUser.getEmail()+"**********************");
                    //Toast.makeText(Principal_AcudienteActivity.this,"Usuario Logeado;"+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
                }else{
                    //Toast.makeText(Principal_AcudienteActivity.this,"Usuario No Logeado;"+firebaseUser.getEmail(),Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.iConductor_Miperfil) {
            goPerfilConductorActivity();
        } else if (id == R.id.iConductor_Mensaje) {
            goMensajeConductorActivity();
        }else if (id == R.id.iConductor_Cerrarsesion) {
            CerrarSesion();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void CerrarSesion() {
        firebaseAuth.signOut();
        goIniciarSesion();
    }

    private void goIniciarSesion() {
        Intent intent =new Intent(Principal_Conductor_Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goPerfilConductorActivity() {
        Intent intent =new Intent(Principal_Conductor_Activity.this,Perfil_Conductror_Activity.class);
        startActivity(intent);
        finish();
    }

    private void goMensajeConductorActivity() {
        Intent intent =new Intent(Principal_Conductor_Activity.this,Mensaje_Activity.class);
        startActivity(intent);
        finish();
    }

    private void leer_base_de_datos_Conductor(final String correo) {

        databaseReference.child("conductores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(correo)){
                            tNombreCond.setText(conductores.getEnombrecond());
                            break;
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }

    private void leer_base_de_datos_Acudiente(final String documentoestudiante, final String estado) {

        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                boolean FlagNoEncontrado = true;

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getDocumentoestudiante().equals(documentoestudiante)){
                            usuarios.setConductor(firebaseAuth.getCurrentUser().getEmail());
                            usuarios.setEstado(estado);
                            //databaseReference.child("acudientes").child(usuarios.getId()).child("conductor").setValue(usuarios.getConductor()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            databaseReference.child("acudientes").child(usuarios.getId()).setValue(usuarios).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Log.d("BD_Acudientes_mod","OK");
                                    }
                                    else{
                                        Log.d("BD_Acudientes_mod","ERROR");
                                    }
                                }
                            });
                            Log.d("Correo conductor",usuarios.getConductor());
                            FlagNoEncontrado = false;
                            break;
                        }
                    }
                    if(FlagNoEncontrado == true){
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Principal_Conductor_Activity.this);
                        builder.setTitle("Error");
                        builder.setMessage("Documento no encontrado");
                        builder.setNegativeButton("Aceptar",null);
                        Dialog dialog = builder.create();
                        dialog.show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }


    @SuppressLint("NewApi")
    public void OnClickRecogerColegio(View view) {

        isLocationEnabled();
        leer_base_de_datos_Acudiente(eDocumento.getText().toString(),"bus");
        iRecoegerColegio.setVisibility(View.INVISIBLE);
    }

    public void OnClickRecogercasa(View view) {
        isLocationEnabled();
        leer_base_de_datos_Acudiente(eDocumento.getText().toString(),"bus");
        iRecoegerCasa.setVisibility(View.INVISIBLE);
    }

    public void OnClickDejarColegio(View view) {
        isLocationEnabled();
        leer_base_de_datos_Acudiente(eDocumento.getText().toString(),"colegio");
        iDejarColegio.setVisibility(View.INVISIBLE);
    }

    public void OnClickDejarCasa(View view) {
        isLocationEnabled();
        leer_base_de_datos_Acudiente(eDocumento.getText().toString(),"casa");
        iDejarCasa.setVisibility(View.INVISIBLE);
    }

}
