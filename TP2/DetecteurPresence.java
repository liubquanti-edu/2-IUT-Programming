package TP2;

public class DetecteurPresence extends Capteur {

    private boolean presence;

    public DetecteurPresence(String identifiant, String piece, boolean presence) {
        super(identifiant, piece);
        this.presence = presence;
    }

    public void setPresence(boolean presence) {
        this.presence = presence;
    }

    @Override
    public void afficherInformations() {
        super.afficherInformations();
        System.out.println("Présence : " + (presence ? "oui" : "non"));
    }

    @Override
    public void analyser() {
        if (presence) {
            System.out.println("Présence détectée");
        } else {
            System.out.println("Aucune présence");
        }
    }

}
