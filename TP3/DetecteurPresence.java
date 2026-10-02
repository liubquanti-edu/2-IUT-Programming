package TP3;

public class DetecteurPresence extends Capteur {

    private boolean presence;

    public DetecteurPresence(String identifiant, String piece, boolean presence) {
        super(identifiant, piece);
        this.presence = presence;
    }

    public boolean isPresence() {
        return presence;
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
    public void analyser() throws CapteurInactifException {
        verifierActif("d'analyser");
        if (presence) {
            System.out.println("Présence détectée");
        } else {
            System.out.println("Aucune présence");
        }
    }

    @Override
    public String getType() {
        return "PRESENCE";
    }

    @Override
    public String getValeurTexte() {
        return String.valueOf(presence);
    }

}
