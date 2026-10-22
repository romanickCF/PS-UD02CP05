package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws InterruptedException {

        Ascensor asc = new Ascensor(4);

        Usuario u1 = new Usuario("usuario 1",asc);
        Usuario u2 = new Usuario("usuario 2",asc);
        Usuario u3 = new Usuario("usuario 3",asc);
        Usuario u4 = new Usuario("usuario 4",asc);



        asc.start();
        u1.start();
        u2.start();
        u3.start();
        u4.start();

        asc.join();
        u1.join();
        u2.join();
        u3.join();
        u4.join();



    }
}
