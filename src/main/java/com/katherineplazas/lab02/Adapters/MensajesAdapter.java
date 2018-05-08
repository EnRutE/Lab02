package com.katherineplazas.lab02.Adapters;

/**
 * Created by KATHE on 30/04/2018.
 */
import android.app.Activity;
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
    public MensajesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Abre actividad co detalle ",Toast.LENGTH_SHORT).show();
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
        private TextView tAsunto,tCuerpo;
        private CircleImageView ifoto;

        public MensajesViewHolder(View itemView) {
            super(itemView);
            tAsunto=itemView.findViewById(R.id.tNombre);
            tCuerpo=itemView.findViewById(R.id.tTelefono);
            ifoto=itemView.findViewById(R.id.iFoto);


        }
        public  void bindMensajes(Mensajes mensajes, Activity activity){

           tAsunto.setText(mensajes.getAsunto());
           tCuerpo.setText(mensajes.getCuerpo());

           Picasso.get().load(mensajes.getFoto()).into(ifoto);

        }
    }
}
