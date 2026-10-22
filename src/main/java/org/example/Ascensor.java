package org.example;

import java.util.LinkedList;

public class Ascensor extends Thread{

    static final int NUMERO_PISOS = 6;
    private boolean hayUsuario;
    private int piso;
    private final int totalUsuarios;
    private int atendidos = 0;


    LinkedList<Usuario> usuariosPendientes = new LinkedList<>();

    public Ascensor(int totalUsuarios){
        this.hayUsuario = false;
        this.piso = 0;
        this.totalUsuarios = totalUsuarios;
    }


    public synchronized void registrar(Usuario u) {
        System.out.println(u.getNombre()+" espera en el piso "+ u.getPisoActual());
        usuariosPendientes.add(u);
        notifyAll();
    }

    public synchronized void subirUsuario(Usuario u) {
        while (hayUsuario || piso != u.getPisoActual() ||!u.equals(usuariosPendientes.getFirst())) {
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
        while(!hayUsuario || piso != u.getPisoElegido() ||!u.equals(usuariosPendientes.getFirst())) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println(u.getNombre() +" se baja en el piso "+ u.getPisoElegido());
        hayUsuario=false;
        usuariosPendientes.remove(u);
        atendidos++;
        notifyAll();
    }

    @Override
    public void run() {
        try {
            while (atender()) {
                Thread.sleep(300);
            }
            while (bajarSolo()) {
                Thread.sleep(300);
            }
            System.out.println("Ascensor llegó a piso -1. Fin del programa.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private synchronized boolean atender() throws InterruptedException {
        while(usuariosPendientes.isEmpty() && atendidos < totalUsuarios){
            wait();
        }
        if(atendidos == totalUsuarios){
            return false;
        }
        Usuario u = usuariosPendientes.getFirst();
        int objetivo = hayUsuario ? u.getPisoElegido() : u.getPisoActual();

        if (piso == objetivo) {
            wait();
        } else {
            piso += (objetivo > piso) ? 1 : -1;
            System.out.println("Ascensor en piso " + piso);
            notifyAll();
        }
        return true;
    }


    private synchronized boolean bajarSolo() {
        if (piso > -1) {
            piso--;
            System.out.println("Ascensor en piso " + piso);
            notifyAll();
            return true;
        }
        return false;
    }
}
