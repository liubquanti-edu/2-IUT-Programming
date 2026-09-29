package TP2;

public class CapteurTemperature extends Capteur {

    private double temperature;

    public CapteurTemperature(String identifiant, String piece, double temperature) {
        super(identifiant, piece);
        this.temperature = temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    @Override
    public void afficherInformations() {
        super.afficherInformations();
        System.out.println("Température : " + temperature + " °C");
    }

    @Override
    public void analyser() {
        if (temperature > 28) {
            System.out.println("ALERTE : température trop élevée");
        } else {
            System.out.println("Température normale");
        }
    }

}
