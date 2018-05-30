package com.katherineplazas.lab02.modelo;

/**
 * Created by KATHE on 05/04/2018.
 */

public class Usuarios {
    private String id, nombre, telefono,foto,correo,cedula,estudiante,documentoestudiante;
    private  String direccioncasa,colegio,direccioncolegio,rh,estado,conductor,foto_estudiante,foto_acudiente;

    public Usuarios(String id, String nombre, String telefono,
                    String correo, String cedula, String estudiante,
                    String documentoestudiante, String direccioncasa, String colegio,
                    String direccioncolegio, String rh, String estado, String conductor,
                    String foto_estudiante,String foto_acudiente) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.cedula = cedula;
        this.estudiante = estudiante;
        this.documentoestudiante = documentoestudiante;
        this.direccioncasa = direccioncasa;
        this.colegio = colegio;
        this.direccioncolegio = direccioncolegio;
        this.rh = rh;
        this.estado = estado;
        this.conductor = conductor;
        this.foto_estudiante = foto_estudiante;
        this.foto_acudiente = foto_acudiente;

    }


    public String getFoto_estudiante() {
        return foto_estudiante;
    }

    public void setFoto_estudiante(String foto_estudiante) {
        this.foto_estudiante = foto_estudiante;
    }

    public String getFoto_acudiente() {
        return foto_acudiente;
    }

    public void setFoto_acudiente(String foto_acudiente) {
        this.foto_acudiente = foto_acudiente;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getConductor() {
        return conductor;
    }

    public void setConductor(String conductor) {
        this.conductor = conductor;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(String estudiante) {
        this.estudiante = estudiante;
    }

    public String getDocumentoestudiante() {
        return documentoestudiante;
    }

    public void setDocumentoestudiante(String documentoestudiante) {
        this.documentoestudiante = documentoestudiante;
    }

    public String getDireccioncasa() {
        return direccioncasa;
    }

    public void setDireccioncasa(String direccioncasa) {
        this.direccioncasa = direccioncasa;
    }

    public String getColegio() {
        return colegio;
    }

    public void setColegio(String colegio) {
        this.colegio = colegio;
    }

    public String getDireccioncolegio() {
        return direccioncolegio;
    }

    public void setDireccioncolegio(String direccioncolegio) {
        this.direccioncolegio = direccioncolegio;
    }

    public String getRh() {
        return rh;
    }

    public void setRh(String rh) {
        this.rh = rh;
    }

    public Usuarios(String id, String nombre, String telefono, int edad) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Usuarios() {
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }


}
