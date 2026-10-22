package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws InterruptedException {

        Ascensor asc = new Ascensor(4);
        Usuario[] usuarios = {
            new Usuario("usuario 1",asc),
            new Usuario("usuario 2",asc),
            new Usuario("usuario 3",asc),
            new Usuario("usuario 4",asc)
        };

        asc.start();
        for (Usuario u: usuarios){
            u.start();
        }
        asc.join();
        for (Usuario u: usuarios){
            u.join();
        }



    }
}
