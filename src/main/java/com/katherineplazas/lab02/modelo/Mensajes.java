package com.katherineplazas.lab02.modelo;

/**
 * Created by KATHE on 23/04/2018.
 */

public class Mensajes {
    private String asunto, cuerpo, id, idconductor,foto,fecha;

    public Mensajes() {
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Mensajes(String id, String asunto, String cuerpo, String idconductor, String foto,String fecha) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.id = id;
        this.idconductor = idconductor;
        this.foto = foto;
        this.fecha = fecha;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdconductor() {
        return idconductor;
    }

    public void setIdconductor(String idconductor) {
        this.idconductor = idconductor;
    }
}
