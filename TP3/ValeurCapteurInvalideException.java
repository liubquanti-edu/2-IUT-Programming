package TP3;

public class ValeurCapteurInvalideException extends CapteurException {

    private double valeur;

    public ValeurCapteurInvalideException(String identifiant, double valeur, String contrainte) {
        super(identifiant, "Valeur incorrecte pour le capteur " + identifiant + " (" + contrainte + ").");
        this.valeur = valeur;
    }

    public double getValeur() {
        return valeur;
    }

}
