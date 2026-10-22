# Simulación concurrente de un ascensor en Java

**Caso práctico 5 – UD02: Programación multihilo (CESUR)**

Repositorio: <https://github.com/romanickCF/PS-UD02CP05>

## Descripción

Simulador de un ascensor en un edificio de varios pisos mediante programación concurrente. El ascensor es un hilo y cada usuario es otro hilo. El ascensor transporta **a un solo usuario cada vez**, atendiendo a los usuarios **en orden de llegada**: va al piso de origen del primero, lo recoge, lo lleva a su destino y pasa al siguiente. Cuando ya no quedan usuarios por atender, baja solo hasta el piso -1 y el programa finaliza.

## Estructura del proyecto

```
src/main/java/org/example/
├── Main.java       Crea el ascensor y los usuarios, lanza los hilos y espera a que terminen
├── Ascensor.java   Hilo del ascensor y recurso compartido (estado + métodos sincronizados)
└── Usuario.java    Hilo de cada usuario (piso de origen, piso de destino)
```

## Diseño

### Recurso compartido

El recurso compartido es **el objeto `Ascensor` y su estado**. Todos los hilos (el ascensor y los usuarios) reciben la misma referencia y sincronizan sobre ese mismo objeto. Su estado es:

| Atributo | Función |
|---|---|
| `piso` | Piso en el que se encuentra el ascensor (empieza en 0) |
| `hayUsuario` | Indica si el ascensor lleva a un usuario |
| `usuariosPendientes` | `LinkedList<Usuario>` con los usuarios registrados, en orden de llegada. El primero es el usuario en curso |
| `totalUsuarios` | Número total de usuarios de la simulación |
| `atendidos` | Número de usuarios que ya han llegado a su destino |

### Clase `Usuario`

Cada usuario recibe un piso de origen y un piso de destino aleatorios, siempre distintos entre sí. Su `run()` tiene tres fases, cada una delegada en un método sincronizado del ascensor:

1. `registrar(u)`: se añade a la lista de pendientes. Este registro es la "llamada" al ascensor.
2. `subirUsuario(u)`: espera hasta que pueda entrar y entra.
3. `bajarUsuario(u)`: espera hasta que el ascensor llegue a su destino y sale.

### Clase `Ascensor`

Su `run()` tiene dos fases:

1. **Atender usuarios** (`atender()`): mientras queden usuarios por atender, toma el primero de la lista y fija el objetivo: su piso de origen si va vacío, o su piso de destino si ya lo lleva. Se mueve **un piso por iteración** hacia ese objetivo.
2. **Bajar solo** (`bajarSolo()`): cuando `atendidos == totalUsuarios`, baja hasta el piso -1 y muestra el mensaje de finalización.

Entre pasos hay una pausa de 300 ms (`Thread.sleep`) fuera del bloque sincronizado, para poder seguir la simulación por consola.

## Sincronización

Se usa `synchronized` con `wait()` y `notifyAll()` sobre el objeto `Ascensor`. Cada espera es un `while` que comprueba una condición concreta:

| Quién espera | Sigue esperando mientras... |
|---|---|
| Usuario antes de subir | hay alguien dentro **o** el ascensor no está en su piso de origen **o** no es el primero de la lista |
| Usuario antes de bajar | no hay nadie dentro **o** el ascensor no está en su piso de destino **o** no es el primero de la lista |
| Ascensor (sin trabajo) | la lista está vacía y aún faltan usuarios por llegar |
| Ascensor (ha llegado al objetivo) | el usuario aún no ha entrado o salido |

Cada cambio de estado relevante (registro, movimiento de un piso, entrada y salida de un usuario) termina con un `notifyAll()`, de modo que los hilos dormidos reevalúan su condición.

### Decisiones de diseño

- **Un solo monitor.** Todo el estado se protege con el cerrojo del `Ascensor`. Al haber un único recurso que bloquear, no hay posibilidad de interbloqueo por orden de adquisición de cerrojos.
- **`wait()` siempre dentro de un `while`.** Al despertar, cada hilo vuelve a comprobar su condición, porque otro hilo pudo haber cambiado el estado antes.
- **El ascensor se mueve piso a piso soltando el monitor entre pasos.** Así los usuarios pueden entrar y salir entre un piso y otro, y la ejecución es realmente concurrente.
- **Solo el hilo del ascensor modifica `piso`.** Los usuarios nunca mueven el ascensor; solo cambian `hayUsuario` y la lista al entrar y salir.
- **Orden de atención.** El usuario permanece en la lista hasta que baja, así que el primero de la lista siempre es el usuario en curso. Esto garantiza que solo entra uno cada vez, aunque varios esperen en el mismo piso.
- **Condición de parada.** El ascensor conoce de antemano el número total de usuarios y cuenta los atendidos. Así distingue "aún no se ha registrado nadie" de "ya no vendrá nadie", y no baja al -1 antes de tiempo.
- **Orden de registro no determinista.** Los hilos usuario se registran en el orden en que el planificador los ejecuta, por lo que el orden de atención puede variar entre ejecuciones. Una vez registrados, se atienden estrictamente en orden de la lista.

## Salida por consola

El programa muestra los siguientes mensajes:

- `usuario N espera en el piso X`: el usuario se ha registrado.
- `Ascensor en piso X`: cada movimiento piso a piso del ascensor.
- `usuario N sube al ascensor.`: el usuario entra.
- `usuario N se baja en el piso X`: el usuario sale en su destino.
- `Ascensor llegó a piso -1. Fin del programa.`: mensaje de finalización.

## Parámetros configurables

| Parámetro | Dónde | Valor actual |
|---|---|---|
| Número de pisos | `Ascensor.NUMERO_PISOS` | 6 (pisos 0 a 5) |
| Número de usuarios | `new Ascensor(n)` en `Main` y usuarios creados | 4 |
| Pausa entre movimientos | `Thread.sleep(...)` en `Ascensor.run()` | 300 ms |

Si se cambia el número de usuarios, hay que crear tantos objetos `Usuario` como indique el argumento del constructor de `Ascensor`; si no, el ascensor esperaría indefinidamente a usuarios que nunca llegan.

## Ejecución

Requisitos: JDK compatible con la versión configurada en el `pom.xml` (Java 27, con `main` de instancia sin `public`) y Maven, o bien un IDE como IntelliJ IDEA.

- **Desde el IDE:** abrir el proyecto y ejecutar `Main`.
- **Desde Maven:**
  ```bash
  mvn compile
  java -cp target/classes org.example.Main
  ```

## Tecnologías

- Java
- Maven
- `Thread`, `synchronized`, `wait()`, `notifyAll()`, `join()`, `LinkedList`