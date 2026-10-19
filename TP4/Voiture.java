package TP4;

public class Voiture extends Vehicule {

    private static final double TARIF_HORAIRE = 2.0;

    private int nombrePlaces;

    public Voiture(String immatriculation, String marque, int dureeStationnement, int nombrePlaces)
            throws DureeStationnementInvalideException {
        super(immatriculation, marque, dureeStationnement);
        this.nombrePlaces = nombrePlaces;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    @Override
    public double calculerPrixStationnement() {
        return getDureeStationnement() * TARIF_HORAIRE;
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Nombre de places : " + nombrePlaces;
    }

}
