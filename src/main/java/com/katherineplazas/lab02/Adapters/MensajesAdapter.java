package com.katherineplazas.lab02.Adapters;

/**
 * Created by KATHE on 30/04/2018.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.katherineplazas.lab02.R;
import com.katherineplazas.lab02.modelo.Mensajes;
import com.katherineplazas.lab02.modelo.ParqueaderosModelo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MensajesAdapter extends  RecyclerView.Adapter<MensajesAdapter.MensajesViewHolder> {
    private ArrayList<Mensajes> mensajesList;
    private  int resource;
    private Activity activity;




    public MensajesAdapter(ArrayList<Mensajes> mensajesList) {

        this.mensajesList = mensajesList;
    }

    public MensajesAdapter(ArrayList<Mensajes> mensajesList, int resource, Activity activity) {
        this.mensajesList = mensajesList;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public MensajesViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        final View itemview = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("Mensaje");
                builder.setMessage(mensajesList.get(parent.indexOfChild(itemview)).getCuerpo());
                builder.setNegativeButton("Aceptar",null);
                Dialog dialog = builder.create();
                dialog.show();
            }
        });
        return new MensajesViewHolder(itemview);
    }




    @Override
    public void onBindViewHolder(MensajesViewHolder holder, int position) {
        Mensajes mensajes=mensajesList.get(position);
        holder.bindMensajes(mensajes,activity);

    }

    @Override
    public int getItemCount() {

        return mensajesList.size();
    }



    public class MensajesViewHolder extends RecyclerView.ViewHolder{
        private TextView tAsunto;
        private CircleImageView ifoto;
        private TextView tFecha;

        public MensajesViewHolder(View itemView) {
            super(itemView);
            tAsunto=itemView.findViewById(R.id.tNombre);
            ifoto=itemView.findViewById(R.id.iFoto);
            tFecha = itemView.findViewById(R.id.tTelefono);

        }
        public  void bindMensajes(Mensajes mensajes, Activity activity){

           tAsunto.setText(mensajes.getAsunto());
           Picasso.get().load(mensajes.getFoto()).into(ifoto);
           tFecha.setText(mensajes.getFecha());

        }
    }
}
