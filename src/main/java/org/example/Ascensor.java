package org.example;

import java.util.LinkedList;

public class Ascensor extends Thread{

    static final int NUMERO_PISOS = 4;
    boolean hayUsuario;
    int numPisos;
    LinkedList<Usuario> usuariosPendientes = new LinkedList<>();

    public Ascensor(){
        this.hayUsuario = false;
        this.numPisos = NUMERO_PISOS;
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
        System.out.println(u.getNombre() +" se baja en el piso "+ u.getPisoElegido());
        hayUsuario=false;
        notifyAll();
    }


    @Override
    public void run() {

    }
}
