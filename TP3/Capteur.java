package TP3;

public abstract class Capteur {

    private String identifiant;
    private String piece;
    private boolean actif;

    public Capteur(String identifiant, String piece) {
        this.identifiant = identifiant;
        this.piece = piece;
        this.actif = true;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public String getPiece() {
        return piece;
    }

    public boolean isActif() {
        return actif;
    }

    public void activer() {
        actif = true;
    }

    public void desactiver() {
        actif = false;
    }

    public void afficherInformations() {
        System.out.println("Identifiant : " + identifiant);
        System.out.println("Pièce : " + piece);
        System.out.println("État : " + (actif ? "actif" : "inactif"));
    }

    // Lève une exception si le capteur est désactivé (ex. operation = "d'analyser")
    protected void verifierActif(String operation) throws CapteurInactifException {
        if (!actif) {
            throw new CapteurInactifException(this, operation);
        }
    }

    public void analyser() throws CapteurInactifException {
        verifierActif("d'analyser");
        System.out.println("Analyse du capteur...");
    }

    // Partie 9 : type et valeur enregistrés dans le fichier
    public abstract String getType();

    public abstract String getValeurTexte();

    // Format : type;identifiant;piece;actif;valeur
    public String versLigne() {
        return getType() + ";" + identifiant + ";" + piece + ";" + actif + ";" + getValeurTexte();
    }

}
