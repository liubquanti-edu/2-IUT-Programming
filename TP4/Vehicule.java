package TP4;

// Classe abstraite : un véhicule générique ne peut pas être instancié
public abstract class Vehicule {

    private String immatriculation;
    private String marque;
    private int dureeStationnement;

    public Vehicule(String immatriculation, String marque, int dureeStationnement)
            throws DureeStationnementInvalideException {
        if (dureeStationnement <= 0) {
            throw new DureeStationnementInvalideException(dureeStationnement);
        }
        this.immatriculation = immatriculation;
        this.marque = marque;
        this.dureeStationnement = dureeStationnement;
    }

    public String getImmatriculation() {
        return immatriculation;
    }

    public String getMarque() {
        return marque;
    }

    public int getDureeStationnement() {
        return dureeStationnement;
    }

    // Chaque type de véhicule calcule son propre prix (polymorphisme)
    public abstract double calculerPrixStationnement();

    @Override
    public String toString() {
        return "Immatriculation : " + immatriculation + '\n' +
                "Marque : " + marque + '\n' +
                "Durée : " + dureeStationnement + " h";
    }

}
