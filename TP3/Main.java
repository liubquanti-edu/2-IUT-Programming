package TP3;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

// Partie 17 : application finale (les parties 1 à 15 sont dans Exercices)
public class Main {

    // Relatif au dossier 2-IUT-Programming, depuis lequel le programme est lancé
    private static final String FICHIER_PAR_DEFAUT = "TP3/capteurs.txt";

    private static ArrayList<Capteur> capteurs = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        initialiserCapteurs();

        boolean quitter = false;
        while (!quitter) {
            afficherMenu();
            // Un seul point de traitement : quelle que soit l'erreur, on revient au menu
            try {
                int choix = lireEntier("Votre choix : ");
                System.out.println();
                switch (choix) {
                    case 1:
                        afficherCapteurs();
                        break;
                    case 2:
                        modifierMesure();
                        break;
                    case 3:
                        activerCapteur();
                        break;
                    case 4:
                        desactiverCapteur();
                        break;
                    case 5:
                        analyserCapteurs();
                        break;
                    case 6:
                        chargerCapteurs();
                        break;
                    case 7:
                        sauvegarderCapteurs();
                        break;
                    case 0:
                        quitter = true;
                        break;
                    default:
                        System.out.println("Erreur : choix inconnu.");
                }
            } catch (NumberFormatException e) {
                System.out.println("\nErreur : vous devez saisir un nombre.");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("Erreur : aucun capteur ne correspond à ce numéro.");
            } catch (ValeurCapteurInvalideException e) {
                System.out.println("Erreur : " + e.getMessage());
                System.out.println("Valeur reçue : " + e.getValeur());
            } catch (IllegalArgumentException e) {
                System.out.println("Erreur : " + e.getMessage());
            } catch (NoSuchElementException e) {
                // Fin de l'entrée standard (Ctrl+Z / Ctrl+D) : plus rien à lire
                quitter = true;
            }
        }

        System.out.println("\nAu revoir.");
        scanner.close();
    }

    private static void initialiserCapteurs() {
        try {
            capteurs.add(new CapteurTemperature("T01", "Salle A", 21.5));
            capteurs.add(new CapteurLuminosite("L01", "Hall", 640));
            capteurs.add(new DetecteurPresence("P01", "Salle B", true));
        } catch (ValeurCapteurInvalideException e) {
            // Impossible avec les valeurs ci-dessus : ce serait une erreur de programmation
            throw new IllegalStateException("Capteurs initiaux incorrects", e);
        }
    }

    private static void afficherMenu() {
        System.out.println("\n========== SMART BUILDING ==========\n");
        System.out.println("1 - Afficher les capteurs");
        System.out.println("2 - Modifier une mesure");
        System.out.println("3 - Activer un capteur");
        System.out.println("4 - Désactiver un capteur");
        System.out.println("5 - Analyser les capteurs");
        System.out.println("6 - Charger les capteurs depuis un fichier");
        System.out.println("7 - Sauvegarder les capteurs dans un fichier");
        System.out.println("0 - Quitter\n");
    }

    private static void afficherCapteurs() {
        if (capteurs.isEmpty()) {
            System.out.println("Aucun capteur.");
        }
        for (int i = 0; i < capteurs.size(); i++) {
            System.out.println("[" + i + "]");
            capteurs.get(i).afficherInformations();
            System.out.println();
        }
    }

    private static void modifierMesure() throws ValeurCapteurInvalideException {
        Capteur capteur = choisirCapteur();
        if (capteur instanceof CapteurTemperature temperature) {
            temperature.setTemperature(lireReel("Nouvelle température : "));
        } else if (capteur instanceof CapteurLuminosite luminosite) {
            luminosite.setLuminosite(lireReel("Nouvelle luminosité : "));
        } else if (capteur instanceof DetecteurPresence detecteur) {
            detecteur.setPresence(lireOuiNon("Présence (oui/non) : "));
        }
        System.out.println("Mesure du capteur " + capteur.getIdentifiant() + " modifiée.");
    }

    private static void activerCapteur() {
        Capteur capteur = choisirCapteur();
        capteur.activer();
        System.out.println("Capteur " + capteur.getIdentifiant() + " activé.");
    }

    private static void desactiverCapteur() {
        Capteur capteur = choisirCapteur();
        capteur.desactiver();
        System.out.println("Capteur " + capteur.getIdentifiant() + " désactivé.");
    }

    // Un capteur désactivé ne doit pas empêcher l'analyse des suivants
    private static void analyserCapteurs() {
        if (capteurs.isEmpty()) {
            System.out.println("Aucun capteur.");
        }
        for (Capteur capteur : capteurs) {
            System.out.print(capteur.getIdentifiant() + " : ");
            try {
                capteur.analyser();
            } catch (CapteurInactifException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
        }
    }

    private static void chargerCapteurs() {
        String nomFichier = lireNomFichier();
        try {
            capteurs = FichierCapteurs.charger(nomFichier);
        } catch (FileNotFoundException e) {
            System.out.println("Impossible d'ouvrir le fichier " + nomFichier + ".");
        } catch (IOException e) {
            System.out.println("Erreur de lecture du fichier " + nomFichier + " : " + e.getMessage());
        }
    }

    private static void sauvegarderCapteurs() {
        String nomFichier = lireNomFichier();
        try {
            FichierCapteurs.sauvegarder(capteurs, nomFichier);
            System.out.println(capteurs.size() + " capteur(s) sauvegardé(s) dans " + nomFichier + ".");
        } catch (IOException e) {
            System.out.println("Impossible d'écrire dans le fichier " + nomFichier + ".");
        }
    }

    // Peut lever NumberFormatException ou IndexOutOfBoundsException, traitées dans main
    private static Capteur choisirCapteur() {
        for (int i = 0; i < capteurs.size(); i++) {
            System.out.println(i + " - " + capteurs.get(i).getIdentifiant());
        }
        return capteurs.get(lireEntier("Numéro du capteur : "));
    }

    private static int lireEntier(String question) {
        System.out.print(question);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static double lireReel(String question) {
        System.out.print(question);
        return Double.parseDouble(scanner.nextLine().trim());
    }

    private static boolean lireOuiNon(String question) {
        System.out.print(question);
        String reponse = scanner.nextLine().trim();
        if (reponse.equalsIgnoreCase("oui")) {
            return true;
        }
        if (reponse.equalsIgnoreCase("non")) {
            return false;
        }
        throw new IllegalArgumentException("répondez par oui ou non.");
    }

    private static String lireNomFichier() {
        System.out.print("Nom du fichier (Entrée = " + FICHIER_PAR_DEFAUT + ") : ");
        String nomFichier = scanner.nextLine().trim();
        return nomFichier.isEmpty() ? FICHIER_PAR_DEFAUT : nomFichier;
    }

}
