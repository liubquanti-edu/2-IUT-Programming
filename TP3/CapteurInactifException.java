package TP3;

public class CapteurInactifException extends CapteurException {

    private Capteur capteur;

    public CapteurInactifException(Capteur capteur, String operation) {
        super(capteur.getIdentifiant(), "Impossible " + operation + " le capteur " + capteur.getIdentifiant()
                + " : le capteur est inactif.");
        this.capteur = capteur;
    }

    public Capteur getCapteur() {
        return capteur;
    }

}
