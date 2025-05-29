package ec.edu.utpl.carrera.compu.progava;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) {
        // Crear 16 jugadores numerados del 1 al 16
        List<Jugador> jugadores = new ArrayList<>();
        for (int i = 1; i <= 16; i++) {
            jugadores.add(new Jugador(i)); // Constructor con número
        }

        ExecutorService executor = Executors.newFixedThreadPool(4); // Ajusta el tamaño del pool

        try {
            // Octavos
            jugadores = ejecutarRonda(jugadores, "Octavos de final", executor);

            // Cuartos
            jugadores = ejecutarRonda(jugadores, "Cuartos de final", executor);

            // Semifinal
            jugadores = ejecutarRonda(jugadores, "Semifinal", executor);

            // Final
            jugadores = ejecutarRonda(jugadores, "Final", executor);

            System.out.println("\n🏆 GANADOR DEL TORNEO: Jugador " + jugadores.get(0).getNumeroJugador());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    public static List<Jugador> ejecutarRonda(List<Jugador> jugadores, String nombreRonda, ExecutorService executor) throws InterruptedException, ExecutionException {
        List<Callable<List<Jugador>>> tareas = new ArrayList<>();

        // Ordenar jugadores por numeroJugador para emparejarlos 1vs16, 2vs15...
        jugadores.sort(Comparator.comparingInt(Jugador::getNumeroJugador));

        int n = jugadores.size();
        for (int i = 0; i < n / 2; i++) {
            List<Jugador> par = new ArrayList<>();
            par.add(jugadores.get(i));
            par.add(jugadores.get(n - 1 - i));
            tareas.add(new Partido(par, nombreRonda));
        }

        // Enviar partidos y esperar resultados
        List<Future<List<Jugador>>> futuros = executor.invokeAll(tareas);

        List<Jugador> ganadores = new ArrayList<>();
        for (Future<List<Jugador>> f : futuros) {
            ganadores.addAll(f.get());
        }

        return ganadores;
    }
}
