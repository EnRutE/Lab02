package com.katherineplazas.lab02.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.katherineplazas.lab02.R;
import com.katherineplazas.lab02.modelo.ParqueaderosModelo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by KATHE on 19/04/2018.
 */

public class Paqueaderos extends  RecyclerView.Adapter<Paqueaderos.ParqueaderoViewHolder>{

    private ArrayList<ParqueaderosModelo> parqueaderoList;
    private  int resource;
    private Activity activity;

    public Paqueaderos(ArrayList<ParqueaderosModelo> parqueaderoList) {
        this.parqueaderoList = parqueaderoList;
    }

    public Paqueaderos(ArrayList<ParqueaderosModelo> parqueaderoList, int resource, Activity activity) {
        this.parqueaderoList = parqueaderoList;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public ParqueaderoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemview = LayoutInflater.from(parent.getContext()).inflate(resource,parent,false);

        itemview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity,"Abre actividad co detalle ",Toast.LENGTH_SHORT).show();
            }
        });
        return new ParqueaderoViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(ParqueaderoViewHolder holder, int position) {
        ParqueaderosModelo parqueadero=parqueaderoList.get(position);
        holder.bindParqueadero(parqueadero,activity);

    }

    @Override
    public int getItemCount() {

        return parqueaderoList.size();
    }

    public class ParqueaderoViewHolder extends RecyclerView.ViewHolder{
        private TextView tnombre,ttelefono, tedad;
        private CircleImageView ifoto;

        public ParqueaderoViewHolder(View itemView) {
            super(itemView);
            tnombre=itemView.findViewById(R.id.tNombre);
            tedad=itemView.findViewById(R.id.tEdad);
            ttelefono=itemView.findViewById(R.id.tTelefono);
            ifoto=itemView.findViewById(R.id.iFoto);


        }
        public  void bindParqueadero(ParqueaderosModelo parqueadero, Activity activity){
            tnombre.setText(parqueadero.getNombre());
            tedad.setText(parqueadero.getEdad());
            ttelefono.setText(parqueadero.getTelefono());

            Picasso.get().load(parqueadero.getFoto()).into(ifoto);
        }
    }
}
