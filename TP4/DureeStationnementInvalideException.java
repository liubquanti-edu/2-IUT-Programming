package TP4;

public class DureeStationnementInvalideException extends Exception {

    private int duree;

    public DureeStationnementInvalideException(int duree) {
        super("la durée " + duree + " h est invalide.");
        this.duree = duree;
    }

    public int getDuree() {
        return duree;
    }

}
