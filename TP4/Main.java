package TP4;

public class Main {

    public static void main(String[] args) {

        // Création d'une voiture et d'une moto
        System.out.println("=== Création des véhicules ===\n");
        Vehicule v1;
        Vehicule m1;
        Vehicule v2;
        try {
            v1 = new Voiture("AA-123-BB", "Peugeot", 3, 5);
            m1 = new Moto("MT-456-CD", "Yamaha", 2, 700);
            v2 = new Voiture("EF-789-GH", "Renault", 4, 5);
        } catch (DureeStationnementInvalideException e) {
            System.out.println("Erreur : " + e.getMessage());
            return;
        }

        System.out.println(v1);
        System.out.println();
        System.out.println(m1);
        System.out.println();

        // Calcul du prix d'une voiture et d'une moto
        System.out.println("=== Prix de stationnement ===\n");
        System.out.println(v1.getImmatriculation() + " : " + v1.calculerPrixStationnement() + " €");
        System.out.println(m1.getImmatriculation() + " : " + m1.calculerPrixStationnement() + " €");
        System.out.println();

        // Ajout de plusieurs types de véhicules dans un même parking
        Parking parking = new Parking("Parking Campus", 3);
        System.out.println("=== Parking ===\n");
        System.out.println(parking);
        System.out.println();

        try {
            parking.ajouterVehicule(v1);
            parking.ajouterVehicule(m1);
            parking.ajouterVehicule(v2); // remplissage complet du parking
        } catch (ParkingCompletException e) {
            System.out.println("Erreur : " + e.getMessage());
        }

        // Affichage des véhicules (parcours d'une collection polymorphe)
        System.out.println("--- Véhicules présents ---\n");
        parking.afficherVehicules();

        System.out.println("--- Prix de chaque véhicule ---\n");
        System.out.println(parking.getNom() + "\n");
        parking.afficherPrixStationnement();
        parking.afficherNombreVehicules();
        System.out.println();

        // Tentative d'ajout dans un parking complet
        System.out.println("Tentative d'ajout de JK-111-LM...");
        try {
            Vehicule v3 = new Voiture("JK-111-LM", "Citroën", 1, 5);
            parking.ajouterVehicule(v3);
            System.out.println("Véhicule ajouté.");
        } catch (ParkingCompletException e) {
            System.out.println("Erreur : " + e.getMessage());
        } catch (DureeStationnementInvalideException e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        System.out.println();

        // Tentatives de création avec une durée incorrecte
        int[] dureesInvalides = { 0, -3, -2 };
        for (int duree : dureesInvalides) {
            System.out.println("Tentative de création d'un véhicule avec une durée de " + duree + " h...");
            try {
                Vehicule invalide = new Moto("ZZ-000-ZZ", "Honda", duree, 500);
                System.out.println("Véhicule créé : " + invalide.getImmatriculation());
            } catch (DureeStationnementInvalideException e) {
                System.out.println("Erreur : " + e.getMessage());
            }
            System.out.println();
        }

        parking.afficherNombreVehicules();
        System.out.println();
        System.out.println("Fin du programme.");
    }

}
