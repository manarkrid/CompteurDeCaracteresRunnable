package compteurcarac;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.concurrent.*;

public class MainAvecExecutor {

    public static final int MAX_THREADS_SIMULT = 10; 

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // 1️ Liste des tâches (Callable)
        List<CompteurDeCaracteresCallable> taches = List.of(
                new CompteurDeCaracteresCallable("http://www.univ-jfc.fr"),
                new CompteurDeCaracteresCallable("https://www.irit.fr/"),
                new CompteurDeCaracteresCallable("http://www.google.fr"),
                new CompteurDeCaracteresCallable("https://www.netflix.com/browse"),
                new CompteurDeCaracteresCallable("https://nodejs.org/fr")
        );

        // 2️ Création du pool de threads
        ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS_SIMULT);

        try {
            Instant start = Instant.now();
            long totalCaracteres = 0;
            Duration sommeDesTemps = Duration.ZERO;

            // 3️ Soumission des tâches au pool (bloquant)
            List<Future<ResultatDuCompte>> resultatsFuturs = executor.invokeAll(taches);

            // 4️ Récupération des résultats
            for (Future<ResultatDuCompte> futur : resultatsFuturs) {
                ResultatDuCompte resultat = futur.get(); // get() est bloquant si le calcul n'est pas fini
                totalCaracteres += resultat.nombreDeCaracteres;
                sommeDesTemps = sommeDesTemps.plus(resultat.tempsDeCalcul);
            }

            // 5️ Affichage des résultats globaux
            System.out.printf("Nombre total de caractères : %d %n", totalCaracteres);
            System.out.printf("Temps effectif de calcul ~ %d secondes %n",
                    Duration.between(start, Instant.now()).toSeconds());
            System.out.printf("Somme des temps individuels ~ %d secondes %n",
                    sommeDesTemps.toSeconds());

        } finally {
            // 6️ Fermeture du pool
            executor.shutdown();
        }
    }
}
