/*package com.katherineplazas.lab02;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.katherineplazas.lab02.modelo.Usuarios;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class PruebaActivity extends AppCompatActivity {

    private EditText eNombre,eEdad,eTelefono;
    private DatabaseReference databaseReference;
    private ListView lListView;
    private CircleImageView iFoto;
    private ArrayAdapter listAdapter;
    private Bitmap bitmap;
    private String urlFoto = "No ha cargado";
    private ArrayList<String > listNombres; //para cargar los nombres de los usuarios
    private ArrayList<Usuarios> listUsuarios;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true); //cache
        databaseReference=FirebaseDatabase.getInstance().getReference();//lab02-375eb
        eNombre=findViewById(R.id.eNombre);
        eEdad=findViewById(R.id.eEdad);
        eTelefono=findViewById(R.id.eTelefono);
        lListView=findViewById(R.id.lListView);
        iFoto = findViewById(R.id.iFoto);

        listNombres=new ArrayList<>();
        listUsuarios= new ArrayList<>();
        /*listAdapter=new ArrayAdapter(this,
                android.R.layout.simple_list_item_1,
                listNombres);

       // lListView.setAdapter(listAdapter);
        final UsuarioAdapter usuarioAdapter=new UsuarioAdapter(this,listUsuarios);
        lListView.setAdapter(usuarioAdapter);
        databaseReference.child("usuarios").addValueEventListener(new ValueEventListener() {
        @Override
            public void onDataChange(DataSnapshot dataSnapshot){
            listNombres.clear();
            listUsuarios.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    Usuarios usuarios = snapshot.getValue(Usuarios.class);
                    listNombres.add(usuarios.getNombre());
                    listUsuarios.add(usuarios);
                }
            }
           usuarioAdapter.notifyDataSetChanged();

        }
        @Override
        public void onCancelled(DatabaseError databaseError){

        }

        });
        lListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String uid=listUsuarios.get(position).getId();
                databaseReference.child("usuarios").child(uid).removeValue();
                listNombres.remove(position);
                listUsuarios.remove(position);
                return false;
            }
        });

    }

    public void fotoClicked(View view) {
        Intent fotoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);//para abrir solo imagenes
        fotoIntent.setType("image/*");
        startActivityForResult(fotoIntent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK){
            if(data == null){
                Toast.makeText(this,"Error cargando foto",Toast.LENGTH_SHORT).show();
            }else {
                Uri imagen = data.getData();//direccion de algo URL para la imagen a URL

                try {
                    //convertir la imagen en un mapa de bits
                    InputStream is = getContentResolver().openInputStream(imagen);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    bitmap = BitmapFactory.decodeStream(bis);

                    iFoto.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class UsuarioAdapter extends ArrayAdapter<Usuarios>{

        public UsuarioAdapter(@NonNull Context context, ArrayList<Usuarios> data) {
            super(context, R.layout.list_item,data);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


            LayoutInflater inflater=LayoutInflater.from(getContext());
            View item=inflater.inflate(R.layout.list_item,null);

            Usuarios usuarios=getItem(position);

            TextView nombre=item.findViewById(R.id.tNombre);
            nombre.setText(usuarios.getNombre());

            TextView telefono =item.findViewById(R.id.tTelefono);
            telefono.setText(usuarios.getTelefono());

            TextView edad =item.findViewById(R.id.tEdad);
            edad.setText(String.valueOf(usuarios.getEdad()));

            CircleImageView iFoto = item.findViewById(R.id.iFoto);
            Picasso.get().load(usuarios.getFoto()).into(iFoto);
            return item;
        }
    }

    public void OnGuardarClicked(View view) {
       // cont=0;
       // Usuarios usuarios=new Usuarios("user"+cont) forma 1
      /*  FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser =firebaseAuth.getCurrentUser();

        Usuarios usuarios=new Usuarios(firebaseUser.getUid(),
                firebaseUser.getDisplayName(),
                firebaseUser.getPhoneNumber(),
                0);//para verficar si un usuario ya esta registrado
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);//Comprimir imagen
        byte[] data = baos.toByteArray();

        //crea la imagen y le cocola ese nombre
        storageReference.child("usuariosFotos").child(databaseReference.push().getKey()).
                putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                urlFoto = taskSnapshot.getDownloadUrl().toString();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("error",e.getMessage().toString());
            }
        });

        //tareas asincronas para que se ejecute en segundo plano
        Usuarios usuarios = new Usuarios(databaseReference.push().getKey(),
                eNombre.getText().toString(),
                eTelefono.getText().toString(),
                urlFoto,
                Integer.valueOf(eEdad.getText().toString()));
        databaseReference.child("usuarios").child(usuarios.getId()).setValue(usuarios).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
                else{

                }
            }
        });
        //Toast.makeText(PruebaActivity.this,databaseReference.child("usuarios1").orderByChild,Toast.LENGTH_LONG).show();

    }
}
*/