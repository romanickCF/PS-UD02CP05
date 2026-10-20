package org.example;

import java.util.LinkedList;

public class Ascensor extends Thread{

    static final int NUMERO_PISOS = 4;
    boolean usuario;
    int numPisos = NUMERO_PISOS;
    int piso;
    LinkedList<Usuario> usuariosPendientes = new LinkedList<Usuario>();

    public Ascensor(){
        this.usuario = false;
        this.numPisos = NUMERO_PISOS;
    }

   public synchronized void subirPisos(){

        while(!usuario && usuariosPendientes != null){
            for(Usuario u : usuariosPendientes){
                piso = u.pisoActual;
                System.out.println("El ascensor llega al piso " + piso);
                usuario = true;
                System.out.println("El usuario "+ u.nombre + " sube al ascensor");
                piso = u.pisoElegido;
                System.out.println("El ascensor llega al piso " + piso);
                usuariosPendientes.remove(u);
                System.out.println("El usuario "+ u.nombre + " baja de el ascensor");
                notifyAll();
            }

        }

   }

    public synchronized void registrar(Usuario u) { }
    public synchronized void subirUsuario(Usuario u) { }
    public synchronized void bajarUsuario(Usuario u) { }


    @Override
    public void run() {
        subirPisos();
    }
}
