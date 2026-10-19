package TP4;

public class ParkingCompletException extends Exception {

    private Vehicule vehicule;

    public ParkingCompletException(Vehicule vehicule) {
        super("impossible d'ajouter " + vehicule.getImmatriculation() + ", le parking est complet.");
        this.vehicule = vehicule;
    }

    public Vehicule getVehicule() {
        return vehicule;
    }

}
