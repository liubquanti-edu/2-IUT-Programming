package TP3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

// Parties 1 à 15 du TP (l'application finale de la Partie 17 est dans Main)
// Les fichiers sont relatifs au dossier 2-IUT-Programming, depuis lequel le programme est lancé
public class Exercices {

    // Les capteurs créés directement dans main ont des valeurs valides :
    // ValeurCapteurInvalideException ne peut pas se produire à leur construction
    public static void main(String[] args) throws ValeurCapteurInvalideException {
        Scanner scanner = new Scanner(System.in);

        // Partie 1 et 2 : intercepter une exception
        System.out.println("=== Parties 1 et 2 : intercepter une exception ===");
        CapteurTemperature t1 = new CapteurTemperature("T01", "Salle A", 21.5);
        t1.afficherInformations();

        System.out.print("\nNouvelle température : ");
        String saisie = scanner.nextLine();
        try {
            double temperature = Double.parseDouble(saisie);
            t1.setTemperature(temperature);
            System.out.println("Température modifiée.");
        } catch (NumberFormatException e) {
            System.out.println("Erreur : veuillez saisir un nombre valide.");
        } catch (ValeurCapteurInvalideException e) {
            // Parties 4 et 7 : valeur hors de l'intervalle autorisé
            System.out.println("Erreur : " + e.getMessage());
        }

        // Le programme continue, que la saisie soit correcte ou non
        System.out.println();
        t1.afficherInformations();
        essayerAnalyser(t1);

        // Partie 3 : plusieurs types d'erreurs
        System.out.println("\n=== Partie 3 : plusieurs types d'erreurs ===");
        CapteurLuminosite l1 = new CapteurLuminosite("L01", "Hall", 640);
        DetecteurPresence p1 = new DetecteurPresence("P01", "Salle B", true);
        ArrayList<Capteur> capteurs = new ArrayList<>();
        capteurs.add(t1);
        capteurs.add(l1);
        capteurs.add(p1);

        for (int i = 0; i < capteurs.size(); i++) {
            System.out.println(i + " - " + capteurs.get(i).getIdentifiant());
        }

        System.out.print("Numéro du capteur : ");
        saisie = scanner.nextLine();
        try {
            int numero = Integer.parseInt(saisie);
            Capteur capteur = capteurs.get(numero);
            System.out.println();
            capteur.afficherInformations();
        } catch (NumberFormatException e) {
            System.out.println("Erreur : vous devez saisir un nombre.");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Erreur : aucun capteur ne correspond à ce numéro.");
        }

        // Partie 4 : déclencher volontairement une exception
        System.out.println("\n=== Partie 4 : déclencher volontairement une exception ===");
        double[] temperatures = {22.5, -70, 150};
        for (double temperature : temperatures) {
            try {
                t1.setTemperature(temperature);
                System.out.println("t1.setTemperature(" + temperature + ") : OK");
            } catch (ValeurCapteurInvalideException e) {
                System.out.println("t1.setTemperature(" + temperature + ") : Erreur : " + e.getMessage());
            }
        }

        double[] luminosites = {600, 0, -50};
        for (double luminosite : luminosites) {
            try {
                l1.setLuminosite(luminosite);
                System.out.println("l1.setLuminosite(" + luminosite + ") : OK");
            } catch (ValeurCapteurInvalideException e) {
                System.out.println("l1.setLuminosite(" + luminosite + ") : Erreur : " + e.getMessage());
            }
        }

        // Partie 5 : une exception propre à SmartBuilding
        System.out.println("\n=== Partie 5 : une exception propre à SmartBuilding ===");
        System.out.println("Capteur actif :");
        essayerAnalyser(t1);

        System.out.println("Capteur désactivé :");
        t1.desactiver();
        essayerAnalyser(t1);
        t1.activer();

        System.out.println("\nAnalyse de tous les capteurs (P01 désactivé) :");
        p1.desactiver();
        for (Capteur capteur : capteurs) {
            System.out.print(capteur.getIdentifiant() + " : ");
            essayerAnalyser(capteur);
        }
        p1.activer();

        // Partie 6 : propagation d'une exception
        System.out.println("\n=== Partie 6 : propagation d'une exception ===");
        l1.desactiver();
        try {
            analyserCapteur(t1);
            analyserCapteur(l1);
            analyserCapteur(p1);
        } catch (CapteurInactifException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        l1.activer();

        // Partie 7 : une hiérarchie d'exceptions
        System.out.println("\n=== Partie 7 : une hiérarchie d'exceptions ===");
        p1.desactiver();
        System.out.println("Chaque type d'exception traité séparément :");
        for (int test = 1; test <= 4; test++) {
            try {
                executerTestPartie7(test, t1, l1, p1);
            } catch (ValeurCapteurInvalideException e) {
                System.out.println("  -> [valeur invalide] " + e.getMessage());
            } catch (CapteurInactifException e) {
                System.out.println("  -> [capteur inactif] " + e.getMessage());
            }
        }

        System.out.println("\nToutes les exceptions des capteurs traitées de manière commune :");
        for (int test = 1; test <= 4; test++) {
            try {
                executerTestPartie7(test, t1, l1, p1);
            } catch (CapteurException e) {
                System.out.println("  -> Erreur sur le capteur " + e.getIdentifiant() + " : " + e.getMessage());
            }
        }
        p1.activer();

        // Partie 8 : ajouter des informations dans une exception
        System.out.println("\n=== Partie 8 : ajouter des informations dans une exception ===");
        double[] temperaturesIncorrectes = {150.0, -51, 80.5, Double.NaN};
        for (double temperature : temperaturesIncorrectes) {
            try {
                t1.setTemperature(temperature);
            } catch (ValeurCapteurInvalideException e) {
                System.out.println("Valeur incorrecte pour le capteur " + e.getIdentifiant() + ".");
                System.out.println("Valeur reçue : " + e.getValeur());
            }
        }
        try {
            l1.setLuminosite(-0.5);
        } catch (ValeurCapteurInvalideException e) {
            System.out.println("Valeur incorrecte pour le capteur " + e.getIdentifiant() + ".");
            System.out.println("Valeur reçue : " + e.getValeur());
        }

        // Partie 9 : sauvegarder les capteurs dans un fichier
        System.out.println("\n=== Partie 9 : sauvegarder les capteurs dans un fichier ===");
        ArrayList<Capteur> batiment = new ArrayList<>();
        batiment.add(new CapteurTemperature("T01", "Salle A", 21.5));
        batiment.add(new CapteurLuminosite("L01", "Hall", 640));
        batiment.add(new DetecteurPresence("P01", "Salle B", true));
        try {
            FichierCapteurs.sauvegarder(batiment, "TP3/capteurs.txt");
            System.out.println("Sauvegarde dans TP3/capteurs.txt :");
            afficherFichier("TP3/capteurs.txt");
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        }

        ArrayList<Capteur> plusieurs = new ArrayList<>(batiment);
        CapteurTemperature t2 = new CapteurTemperature("T02", "Bureau", 19.0);
        t2.desactiver();
        plusieurs.add(t2);
        plusieurs.add(new DetecteurPresence("P02", "Couloir", false));
        try {
            FichierCapteurs.sauvegarder(plusieurs, "TP3/sauvegarde_test.txt");
            System.out.println("\nSauvegarde de " + plusieurs.size() + " capteurs :");
            afficherFichier("TP3/sauvegarde_test.txt");
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        }

        System.out.println("\nSauvegarde dans un dossier inexistant :");
        try {
            FichierCapteurs.sauvegarder(batiment, "TP3/dossier_inexistant/capteurs.txt");
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        }

        // Partie 10 : garantir la fermeture d'un fichier (finally)
        System.out.println("\n=== Partie 10 : finally ===");
        // Un élément null provoque une NullPointerException au milieu de l'écriture
        ArrayList<Capteur> avecErreur = new ArrayList<>(batiment);
        avecErreur.add(1, null);
        try {
            FichierCapteurs.sauvegarderAvecFinally(batiment, "TP3/sauvegarde_test.txt");
            System.out.println("Écriture normale :");
            afficherFichier("TP3/sauvegarde_test.txt");

            FichierCapteurs.sauvegarderAvecFinally(avecErreur, "TP3/sauvegarde_test.txt");
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        } catch (NullPointerException e) {
            System.out.println("Exception pendant l'écriture : " + e.getClass().getSimpleName());
            System.out.println("Contenu du fichier (vidé sur le disque par close() dans finally) :");
            afficherFichier("TP3/sauvegarde_test.txt");
        }

        // Partie 11 : try-with-resources
        System.out.println("\n=== Partie 11 : try-with-resources ===");
        try {
            FichierCapteurs.sauvegarder(batiment, "TP3/sauvegarde_test.txt");
            System.out.println("Écriture normale :");
            afficherFichier("TP3/sauvegarde_test.txt");

            FichierCapteurs.sauvegarder(avecErreur, "TP3/sauvegarde_test.txt");
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        } catch (NullPointerException e) {
            System.out.println("Exception pendant l'écriture : " + e.getClass().getSimpleName());
            System.out.println("Contenu du fichier (fermé automatiquement par try-with-resources) :");
            afficherFichier("TP3/sauvegarde_test.txt");
        }
        try {
            Files.deleteIfExists(Path.of("TP3/sauvegarde_test.txt"));
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        }

        // Partie 12 : charger les capteurs depuis un fichier
        System.out.println("\n=== Partie 12 : charger les capteurs depuis un fichier ===");
        chargerEtAfficher("TP3/capteurs.txt");
        chargerEtAfficher("TP3/fichier_inconnu.txt");

        // Partie 13 : un fichier contenant des données incorrectes
        System.out.println("\n=== Partie 13 : un fichier contenant des données incorrectes ===");
        chargerEtAfficher("TP3/fichiers/valeur_incorrecte.txt");
        chargerEtAfficher("TP3/fichiers/champs_manquants.txt");
        chargerEtAfficher("TP3/fichiers/type_inconnu.txt");

        // Partie 14 : continuer le chargement malgré une erreur
        System.out.println("\n=== Partie 14 : continuer le chargement malgré une erreur ===");
        try {
            FichierCapteurs.charger("TP3/fichiers/erreur_ligne3.txt");
        } catch (IOException e) {
            System.out.println("Impossible d'ouvrir le fichier TP3/fichiers/erreur_ligne3.txt.");
        }

        // Partie 15 : conserver la cause d'une exception
        System.out.println("\n=== Partie 15 : conserver la cause d'une exception ===");
        String[] lignes = {"TEMPERATURE;T02;Salle B;true;bonjour", "TEMPERATURE;T02;Salle B;true;150"};
        for (String ligne : lignes) {
            try {
                FichierCapteurs.lireCapteur(ligne, 3);
            } catch (FormatCapteurException e) {
                System.out.println("Erreur ligne " + e.getNumeroLigne() + " : " + e.getMessage());
                System.out.println("Cause : " + e.getCause());
                System.out.println("Type de la cause : " + e.getCause().getClass().getSimpleName());
            }
        }

        System.out.println("\nFin du programme.");

        scanner.close();
    }

    // Partie 6 : ne traite pas l'exception, elle est propagée à l'appelant
    public static void analyserCapteur(Capteur capteur) throws CapteurInactifException {
        if (!capteur.isActif()) {
            throw new CapteurInactifException(capteur, "d'analyser");
        }
        System.out.print(capteur.getIdentifiant() + " : ");
        capteur.analyser();
    }

    // Parties 2 et 5 : traite l'exception sur place
    private static void essayerAnalyser(Capteur capteur) {
        try {
            capteur.analyser();
        } catch (CapteurInactifException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
    }

    // Partie 7 : les opérations testées, qui peuvent lever les deux types d'exceptions
    private static void executerTestPartie7(int test, CapteurTemperature t1, CapteurLuminosite l1,
            DetecteurPresence p1) throws ValeurCapteurInvalideException, CapteurInactifException {
        switch (test) {
            case 1:
                System.out.println("t1.setTemperature(28.0)");
                t1.setTemperature(28.0);
                System.out.println("  -> OK");
                break;
            case 2:
                System.out.println("t1.setTemperature(120.0)");
                t1.setTemperature(120.0);
                System.out.println("  -> OK");
                break;
            case 3:
                System.out.println("l1.setLuminosite(-20)");
                l1.setLuminosite(-20);
                System.out.println("  -> OK");
                break;
            default:
                System.out.println("analyserCapteur(p1) (P01 désactivé)");
                analyserCapteur(p1);
                break;
        }
    }

    // Parties 12 et 13 : chargement interrompu à la première erreur
    private static void chargerEtAfficher(String nomFichier) {
        System.out.println("\nChargement de " + nomFichier + " :");
        try {
            ArrayList<Capteur> capteurs = FichierCapteurs.chargerStrict(nomFichier);
            for (Capteur capteur : capteurs) {
                capteur.afficherInformations();
                System.out.println();
            }
            System.out.println(capteurs.size() + " capteurs chargés.");
        } catch (FileNotFoundException e) {
            System.out.println("Impossible d'ouvrir le fichier " + nomFichier + ".");
            System.out.println("(" + e + ")");
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier " + nomFichier + " : " + e.getMessage());
        } catch (FormatCapteurException e) {
            System.out.println("Erreur dans le fichier à la ligne " + e.getNumeroLigne() + " :");
            System.out.println(e.getLigne());
            System.out.println();
            System.out.println(e.getMessage());
        }
    }

    private static void afficherFichier(String nomFichier) {
        try {
            for (String ligne : Files.readAllLines(Path.of(nomFichier))) {
                System.out.println("  " + ligne);
            }
        } catch (IOException e) {
            System.out.println("Erreur : " + e);
        }
    }

}
