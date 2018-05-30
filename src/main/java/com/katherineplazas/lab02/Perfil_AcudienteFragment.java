package com.katherineplazas.lab02;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil_AcudienteFragment extends Fragment {

    private TextView tNombre, tCedula,tTelefono,tNombre_est,tDocumento_est,tColegio,tRh,tCorreo;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference databaseReference;
    private CircleImageView iFoto;
    private String urlFoto = "No ha cargado";
    private Bitmap bitmap;


    public Perfil_AcudienteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=inflater.inflate(R.layout.fragment_perfil__acudiente, container, false);
       tNombre = view.findViewById(R.id.tNombre);
       tCedula = view.findViewById(R.id.tCedula);
       tTelefono = view.findViewById(R.id.tTelefono);
       tNombre_est = view.findViewById(R.id.tNombre_est);
       tDocumento_est = view.findViewById(R.id.tDocumento_est);
       tColegio = view.findViewById(R.id.tColegio);
       tRh = view.findViewById(R.id.tRh);
       tCorreo = view.findViewById(R.id.tCorreo);
       iFoto = view.findViewById(R.id.iFoto);
       databaseReference= FirebaseDatabase.getInstance().getReference();
       inicializar();
       iFoto.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent fotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);//para abrir solo imagenes
               fotoIntent.setType("image/*");
               startActivityForResult(fotoIntent,1);

           }
       });
       bajar_url_foto_firebase();
       return view;

    }

    private void leer_base_de_datos_Acudiente(final String correo) {

        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(correo)){
                           tNombre.setText("Nombre: "+usuarios.getNombre());
                           tCedula.setText("Cédula: "+usuarios.getCedula());
                           tTelefono.setText("Teléfono: "+usuarios.getTelefono());
                           tNombre_est.setText("Estudiante: "+usuarios.getEstudiante());
                           tDocumento_est.setText("Dcumento estudiante: "+usuarios.getDocumentoestudiante());
                           tColegio.setText("Colegio: "+usuarios.getColegio());
                           tRh.setText("RH: "+usuarios.getRh());
                           tCorreo.setText(usuarios.getCorreo());
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

    private void inicializar(){
        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        leer_base_de_datos_Acudiente(firebaseUser.getEmail());
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == getActivity().RESULT_OK){
            if(data == null){
                Toast.makeText(getContext(),"Error cargando foto",Toast.LENGTH_SHORT).show();
            }else {
                Uri imagen = data.getData();//direccion de algo URL para la imagen a URL

                    //convertir la imagen en un mapa de bits
                InputStream is = null;
                try {
                    is = getActivity().getContentResolver().openInputStream(imagen);
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

        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(firebaseAuth.getCurrentUser().getEmail())){
                            usuarios.setFoto_acudiente(UrlFoto);
                            Log.d("urlFoto",UrlFoto);

                            databaseReference.child("acudientes").child(usuarios.getId()).setValue(usuarios);
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

    private void bajar_url_foto_firebase() {
        databaseReference.child("acudientes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){

                if(dataSnapshot.exists()){

                    for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                        Usuarios usuarios = snapshot.getValue(Usuarios.class);
                        if(usuarios.getCorreo().equals(firebaseAuth.getCurrentUser().getEmail())){
                            Picasso.get().load(usuarios.getFoto_acudiente()).into(iFoto);
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
