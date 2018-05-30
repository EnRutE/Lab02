package com.katherineplazas.lab02;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
import com.katherineplazas.lab02.modelo.HttpDataHandler;
import com.katherineplazas.lab02.modelo.Usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LocationManager locationManager;

    String eMail, ePassword, eNombre,eCedula,eTelefono,eNombreEstud,eDocumentoEstud;
    String eColegio,eRh;
    String direccioncolegio,direccioncasa;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    static private LatLng DirCol;
    private boolean FlagMarker = false;
    private Button bFinalizar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        eMail = extras.getString("Correo");
        ePassword = extras.getString("Contrasena");
        eNombre = extras.getString("Nombre");
        eCedula = extras.getString("Cedula");
        eTelefono = extras.getString("Telefono");
        eNombreEstud = extras.getString("NombreEstud");
        eDocumentoEstud = extras.getString("DocuementoEstud");
        eColegio = extras.getString("Colegio");
        eRh = extras.getString("Rh");

        tutorial(eColegio);

        bFinalizar = findViewById(R.id.bfinalizar);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        //Obtain the SupportMapFragment and get notified when the map is ready to be used.

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    private void goPrincipalAcudiente() {
        Intent intent =new Intent(MapsActivity.this,Principal_AcudienteActivity.class);
        startActivity(intent);
        finish();
    }

    private void goLoginActivity() {
        Intent intent =new Intent(MapsActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        goLoginActivity();
    }

    private void crearBasedeDados_Acudiente() {

        Usuarios usuarios = new Usuarios(databaseReference.push().getKey(),
                eNombre,
                eTelefono,
                eMail,
                eCedula,
                eNombreEstud,
                eDocumentoEstud,
                direccioncasa,
                eColegio,
                direccioncolegio,
                eRh,
                "N",
                "N",
                "foto_estudiante",
                "https://firebasestorage.googleapis.com/v0/b/lab02-375eb.appspot.com/o/usuariosFotos%2Fusuario_foto.png?alt=media&token=4ff39136-9edc-4c86-8c89-2eeb081df67d");

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
                            Toast.makeText(MapsActivity.this,"Cuenta creada",Toast.LENGTH_SHORT).show();
                            crearBasedeDados_Acudiente();
                            goPrincipalAcudiente();
                        }else {
                            Toast.makeText(MapsActivity.this,"Correo no válido",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void tutorial(String nombrecolegio){

        nombrecolegio.replace(" ","+");
          new GetCordinates().execute(nombrecolegio+",+medellin,+CA&key=AIzaSyBxJ7IsRRR4byKh3WhDYF4RWTlP5m34HfM");
        }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        bFinalizar = findViewById(R.id.bfinalizar);
        mMap = googleMap;
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {

            @Override
            public void onMapLongClick(LatLng latLng) {
                LatLng DirCasa=latLng;
                mMap.moveCamera(CameraUpdateFactory.newLatLng(DirCasa));
                if(FlagMarker == false){
                    mMap.addMarker(new MarkerOptions().
                            position(DirCasa).
                            icon(BitmapDescriptorFactory.fromResource(R.drawable.markercasa))); //Personalizar puntero
                    bFinalizar.setEnabled(true);
                    direccioncasa = String.valueOf(DirCasa.latitude)+","+String.valueOf(DirCasa.longitude);
                    FlagMarker = true;
                }


            }
        });
    }

    private void leer_escribir_base_de_datos_Acudiente(final String email, final String s, final boolean FlagCasaColegio) {
        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(email)){
                            if(FlagCasaColegio){
                                usuarios.setDireccioncasa(s);
                            }else {
                                usuarios.setDireccioncolegio(s);
                            }

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

    public void OnClickFinalizar(View view) {

        crearCuentaFirebase(eMail, ePassword);
    }

    private class GetCordinates extends AsyncTask<String,Void,String> {

        ProgressDialog dialog = new ProgressDialog(MapsActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Buscando colegio");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String response;
            try {
                String address = strings[0];
                HttpDataHandler http = new HttpDataHandler();
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s",address);
                Log.d("-------------:url",url.toString());
                response = http.getHTTPData(url);
                return response;
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                JSONObject jsonObject = new JSONObject(s);

                String lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lat").toString();

                String lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                double latitud=Double.parseDouble(lat);
                double longitud =Double.parseDouble(lng);

                DirCol=new LatLng(latitud,longitud);
                direccioncolegio = lat+","+lng;

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DirCol, 13));
                mMap.addMarker(new MarkerOptions().
                        position(DirCol).
                        icon(BitmapDescriptorFactory.fromResource(R.drawable.markercolegio))); //Personalizar puntero
                Toast.makeText(MapsActivity.this,"Para ubicar su casa mantenga presionado el mapa",Toast.LENGTH_LONG).show();

                if(dialog.isShowing()){
                    dialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}
