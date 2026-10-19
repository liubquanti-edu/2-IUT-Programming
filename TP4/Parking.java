package TP4;

import java.util.ArrayList;

public class Parking {

    private String nom;
    private int capacite;
    // Collection polymorphe : contient indifféremment des voitures et des motos
    private ArrayList<Vehicule> vehicules;

    public Parking(String nom, int capacite) {
        this.nom = nom;
        this.capacite = capacite;
        this.vehicules = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public int getCapacite() {
        return capacite;
    }

    public int getNombreVehicules() {
        return vehicules.size();
    }

    public boolean estComplet() {
        return vehicules.size() >= capacite;
    }

    public void ajouterVehicule(Vehicule v) throws ParkingCompletException {
        if (estComplet()) {
            throw new ParkingCompletException(v);
        }
        vehicules.add(v);
    }

    public void afficherVehicules() {
        for (Vehicule v : vehicules) {
            System.out.println(v);
            System.out.println();
        }
    }

    public void afficherNombreVehicules() {
        System.out.println("Nombre de véhicules : " + getNombreVehicules());
    }

    public void afficherPrixStationnement() {
        for (Vehicule v : vehicules) {
            System.out.println(v.getImmatriculation() + " - " + v.getMarque());
            System.out.println("Durée : " + v.getDureeStationnement() + " h");
            System.out.println("Prix : " + v.calculerPrixStationnement() + " €");
            System.out.println();
        }
    }

    @Override
    public String toString() {
        return nom + '\n' + "Capacité : " + capacite + " véhicules";
    }

}
