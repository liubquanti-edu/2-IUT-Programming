package TP2;

public class CapteurLuminosite extends Capteur {

    private double luminosite;

    public CapteurLuminosite(String identifiant, String piece, double luminosite) {
        super(identifiant, piece);
        this.luminosite = luminosite;
    }

    public void setLuminosite(double luminosite) {
        this.luminosite = luminosite;
    }

    @Override
    public void afficherInformations() {
        super.afficherInformations();
        System.out.println("Luminosité : " + luminosite + " lux");
    }

    @Override
    public void analyser() {
        if (luminosite < 100) {
            System.out.println("ALERTE : luminosité insuffisante");
        } else {
            System.out.println("Luminosité normale");
        }
    }

}
