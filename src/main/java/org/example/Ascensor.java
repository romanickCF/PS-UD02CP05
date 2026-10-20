package org.example;

import java.util.LinkedList;

public class Ascensor extends Thread{

    static final int NUMERO_PISOS = 4;
    boolean hayUsuario;
    int numPisos;
    int piso;
    LinkedList<Usuario> usuariosPendientes = new LinkedList<>();

    public Ascensor(){
        this.hayUsuario = false;
        this.numPisos = NUMERO_PISOS;
        this.piso = 0;
    }


    public synchronized void registrar(Usuario u) {
        System.out.println(u.getNombre()+" espera en el piso "+ u.getPisoActual());
        usuariosPendientes.add(u);
        notifyAll();
    }

    public synchronized void subirUsuario(Usuario u) {
        while (hayUsuario){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        moverAscensor(u);
        System.out.println(u.getNombre() +" sube al ascensor.");
        hayUsuario = true;
        notifyAll();
    }

    public synchronized void bajarUsuario(Usuario u) {
        while(!hayUsuario){
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        moverAscensor(u);
        System.out.println(u.getNombre() +" se baja en el piso "+ u.getPisoElegido());
        hayUsuario=false;
        usuariosPendientes.remove(u);
        notifyAll();
    }

    private void moverAscensor(Usuario u) {
        int pisoObjetivo;

        if(hayUsuario){
            pisoObjetivo = u.getPisoElegido();
        }
        else{
            pisoObjetivo = u.getPisoActual();
        }

        boolean esPositivo = (pisoObjetivo-piso) > 0;

        while(piso!=pisoObjetivo) {
            if (esPositivo) {
                System.out.println("Ascensor subiendo a piso " + (piso + 1));
                piso++;
            } else {
                System.out.println("Ascensor bajando a piso " + (piso - 1));
                piso--;
            }

        }

    }


    @Override
    public void run() {

    }
}
