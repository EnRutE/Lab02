package com.katherineplazas.lab02;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.katherineplazas.lab02.modelo.Conductores;
import com.katherineplazas.lab02.modelo.Usuarios;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class Perfil_Conductror_Activity extends AppCompatActivity {

    private TextView tNombre,tCedula,tTelefono,tPlaca,tCiudad,tCorreo;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private CircleImageView iFoto;
    private String urlFoto = "No ha cargado";
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_perfil__conductror_);

        tNombre = findViewById(R.id.tNombreCond);
        tCedula = findViewById(R.id.tCedulaCond);
        tTelefono = findViewById(R.id.tTelefonoCond);
        tPlaca = findViewById(R.id.tPlaca);
        tCiudad = findViewById(R.id.tCiudadCond);
        tCorreo = findViewById(R.id.tCorreoCond);
        iFoto = findViewById(R.id.iFoto);

        iFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);//para abrir solo imagenes
                fotoIntent.setType("image/*");
                startActivityForResult(fotoIntent,1);
            }
        });
        bajar_url_foto_firebase();
        datos_usuario_logueado();


    }

    private void datos_usuario_logueado(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        leer_base_de_datos_Conductor(firebaseUser.getEmail());
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
                            tNombre.setText("Nombre: "+conductores.getEnombrecond());
                            tCedula.setText("Cédula: "+conductores.getEcedulacond());
                            tTelefono.setText("Teléfono: "+conductores.getEtelefono());
                            tPlaca.setText("Placa: "+conductores.getEplaca());
                            tCiudad.setText("Ciudad: "+conductores.getEcidudad());
                            tCorreo.setText(conductores.getEcorreo());
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

    @Override
    public void onBackPressed() {
        goPrincipalConductorActivity();
    }

    private void goPrincipalConductorActivity() {
        Intent intent =new Intent(Perfil_Conductror_Activity.this,Principal_Conductor_Activity.class);
        startActivity(intent);
        finish();
    }

    private void bajar_url_foto_firebase() {
        databaseReference.child("conductores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        if(conductores.getEcorreo().equals(firebaseAuth.getCurrentUser().getEmail())){
                            Picasso.get().load(conductores.getFoto_cond()).into(iFoto);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this,"Error cargando foto",Toast.LENGTH_SHORT).show();
            }else {
                Uri imagen = data.getData();//direccion de algo URL para la imagen a URL

                //convertir la imagen en un mapa de bits
                InputStream is = null;
                try {
                    is = this.getContentResolver().openInputStream(imagen);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);

                    iFoto.setImageBitmap(bitmap);
                    Guardar_foto_database(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }


            }
        }
    }

    private void Guardar_foto_database(Bitmap bitmap) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,10,baos);//Comprimir imagen
        byte[] data = baos.toByteArray();

        //crea la imagen y le cocola ese nombre
        storageReference.child("usuariosFotos").child(databaseReference.push().getKey()).
                putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                urlFoto = taskSnapshot.getDownloadUrl().toString();
                subir_url_foto_firebase(urlFoto);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage().toString());
            }
        });
    }

    private void subir_url_foto_firebase(final String UrlFoto) {

        databaseReference.child("conductores").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Conductores conductores = snapshot.getValue(Conductores.class);
                        if(conductores.getEcorreo().equals(firebaseAuth.getCurrentUser().getEmail())){
                            conductores.setFoto_cond(UrlFoto);
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


}
