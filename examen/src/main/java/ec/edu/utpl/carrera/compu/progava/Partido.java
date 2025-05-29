package ec.edu.utpl.carrera.compu.progava;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.Random;

public class Partido implements Callable<List<Jugador>> {
    private final List<Jugador> jugadores;
    private final String nombreRonda;
    private final Random rand = new Random();

    public Partido(List<Jugador> jugadores, String nombreRonda) {
        this.jugadores = jugadores;
        this.nombreRonda = nombreRonda;
    }

    @Override
    public List<Jugador> call() {
        double tiempo = 1.5 + (0.5 * rand.nextDouble());
        long milisegundos = (long)(tiempo * 1000);
        try {
            Thread.sleep(milisegundos); // Simula el tiempo del partido
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n--- " + nombreRonda + " ---");
        List<Jugador> ganadores = new ArrayList<>();

        for (int i = 0; i < jugadores.size(); i += 2) {
            Jugador j1 = jugadores.get(i);

            Jugador j2 = jugadores.get(i + 1);
            System.out.println("Jugador " + j1.getNumeroJugador() + " vs Jugador " + j2.getNumeroJugador());
            int setsJ1 = 0;
            int setsJ2 = 0;
            int setsJugados = 0;


            // Simulación de sets
            while ((setsJ1 < 2 && setsJ2 < 2) && setsJugados < 3) {
                setsJugados++;
                Jugador ganadorSet = Math.random() < 0.5 ? j1 : j2;
                if (ganadorSet.equals(j1)) {
                    setsJ1++;
                } else {
                    setsJ2++;
                }
                System.out.println("Set " + setsJugados + ":" + ganadorSet.getNumeroJugador());
            }

            Jugador ganador = setsJ1 > setsJ2 ? j1 : j2;
            ganadores.add(ganador);

            System.out.println(j1.getNumeroJugador() + " vs " + j2.getNumeroJugador()
                    + " => Resultado: " + setsJ1 + "-" + setsJ2 + " => Gana: " + ganador.getNumeroJugador());
        }

        return ganadores;
    }
}
