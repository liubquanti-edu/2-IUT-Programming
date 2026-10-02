package TP3;

// Erreur dans une ligne du fichier de sauvegarde (Parties 13 à 15)
public class FormatCapteurException extends Exception {

    private int numeroLigne;
    private String ligne;

    public FormatCapteurException(int numeroLigne, String ligne, String message) {
        super(message);
        this.numeroLigne = numeroLigne;
        this.ligne = ligne;
    }

    public FormatCapteurException(int numeroLigne, String ligne, String message, Throwable cause) {
        super(message, cause);
        this.numeroLigne = numeroLigne;
        this.ligne = ligne;
    }

    public int getNumeroLigne() {
        return numeroLigne;
    }

    public String getLigne() {
        return ligne;
    }

}
