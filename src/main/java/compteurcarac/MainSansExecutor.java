package compteurcarac;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class MainSansExecutor {

    public static void main(String[] args) throws InterruptedException {
        // 1️ Liste des tâches (les URLs à analyser)
        List<CompteurDeCaracteresRunnable> taches = List.of(
                new CompteurDeCaracteresRunnable("http://www.univ-jfc.fr"),
                new CompteurDeCaracteresRunnable("https://www.irit.fr/"),
                new CompteurDeCaracteresRunnable("http://www.google.fr"),
                new CompteurDeCaracteresRunnable("https://www.netflix.com/browse"),
                new CompteurDeCaracteresRunnable("https://nodejs.org/fr")
        );

        // Pour stocker les threads créés
        List<Thread> threads = new ArrayList<>();

        // 2️ Mesure du temps global
        Instant start = Instant.now();

        // 3️ Créer et démarrer un Thread pour chaque tâche
        for (CompteurDeCaracteresRunnable tache : taches) {
            Thread t = new Thread(tache);
            threads.add(t);
            t.start();
        }

        // 4️ Attendre la fin de tous les threads (join)
        for (Thread t : threads) {
            t.join();
        }

        // 5 Récupérer les résultats de chaque tâche
        int totalCaracteres = 0;
        Duration sommeDesTemps = Duration.ZERO;

        for (CompteurDeCaracteresRunnable tache : taches) {
            ResultatDuCompte resultat = tache.getResultat();
            if (resultat != null) {
                totalCaracteres += resultat.nombreDeCaracteres;
                sommeDesTemps = sommeDesTemps.plus(resultat.tempsDeCalcul);
            }
        }

        // 6️ Afficher les résultats globaux
        System.out.printf(" Nombre total de caractères : %d %n", totalCaracteres);
        System.out.printf(" Temps effectif global ~ %d secondes %n",
                Duration.between(start, Instant.now()).toSeconds());
        System.out.printf("Somme des temps individuels ~ %d secondes %n",
                sommeDesTemps.toSeconds());

        System.out.println("\n Remarque : le temps global est plus petit que la somme des temps individuels grâce à l’exécution concurrente !");
    }
}
