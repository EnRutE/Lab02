package com.katherineplazas.lab02;


import android.*;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.HttpDataHandler;
import com.katherineplazas.lab02.modelo.Usuarios;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Protocol;




/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    MapView mapView;
    GoogleMap mMap;
    Activity activity;

    LocationManager locationManager;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private String latitud_actual,latitud_siguiente,longitud_actual,longitud_siguiente;
    private int flag_posicion = 0;
    private TextView tVelocidad;
    static LatLng punto_actual;
    static LatLng punto_siguiente;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference();


        tVelocidad = view.findViewById(R.id.tVelocidad);

        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
       /* GoogleApiClient googleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity(),this)
                .addConnectionCallbacks(this)
                .addApi(Location)
                .build();*/

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true); //añadir permisos, muestra mi ubicación

        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID); //posee nombre de la calles
        //mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
        //mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        //mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE); //solo imagen satelital
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(6.27,-75.56), 13));



        //Para capturar correo del conductor y el estado
        buscar_correo_conductor(firebaseAuth.getCurrentUser().getEmail());




        /*
        LatLng punto_actual = new LatLng(Double.valueOf(latitud_actual),Double.valueOf(longitud_actual));
        LatLng punto_siguinte = new LatLng(Double.valueOf(latitud_siguiente),Double.valueOf(longitud_sigueinte));

        if(estado == "bus"){
             mMap.addPolyline(new PolylineOptions().
                    add(punto_actual,punto_siguinte).width(3).color(Color.BLUE));
        }*/

    }

    private void buscar_correo_conductor(final String correo) {
        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(correo)){
                            if(usuarios.getEstado().equals("bus")){
                                //captura posicion actual y siguiente y traza linea en el mapa
                                leer_ubicacion_conductor(usuarios.getConductor());
                                String Dircol=usuarios.getDireccioncolegio();
                                String DirCasa=usuarios.getDireccioncasa();
                                Log.d("Dir colegio lat",Dircol.substring(0,Dircol.indexOf(',')+1));
                                Log.d("Dir colegio",Dircol);

                                LatLng Direccioncol=new LatLng(Double.valueOf( Dircol.substring(0,Dircol.indexOf(','))),Double.valueOf( Dircol.substring(Dircol.indexOf(',')+1,Dircol.length())));
                                LatLng Direccioncasa=new LatLng(Double.valueOf( DirCasa.substring(0,DirCasa.indexOf(','))),Double.valueOf( DirCasa.substring(DirCasa.indexOf(',')+1,DirCasa.length())));
                                mMap.addMarker(new MarkerOptions().
                                        position(Direccioncasa).
                                        title("Casa").
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.markercasa))); //Personalizar puntero
                                mMap.addMarker(new MarkerOptions().
                                        position(Direccioncol).
                                        title("Colegio").
                                        icon(BitmapDescriptorFactory.fromResource(R.drawable.markercolegio))); //Personalizar puntero
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Direccioncol, 15));


                            }else {

                            }
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

    private void leer_ubicacion_conductor(final String correo) {

        databaseReference.child("conductores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(correo)){
                            double velociadad = Double.valueOf(conductores.getVelocidad());
                            velociadad = velociadad*3.6;
                            tVelocidad.setText(" Velocidad: "+String.format("%.1f",velociadad)+" Km/h");
                            switch (flag_posicion){
                                case 0:
                                    latitud_actual = conductores.getLatitud_cond();
                                    longitud_actual = conductores.getLongitud_cond();
                                    punto_actual = new LatLng(Double.valueOf(latitud_actual),Double.valueOf(longitud_actual));
                                    flag_posicion = 1;
                                    break;
                                case 1:
                                    latitud_siguiente = conductores.getLatitud_cond();
                                    longitud_siguiente = conductores.getLongitud_cond();
                                    punto_siguiente = new LatLng(Double.valueOf(latitud_siguiente),Double.valueOf(longitud_siguiente));
                                    flag_posicion = 0;
                                    break;
                            }
                            if(punto_siguiente!=null&& flag_posicion==1){
                                Log.d("linea","ok");
                                mMap.addPolyline(new PolylineOptions().
                                        add(punto_actual,punto_siguiente).width(5).color(Color.BLUE));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(punto_siguiente));
                            }
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


/*    private void leer_base_de_datos_Acudientes(final String correo) {
        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if(dataSnapshot.exists()){
                    Log.d("entro","ok");
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(correo)){
                            correo_conductor = usuarios.getConductor();
                            estado = usuarios.getEstado();
                            Log.d("Correo_conductor",correo_conductor);
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




    private void leer_base_de_datos_Conductor(final String correo) {
        databaseReference.child("conductores").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){
                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        System.out.println(conductores);
                        if(conductores.getEcorreo().equals(correo)){
                            velociadad = conductores.getVelocidad();
                            switch (flag_posicion){
                                case 0:
                                    latitud_actual = conductores.getLatitud_cond();
                                    longitud_actual = conductores.getLongitud_cond();
                                    flag_posicion = 1;
                                    break;
                                case 1:
                                    latitud_siguiente = conductores.getLatitud_cond();
                                    longitud_sigueinte = conductores.getLongitud_cond();
                                    flag_posicion = 0;
                                    break;
                            }
                            Log.d("punto actual",latitud_actual+longitud_actual);
                            Log.d("punto siguiente",latitud_siguiente+longitud_sigueinte);
                            break;
                        }
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        });
    }*/

  //  public void tutorial(){
    //    new GetCordinates().execute("Calle+34ee#89-83");
    //}

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    /*private class GetCordinates extends AsyncTask<String,Void,String>{

        ProgressDialog dialog = new ProgressDialog(Principal_AcudienteActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Por favor espere");
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
                String lat = ((JSONArray)jsonObject.get("result")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lad").toString();

                String lng = ((JSONArray)jsonObject.get("result")).getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").get("lng").toString();

                if(dialog.isShowing()){
                    dialog.dismiss();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }*/
}
