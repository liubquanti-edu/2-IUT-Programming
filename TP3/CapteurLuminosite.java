package TP3;

public class CapteurLuminosite extends Capteur {

    private double luminosite;

    public CapteurLuminosite(String identifiant, String piece, double luminosite)
            throws ValeurCapteurInvalideException {
        super(identifiant, piece);
        setLuminosite(luminosite);
    }

    public double getLuminosite() {
        return luminosite;
    }

    public void setLuminosite(double luminosite) throws ValeurCapteurInvalideException {
        if (Double.isNaN(luminosite) || luminosite < 0) {
            throw new ValeurCapteurInvalideException(getIdentifiant(), luminosite,
                    "une luminosité ne peut pas être négative");
        }
        this.luminosite = luminosite;
    }

    @Override
    public void afficherInformations() {
        super.afficherInformations();
        System.out.println("Luminosité : " + luminosite + " lux");
    }

    @Override
    public void analyser() throws CapteurInactifException {
        verifierActif("d'analyser");
        if (luminosite < 100) {
            System.out.println("ALERTE : luminosité insuffisante");
        } else {
            System.out.println("Luminosité normale");
        }
    }

    @Override
    public String getType() {
        return "LUMINOSITE";
    }

    @Override
    public String getValeurTexte() {
        return String.valueOf(luminosite);
    }

}
