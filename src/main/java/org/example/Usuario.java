package org.example;

import java.util.Random;

import static org.example.Ascensor.NUMERO_PISOS;

public class Usuario extends Thread {

    String nombre;
    int pisoActual;
    int pisoElegido;
    Ascensor asc;


    public Usuario(String nombre, Ascensor asc){
        this.nombre = nombre;

        this.pisoActual = generarPisoAleatorio();

       int aleatorio = generarPisoAleatorio();
       while(aleatorio == pisoActual){
           aleatorio = generarPisoAleatorio();
       }
        this.pisoElegido = aleatorio ;
       this.asc = asc;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPisoActual() {
        return pisoActual;
    }

    public void setPisoActual(int pisoActual) {
        this.pisoActual = pisoActual;
    }

    public int getPisoElegido() {
        return pisoElegido;
    }

    public void setPisoElegido(int pisoElegido) {
        this.pisoElegido = pisoElegido;
    }

    public Ascensor getAsc() {
        return asc;
    }

    public void setAsc(Ascensor asc) {
        this.asc = asc;
    }

    private int generarPisoAleatorio(){
        Random rand = new Random();
        return rand.nextInt(NUMERO_PISOS);
    }

    @Override
    public void run() {
        asc.registrar(this);
        asc.subirUsuario(this);
        asc.bajarUsuario(this);
    }


    }

