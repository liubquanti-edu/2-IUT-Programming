package TP3;

// Exception générale : toutes les erreurs liées à un capteur en héritent
public class CapteurException extends Exception {

    private String identifiant;

    public CapteurException(String identifiant, String message) {
        super(message);
        this.identifiant = identifiant;
    }

    public String getIdentifiant() {
        return identifiant;
    }

}
