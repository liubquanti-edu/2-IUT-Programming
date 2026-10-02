package TP3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

// Sauvegarde et chargement des capteurs au format : type;identifiant;piece;actif;valeur
public class FichierCapteurs {

    // Partie 10 : la fermeture du fichier est garantie par finally
    public static void sauvegarderAvecFinally(ArrayList<Capteur> capteurs, String nomFichier) throws IOException {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(nomFichier));
            for (Capteur capteur : capteurs) {
                writer.write(capteur.versLigne());
                writer.newLine();
            }
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    // Partie 11 : même sauvegarde avec try-with-resources (fermeture automatique)
    public static void sauvegarder(ArrayList<Capteur> capteurs, String nomFichier) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier))) {
            for (Capteur capteur : capteurs) {
                writer.write(capteur.versLigne());
                writer.newLine();
            }
        }
    }

    // Parties 12 et 13 : le chargement s'arrête à la première ligne incorrecte
    public static ArrayList<Capteur> chargerStrict(String nomFichier) throws IOException, FormatCapteurException {
        ArrayList<Capteur> capteurs = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            int numeroLigne = 0;
            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                if (!ligne.isBlank()) {
                    capteurs.add(lireCapteur(ligne, numeroLigne));
                }
            }
        }
        return capteurs;
    }

    // Partie 14 : une ligne incorrecte est signalée, le chargement continue
    public static ArrayList<Capteur> charger(String nomFichier) throws IOException {
        ArrayList<Capteur> capteurs = new ArrayList<>();
        int lignesIncorrectes = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String ligne;
            int numeroLigne = 0;
            while ((ligne = reader.readLine()) != null) {
                numeroLigne++;
                if (ligne.isBlank()) {
                    continue;
                }
                try {
                    Capteur capteur = lireCapteur(ligne, numeroLigne);
                    capteurs.add(capteur);
                    System.out.println(capteur.getIdentifiant() + " chargé.");
                } catch (FormatCapteurException e) {
                    lignesIncorrectes++;
                    afficherErreur(e);
                }
            }
        }
        System.out.println("\nChargement terminé.");
        System.out.println(capteurs.size() + (capteurs.size() > 1 ? " capteurs chargés." : " capteur chargé."));
        System.out.println(lignesIncorrectes
                + (lignesIncorrectes > 1 ? " lignes incorrectes." : " ligne incorrecte."));
        return capteurs;
    }

    public static void afficherErreur(FormatCapteurException e) {
        System.out.println("\nErreur ligne " + e.getNumeroLigne() + " : " + e.getMessage());
        System.out.println(e.getLigne());
        // Partie 15 : exception d'origine
        if (e.getCause() != null) {
            System.out.println("Cause : " + e.getCause());
        }
        System.out.println();
    }

    // Parties 13 et 15 : transforme une ligne du fichier en capteur
    public static Capteur lireCapteur(String ligne, int numeroLigne) throws FormatCapteurException {
        String[] champs = ligne.split(";");
        if (champs.length != 5) {
            throw new FormatCapteurException(numeroLigne, ligne,
                    "5 champs attendus (type;identifiant;piece;actif;valeur), " + champs.length + " trouvé(s).");
        }
        String type = champs[0].trim();
        String identifiant = champs[1].trim();
        String piece = champs[2].trim();
        String valeur = champs[4].trim();
        boolean actif = lireBooleen(champs[3].trim(), numeroLigne, ligne, "État incorrect (true ou false attendu).");

        Capteur capteur;
        switch (type) {
            case "TEMPERATURE":
                try {
                    capteur = new CapteurTemperature(identifiant, piece, Double.parseDouble(valeur));
                } catch (NumberFormatException | ValeurCapteurInvalideException e) {
                    throw new FormatCapteurException(numeroLigne, ligne,
                            "Valeur incorrecte pour un capteur de température.", e);
                }
                break;
            case "LUMINOSITE":
                try {
                    capteur = new CapteurLuminosite(identifiant, piece, Double.parseDouble(valeur));
                } catch (NumberFormatException | ValeurCapteurInvalideException e) {
                    throw new FormatCapteurException(numeroLigne, ligne,
                            "Valeur incorrecte pour un capteur de luminosité.", e);
                }
                break;
            case "PRESENCE":
                boolean presence = lireBooleen(valeur, numeroLigne, ligne,
                        "Valeur incorrecte pour un détecteur de présence (true ou false attendu).");
                capteur = new DetecteurPresence(identifiant, piece, presence);
                break;
            default:
                throw new FormatCapteurException(numeroLigne, ligne, "Type de capteur inconnu : " + type + ".");
        }

        if (!actif) {
            capteur.desactiver();
        }
        return capteur;
    }

    // Boolean.parseBoolean renverrait false pour n'importe quel texte : on vérifie nous-mêmes
    private static boolean lireBooleen(String texte, int numeroLigne, String ligne, String message)
            throws FormatCapteurException {
        if (texte.equals("true")) {
            return true;
        }
        if (texte.equals("false")) {
            return false;
        }
        throw new FormatCapteurException(numeroLigne, ligne, message);
    }

}
