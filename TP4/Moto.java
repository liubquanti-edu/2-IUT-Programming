package TP4;

public class Moto extends Vehicule {

    private static final double TARIF_HORAIRE = 1.0;

    private int cylindree;

    public Moto(String immatriculation, String marque, int dureeStationnement, int cylindree)
            throws DureeStationnementInvalideException {
        super(immatriculation, marque, dureeStationnement);
        this.cylindree = cylindree;
    }

    public int getCylindree() {
        return cylindree;
    }

    @Override
    public double calculerPrixStationnement() {
        return getDureeStationnement() * TARIF_HORAIRE;
    }

    @Override
    public String toString() {
        return super.toString() + '\n' +
                "Cylindrée : " + cylindree + " cm³";
    }

}
