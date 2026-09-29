# TP2 — Réponses aux questions

## Partie 2 — Premier héritage

**Pourquoi recopier les attributs est peu satisfaisant ?**
On duplique le code (attributs, constructeur, accesseurs, `afficherInformations()`) dans chaque classe de capteur. Si le fonctionnement commun doit changer (ajout d'un attribut, format d'affichage, règle d'activation…), il faut modifier toutes les classes une par une, avec un risque d'oubli et d'incohérence.

**Pourquoi `t1.afficherInformations()` et `t1.desactiver()` fonctionnent-ils ?**
`CapteurTemperature` hérite de `Capteur` (`extends`) : elle hérite de toutes ses méthodes publiques. Un `CapteurTemperature` *est un* `Capteur`.

## Partie 3 — Redéfinition

**À quoi sert `@Override` ?**
Il indique au compilateur que la méthode redéfinit une méthode de la classe mère. Si la signature ne correspond à aucune méthode héritée (faute de frappe, mauvais paramètres), la compilation échoue au lieu de créer silencieusement une nouvelle méthode.

**`super.afficherInformations()` vs `afficherInformations()` dans `CapteurTemperature` ?**
- `super.afficherInformations()` appelle la version de la classe mère `Capteur`.
- `afficherInformations()` appelle la version de l'objet courant, donc celle de `CapteurTemperature` elle-même : à l'intérieur de cette méthode, cela provoquerait une récursion infinie (`StackOverflowError`).

## Partie 5 — L'affichage

**`Capteur c1 = t1;` compile-t-il ?**
Oui. Un `CapteurTemperature` *est un* `Capteur` (héritage), on peut donc le référencer par une variable du type de la classe mère (surclassement / upcasting implicite).

**Quelle méthode est utilisée ?**
`CapteurTemperature.afficherInformations()`. Le type de la variable (`Capteur`) détermine ce que l'on a le droit d'appeler à la compilation, mais c'est le type réel de l'objet qui détermine la méthode exécutée (liaison dynamique). C'est le polymorphisme.

## Partie 11 — Classe abstraite

**Que signifierait `new Capteur("C99", "Couloir")` ?**
Un capteur « générique » qui ne mesure rien : cela n'a pas de sens dans le bâtiment.

**Après `public abstract class Capteur`, que se passe-t-il ?**
Erreur de compilation :
```
error: Capteur is abstract; cannot be instantiated
```
Une classe abstraite ne peut pas être instanciée : elle sert uniquement de modèle commun aux sous-classes.

**Les sous-classes restent-elles utilisables ?**
Oui : `CapteurTemperature`, `CapteurLuminosite` et `DetecteurPresence` sont des classes concrètes, elles s'instancient normalement et appellent toujours le constructeur de `Capteur` via `super(...)`. On peut aussi toujours utiliser `Capteur` comme type de variable (`Capteur c1 = t1;`, `ArrayList<Capteur>`).
