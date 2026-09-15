package TP1;

public class Main {

    public static void main(String[] args) {
        testInscrire();
        testEnvoyerMessage();
    }

    private static void testInscrire() {
        System.out.println("=== Test inscrire ===");
        SystemeMessagerie systeme = new SystemeMessagerie();

        Utilisateur u1 = systeme.inscrire("Dupont", "Jean", "jean.dupont@mail.com");
        System.out.println("OK : utilisateur cree, numero = " + u1.numero);

        try {
            systeme.inscrire("Dupont", "Jean", "autre@mail.com");
            System.out.println("ECHEC : un doublon nom/prenom aurait du etre refuse");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : doublon refuse (" + e.getMessage() + ")");
        }

        try {
            systeme.inscrire("", "Marie", "marie@mail.com");
            System.out.println("ECHEC : un nom vide aurait du etre refuse");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : nom vide refuse (" + e.getMessage() + ")");
        }

        try {
            systeme.inscrire("Martin", "Paul", null);
            System.out.println("ECHEC : un email manquant aurait du etre refuse");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : email manquant refuse (" + e.getMessage() + ")");
        }

        Utilisateur u2 = systeme.inscrire("Martin", "Paul", "paul.martin@mail.com");
        System.out.println("OK : deuxieme utilisateur cree, numero = " + u2.numero);
    }

    private static void testEnvoyerMessage() {
        System.out.println("\n=== Test envoyerMessage ===");
        SystemeMessagerie systeme = new SystemeMessagerie();
        Utilisateur jean = systeme.inscrire("Dupont", "Jean", "jean.dupont@mail.com");
        Utilisateur marie = systeme.inscrire("Curie", "Marie", "marie.curie@mail.com");

        systeme.envoyerMessage(jean, marie, "Bonjour Marie !");
        System.out.println("OK : message envoye, total messages = " + systeme.messages.size());

        try {
            systeme.envoyerMessage(jean, jean, "Message a moi-meme");
            System.out.println("ECHEC : envoi a soi-meme aurait du etre refuse");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : envoi a soi-meme refuse (" + e.getMessage() + ")");
        }

        try {
            systeme.envoyerMessage(jean, marie, "   ");
            System.out.println("ECHEC : contenu vide aurait du etre refuse");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : contenu vide refuse (" + e.getMessage() + ")");
        }

        try {
            systeme.envoyerMessage(null, marie, "Salut");
            System.out.println("ECHEC : expediteur null aurait du etre refuse");
        } catch (IllegalArgumentException e) {
            System.out.println("OK : expediteur null refuse (" + e.getMessage() + ")");
        }

        Message dernier = systeme.messages.get(systeme.messages.size() - 1);
        System.out.println("Dernier message enregistre : de " + dernier.expediteur.prenom
                + " a " + dernier.destinataire.prenom + " (" + dernier.dateEnvoi + ")");
    }
}
