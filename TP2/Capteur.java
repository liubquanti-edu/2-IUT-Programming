package TP2;

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

    public void analyser() {
        System.out.println("Analyse du capteur...");
    }

}
