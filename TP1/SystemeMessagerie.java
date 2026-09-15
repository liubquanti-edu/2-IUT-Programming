package TP1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SystemeMessagerie {

    List<Utilisateur> utilisateurs = new ArrayList<>();
    List<Message> messages = new ArrayList<>();

    public Utilisateur inscrire(String nom, String prenom, String email) {
        if (nom == null || nom.isBlank() || prenom == null || prenom.isBlank()) {
            throw new IllegalArgumentException("Le nom et le prenom sont obligatoires.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("L'adresse mail est obligatoire.");
        }

        for (Utilisateur u : utilisateurs) {
            if (u.nom.equalsIgnoreCase(nom) && u.prenom.equalsIgnoreCase(prenom)) {
                throw new IllegalArgumentException("Un utilisateur avec ce nom et prenom existe deja.");
            }
        }

        Utilisateur nouveau = new Utilisateur();
        nouveau.numero = utilisateurs.size() + 1;
        nouveau.nom = nom;
        nouveau.prenom = prenom;
        nouveau.email = email;

        utilisateurs.add(nouveau);
        return nouveau;
    }

    public void envoyerMessage(Utilisateur expediteur, Utilisateur destinataire, String contenu) {
        if (expediteur == null || destinataire == null) {
            throw new IllegalArgumentException("L'expediteur et le destinataire sont obligatoires.");
        }
        if (expediteur == destinataire) {
            throw new IllegalArgumentException("Impossible de s'envoyer un message a soi-meme.");
        }
        if (contenu == null || contenu.isBlank()) {
            throw new IllegalArgumentException("Le contenu du message est obligatoire.");
        }

        Message message = new Message();
        message.expediteur = expediteur;
        message.destinataire = destinataire;
        message.contenu = contenu;
        message.dateEnvoi = LocalDateTime.now().toString();

        messages.add(message);
    }

    public List<Utilisateur> rechercherUtilisateurs(String nom, String prenom) {
        throw new UnsupportedOperationException("TODO");
    }

    public List<Message> consulterMessages(Utilisateur utilisateur) {
        throw new UnsupportedOperationException("TODO");
    }


}
