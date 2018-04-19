package com.katherineplazas.lab02.modelo;

/**
 * Created by KATHE on 19/04/2018.
 */

public class ParqueaderosModelo {
    String nombre,foto, edad,telefono;
//hola
    public  ParqueaderosModelo(){

    }
    public ParqueaderosModelo(String nombre, String foto, String edad, String telefono){
        this.nombre=nombre;
        this.foto=foto;
        this.edad=edad;
        this.telefono=telefono;
    }

    public String getNombre() {
        return nombre;
    }

    public String getFoto() {
        return foto;
    }

    public String getEdad() {
        return edad;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
