package TP3;

public class CapteurTemperature extends Capteur {

    public static final double TEMPERATURE_MIN = -50;
    public static final double TEMPERATURE_MAX = 80;

    private double temperature;

    public CapteurTemperature(String identifiant, String piece, double temperature)
            throws ValeurCapteurInvalideException {
        super(identifiant, piece);
        setTemperature(temperature);
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) throws ValeurCapteurInvalideException {
        if (Double.isNaN(temperature) || temperature < TEMPERATURE_MIN || temperature > TEMPERATURE_MAX) {
            throw new ValeurCapteurInvalideException(getIdentifiant(), temperature,
                    "température comprise entre " + TEMPERATURE_MIN + " et " + TEMPERATURE_MAX + " °C attendue");
        }
        this.temperature = temperature;
    }

    @Override
    public void afficherInformations() {
        super.afficherInformations();
        System.out.println("Température : " + temperature + " °C");
    }

    @Override
    public void analyser() throws CapteurInactifException {
        verifierActif("d'analyser");
        if (temperature > 28) {
            System.out.println("ALERTE : température trop élevée");
        } else {
            System.out.println("Température normale");
        }
    }

    @Override
    public String getType() {
        return "TEMPERATURE";
    }

    @Override
    public String getValeurTexte() {
        return String.valueOf(temperature);
    }

}
