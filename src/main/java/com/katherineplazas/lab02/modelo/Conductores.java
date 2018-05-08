package com.katherineplazas.lab02.modelo;

/**
 * Created by KATHE on 22/04/2018.
 */

public class Conductores {
    private String id,ecorreo,enombrecond,ecedulacond,eplaca,ecidudad,etelefono;
    private String latitud_cond,longitud_cond,foto_cond,foto_docum;

    public Conductores(String id, String ecorreo, String enombrecond,
                       String ecedulacond, String eplaca, String ecidudad,
                       String etelefono, String latitud_cond, String longitud_cond,
                       String foto_cond, String foto_docum) {
        this.id = id;
        this.ecorreo = ecorreo;
        this.enombrecond = enombrecond;
        this.ecedulacond = ecedulacond;
        this.eplaca = eplaca;
        this.ecidudad = ecidudad;
        this.etelefono = etelefono;
        this.latitud_cond = latitud_cond;
        this.longitud_cond = longitud_cond;
        this.foto_cond = foto_cond;
        this.foto_docum = foto_docum;
    }

    public String getLatitud_cond() {
        return latitud_cond;
    }

    public void setLatitud_cond(String latitud_cond) {
        this.latitud_cond = latitud_cond;
    }

    public String getLongitud_cond() {
        return longitud_cond;
    }

    public void setLongitud_cond(String longitud_cond) {
        this.longitud_cond = longitud_cond;
    }

    public String getFoto_cond() {
        return foto_cond;
    }

    public void setFoto_cond(String foto_cond) {
        this.foto_cond = foto_cond;
    }

    public String getFoto_docum() {
        return foto_docum;
    }

    public void setFoto_docum(String foto_docum) {
        this.foto_docum = foto_docum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Conductores() {
    }

    public String getEcorreo() {
        return ecorreo;
    }

    public void setEcorreo(String ecorreo) {
        this.ecorreo = ecorreo;
    }



    public String getEnombrecond() {
        return enombrecond;
    }

    public void setEnombrecond(String enombrecond) {
        this.enombrecond = enombrecond;
    }

    public String getEcedulacond() {
        return ecedulacond;
    }

    public void setEcedulacond(String ecedulacond) {
        this.ecedulacond = ecedulacond;
    }

    public String getEplaca() {
        return eplaca;
    }

    public void setEplaca(String eplaca) {
        this.eplaca = eplaca;
    }

    public String getEcidudad() {
        return ecidudad;
    }

    public void setEcidudad(String ecidudad) {
        this.ecidudad = ecidudad;
    }

    public String getEtelefono() {
        return etelefono;
    }

    public void setEtelefono(String etelefono) {
        this.etelefono = etelefono;
    }
}
