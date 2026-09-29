package TP2;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        // Partie 1 : Capteur est désormais abstraite (Partie 11), l'instruction suivante ne compile plus :
        // Capteur c = new Capteur("C99", "Couloir");
        // -> error: Capteur is abstract; cannot be instantiated

        // Partie 2
        System.out.println("=== Partie 2 : héritage ===");
        CapteurTemperature t1 = new CapteurTemperature("T01", "Salle A", 21.5);
        t1.afficherInformations();
        t1.desactiver();
        System.out.println("Après desactiver() : actif = " + t1.isActif());
        t1.activer();

        // Parties 3 et 4
        System.out.println("\n=== Parties 3 et 4 : redéfinition ===");
        CapteurLuminosite l1 = new CapteurLuminosite("L01", "Hall", 640);
        DetecteurPresence p1 = new DetecteurPresence("P01", "Salle B", true);
        t1.afficherInformations();
        System.out.println();
        l1.afficherInformations();
        System.out.println();
        p1.afficherInformations();

        // Partie 5
        System.out.println("\n=== Partie 5 : références de type Capteur ===");
        Capteur c1 = t1;
        Capteur c2 = l1;
        Capteur c3 = p1;
        c1.afficherInformations();
        System.out.println();
        c2.afficherInformations();
        System.out.println();
        c3.afficherInformations();

        // Partie 7
        System.out.println("\n=== Partie 7 : méthode polymorphe ===");
        afficherCapteur(t1);
        System.out.println();
        afficherCapteur(l1);
        System.out.println();
        afficherCapteur(p1);

        // Partie 8
        System.out.println("\n=== Partie 8 : collection de capteurs ===");
        ArrayList<Capteur> capteurs = new ArrayList<>();
        capteurs.add(t1);
        capteurs.add(l1);
        capteurs.add(p1);
        for (Capteur capteur : capteurs) {
            capteur.afficherInformations();
            System.out.println();
        }

        // Partie 9
        System.out.println("=== Partie 9 : analyses ===");
        analyserTous(capteurs);

        // Partie 10
        System.out.println("\n=== Partie 10 : modification des valeurs ===");
        t1.setTemperature(31.2);
        l1.setLuminosite(50);
        p1.setPresence(false);
        analyserTous(capteurs);
    }

    public static void afficherCapteur(Capteur capteur) {
        capteur.afficherInformations();
    }

    private static void analyserTous(ArrayList<Capteur> capteurs) {
        for (Capteur capteur : capteurs) {
            System.out.print(capteur.getIdentifiant() + " : ");
            capteur.analyser();
        }
    }

}
