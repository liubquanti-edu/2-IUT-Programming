# TP3 — Réponses aux questions

## Partie 1 — Une première exception

**Saisie `23.5`**
La conversion `Double.parseDouble("23.5")` réussit, `setTemperature(23.5)` est appelée et l'affichage indique bien `Température : 23.5 °C`.

**Saisie `bonjour` : que se passe-t-il ?**
Le programme s'arrête brutalement, sans exécuter la suite de `main` (le capteur n'est ni modifié ni réaffiché). Java affiche :
```
Exception in thread "main" java.lang.NumberFormatException: For input string: "bonjour"
	at java.base/jdk.internal.math.FloatingDecimal.readJavaFormatString(...)
	at java.base/jdk.internal.math.FloatingDecimal.parseDouble(...)
	at java.base/java.lang.Double.parseDouble(Double.java:993)
	at TP3.Main.main(Main.java:17)
```

**Nom de l'exception ?**
`java.lang.NumberFormatException` : la chaîne fournie ne représente pas un nombre valide.

**À quelle instruction apparaît-elle ?**
À la ligne 17 de `Main.java` :
```java
double temperature = Double.parseDouble(saisie);
```
La pile d'appels (*stack trace*) se lit de haut en bas : l'exception naît au fond de la bibliothèque Java (`FloatingDecimal`), remonte dans `Double.parseDouble`, puis dans notre `main` à la ligne 17. Comme personne ne la traite, elle remonte jusqu'à la JVM, qui arrête le programme.

**Les différentes saisies ont-elles le même comportement ?**

| Saisie  | Résultat                       | Explication                                                        |
|---------|--------------------------------|--------------------------------------------------------------------|
| `18`    | OK → `18.0 °C`                 | Un entier est un réel valide                                       |
| `-4.5`  | OK → `-4.5 °C`                 | Le signe `-` est accepté                                           |
| `25,3`  | `NumberFormatException`        | `parseDouble` n'accepte que le **point** comme séparateur décimal (format Java, indépendant de la langue du système) |
| `31.2`  | OK → `31.2 °C` + `ALERTE`      | Conversion correcte ; l'analyse déclenche l'alerte (> 28 °C)       |
| `abc`   | `NumberFormatException`        | Ce n'est pas un nombre                                             |

Non, toutes les saisies n'ont pas le même comportement : les nombres bien écrits (avec un point) sont convertis, alors que le texte **et** la virgule décimale française provoquent une `NumberFormatException` qui fait planter le programme. On remarque aussi qu'une valeur comme `-4.5` est acceptée même si elle pourrait être incohérente pour une pièce : `parseDouble` vérifie le **format**, pas la **cohérence** de la valeur.

## Partie 2 — Intercepter une exception

La conversion est placée dans un bloc `try`, et la `NumberFormatException` est interceptée par un `catch` :
```java
try {
    double temperature = Double.parseDouble(saisie);
    t1.setTemperature(temperature);
    System.out.println("Température modifiée.");
} catch (NumberFormatException e) {
    System.out.println("Erreur : veuillez saisir un nombre valide.");
}
```

**Tests**

| Saisie    | Résultat                                         | Température finale |
|-----------|--------------------------------------------------|--------------------|
| `23.5`    | « Température modifiée. »                        | 23.5 °C            |
| `-4.5`    | « Température modifiée. »                        | -4.5 °C            |
| `bonjour` | « Erreur : veuillez saisir un nombre valide. »   | 21.5 °C (inchangée)|
| `25,3`    | « Erreur : veuillez saisir un nombre valide. »   | 21.5 °C (inchangée)|
| `abc`     | « Erreur : veuillez saisir un nombre valide. »   | 21.5 °C (inchangée)|
| *(vide)*  | « Erreur : veuillez saisir un nombre valide. »   | 21.5 °C (inchangée)|

Dans tous les cas, le programme continue : le capteur est réaffiché, analysé, puis « Fin du programme. » s'affiche. Il n'y a plus de *stack trace*.

**Quelle construction Java permet d'intercepter une exception ?**
Le bloc `try { ... } catch (TypeException e) { ... }`.

**À quoi correspondent `try` et `catch` ?**
- `try` (« essayer ») : contient les instructions **susceptibles de lever une exception**. Si une exception survient, l'exécution du bloc s'interrompt **immédiatement** à cette instruction. Les lignes suivantes du `try` ne sont pas exécutées : ici, `setTemperature` n'est pas appelée et la température reste inchangée.
- `catch` (« attraper ») : précise **quel type d'exception** on intercepte (ici `NumberFormatException`) et contient le **traitement** à effectuer dans ce cas, ici afficher un message compréhensible. La variable `e` donne accès à l'exception (par exemple `e.getMessage()`).
- Après le `catch`, l'exécution reprend normalement **après** le bloc `try/catch`. Si aucune exception ne survient, le `catch` est simplement ignoré.

## Partie 3 — Plusieurs types d'erreurs

**Saisie `1`**
Le programme affiche les informations du capteur `L01` (Hall, 640.0 lux).

**Saisie `15` : que se passe-t-il ?** *(avant l'ajout des `catch`)*
Le programme s'arrête avec :
```
Exception in thread "main" java.lang.IndexOutOfBoundsException: Index 15 out of bounds for length 3
```
La conversion `Integer.parseInt("15")` réussit, mais `capteurs.get(15)` échoue : la liste ne contient que les indices 0 à 2. La saisie `-1` produit la même exception.

**Saisie `bonjour`**
```
Exception in thread "main" java.lang.NumberFormatException: For input string: "bonjour"
	at java.base/java.lang.Integer.parseInt(Integer.java:529)
```
Cette fois, c'est la conversion qui échoue, avant même l'accès à la liste.

**Ces deux erreurs provoquent-elles la même exception ?**
Non :
- `bonjour` provoque une `NumberFormatException`, levée par `Integer.parseInt`. C'est un problème de **format** de la saisie.
- `15` provoque une `IndexOutOfBoundsException`, levée par `ArrayList.get`. Le nombre est valide, mais **aucun élément** ne lui correspond.

**Traitement séparé** : on enchaîne plusieurs blocs `catch` après un même `try`. Java exécute le premier `catch` dont le type correspond à l'exception levée.
```java
try {
    int numero = Integer.parseInt(saisie);
    Capteur capteur = capteurs.get(numero);
    capteur.afficherInformations();
} catch (NumberFormatException e) {
    System.out.println("Erreur : vous devez saisir un nombre.");
} catch (IndexOutOfBoundsException e) {
    System.out.println("Erreur : aucun capteur ne correspond à ce numéro.");
}
```

| Saisie    | Message affiché                                       |
|-----------|-------------------------------------------------------|
| `1`       | informations de `L01`                                 |
| `15`      | Erreur : aucun capteur ne correspond à ce numéro.     |
| `-1`      | Erreur : aucun capteur ne correspond à ce numéro.     |
| `bonjour` | Erreur : vous devez saisir un nombre.                 |
| *(vide)*  | Erreur : vous devez saisir un nombre.                 |

## Partie 4 — Déclencher volontairement une exception

Le mutateur vérifie la valeur et **lève** une exception si elle est incohérente :
```java
public void setTemperature(double temperature) {
    if (temperature < -50 || temperature > 80) {
        throw new IllegalArgumentException(
                "Température invalide : " + temperature + " °C (doit être comprise entre -50 et 80 °C)");
    }
    this.temperature = temperature;
}
```
Même principe pour `setLuminosite` avec la condition `luminosite < 0`.

`IllegalArgumentException` est l'exception standard de Java pour signaler qu'**un argument passé à une méthode est invalide**.

**Tests** (chaque appel est placé dans un `try/catch`)

| Appel                       | Résultat                                                                   |
|-----------------------------|----------------------------------------------------------------------------|
| `t1.setTemperature(22.5)`   | OK                                                                         |
| `t1.setTemperature(-70)`    | `IllegalArgumentException` : Température invalide : -70.0 °C (…)           |
| `t1.setTemperature(150)`    | `IllegalArgumentException` : Température invalide : 150.0 °C (…)           |
| `l1.setLuminosite(600)`     | OK                                                                         |
| `l1.setLuminosite(0)`       | OK (0 n'est pas négatif : obscurité totale)                                |
| `l1.setLuminosite(-50)`     | `IllegalArgumentException` : Luminosité invalide : -50.0 lux (…)           |

Quand une valeur est refusée, l'attribut **n'est pas modifié**, car le `throw` interrompt la méthode avant l'affectation. Le capteur reste donc toujours dans un état cohérent.

Sans `try/catch`, `t1.setTemperature(150)` arrête le programme :
```
Exception in thread "main" java.lang.IllegalArgumentException: Température invalide : 150.0 °C (doit être comprise entre -50 et 80 °C)
	at TP3.CapteurTemperature.setTemperature(CapteurTemperature.java:14)
```

> Remarque : dans la Partie 2, saisir `250` lève maintenant cette exception. Un `catch (IllegalArgumentException e)` a été ajouté **après** le `catch (NumberFormatException e)`. L'ordre est important, car `NumberFormatException` est une sous-classe d'`IllegalArgumentException`. Placé en premier, le `catch` le plus général intercepterait aussi les erreurs de format, et le compilateur refuserait le second `catch` (exception déjà interceptée).

**Quelle instruction permet de déclencher volontairement une exception ?**
`throw`, suivi d'un objet exception créé avec `new` : `throw new IllegalArgumentException("message");`.

**Différence entre exception automatique et exception volontaire ?**
- **Automatique** : c'est Java (la JVM ou la bibliothèque standard) qui détecte une opération **techniquement impossible**, par exemple convertir `"bonjour"` en nombre (`NumberFormatException`), accéder à l'indice 15 d'une liste de 3 éléments (`IndexOutOfBoundsException`), diviser un entier par zéro ou appeler une méthode sur `null`.
- **Volontaire** : c'est **notre programme** qui décide, avec `throw`, qu'une situation est une erreur selon une **règle métier** que Java ne peut pas connaître. Pour Java, `250.0` est un `double` parfaitement valide. C'est nous qui décidons qu'une température doit être comprise entre -50 et 80 °C. On choisit aussi le type d'exception et le message.
- Dans les deux cas, l'exception se propage et s'intercepte **de la même manière** (`try/catch`). Seule son origine diffère.

## Partie 5 — Une exception propre à SmartBuilding

**Que se passe-t-il actuellement avec `t1.desactiver(); t1.analyser();` ?** *(avant modification)*
Rien n'empêche l'analyse : le programme affiche `Température normale` alors que le capteur est inactif. Aucune erreur n'est signalée, et le résultat est trompeur, car un capteur éteint ne fournit pas de mesure fiable.

**La classe `CapteurInactifException`**
```java
public class CapteurInactifException extends Exception {

    private Capteur capteur;

    public CapteurInactifException(Capteur capteur, String operation) {
        super("Impossible " + operation + " le capteur " + capteur.getIdentifiant()
                + " : le capteur est inactif.");
        this.capteur = capteur;
    }

    public Capteur getCapteur() {
        return capteur;
    }
}
```
- Elle hérite d'`Exception` : c'est donc une **exception vérifiée** (*checked*). Toute méthode qui peut la lever doit le déclarer avec `throws`, et tout appelant doit la traiter (`try/catch`) ou la déclarer à son tour.
- Le message est transmis au constructeur parent avec `super(...)`. Il est ensuite récupéré par `e.getMessage()`.
- Elle conserve le capteur concerné (`getCapteur()`), ce qui permet au code qui l'intercepte de savoir **quel** capteur pose problème.

**Utilisation** : une méthode `verifierActif` dans `Capteur`, réutilisable par toute opération qui nécessite un capteur actif :
```java
protected void verifierActif(String operation) throws CapteurInactifException {
    if (!actif) {
        throw new CapteurInactifException(this, operation);
    }
}
```
Chaque `analyser()` (dans `Capteur` et ses trois sous-classes) déclare `throws CapteurInactifException` et commence par `verifierActif("d'analyser");`.

> Une méthode redéfinie ne peut pas lever plus d'exceptions vérifiées que la méthode d'origine. Il faut donc déclarer `throws CapteurInactifException` **aussi dans `Capteur.analyser()`**. Sinon, les sous-classes ne pourraient pas la déclarer, et l'appel `capteur.analyser()` via une référence de type `Capteur` ne compilerait pas.

Si on appelle `t1.analyser()` sans la traiter, le **compilateur** refuse le programme :
```
error: unreported exception CapteurInactifException; must be caught or declared to be thrown
```

**Tests**
```
Capteur actif :
Température normale
Capteur désactivé :
Erreur : Impossible d'analyser le capteur T01 : le capteur est inactif.

Analyse de tous les capteurs (P01 désactivé) :
T01 : Température normale
L01 : ALERTE : luminosité insuffisante
P01 : Erreur : Impossible d'analyser le capteur P01 : le capteur est inactif.
```
Dans la boucle, l'erreur sur `P01` n'empêche pas l'analyse des autres capteurs.

**Pourquoi créer une exception spécifique plutôt qu'utiliser `Exception` ?**
- **Précision du `catch`** : on peut intercepter *uniquement* ce cas avec `catch (CapteurInactifException e)`. Avec `catch (Exception e)`, on attraperait aussi toutes les autres erreurs (`NullPointerException`, `IndexOutOfBoundsException`…) et on les traiterait toutes de la même façon, en masquant parfois de vrais bugs.
- **Traitements différents** : plusieurs `catch` successifs permettent de réagir différemment selon le problème (par exemple réactiver le capteur, ou ignorer une saisie invalide).
- **Lisibilité** : le nom de la classe décrit le problème. `throws CapteurInactifException` dans une signature documente précisément ce qui peut mal se passer, alors que `throws Exception` ne dit rien.
- **Informations supplémentaires** : la classe peut porter des données utiles (ici le capteur concerné) en plus du message.
- **Vérification par le compilateur** : comme exception vérifiée, elle oblige le développeur à prévoir le cas « capteur inactif » partout où l'on analyse un capteur.

## Partie 6 — Propagation d'une exception

**Première version (sans traiter l'exception)**
```java
public static void analyserCapteur(Capteur capteur) {
    if (!capteur.isActif()) {
        throw new CapteurInactifException(capteur, "d'analyser");
    }
    capteur.analyser();
}
```
Le compilateur refuse :
```
TP3\Main.java:126: error: unreported exception CapteurInactifException; must be caught or declared to be thrown
            throw new CapteurInactifException(capteur, "d'analyser");
TP3\Main.java:129: error: unreported exception CapteurInactifException; must be caught or declared to be thrown
        capteur.analyser();
TP3\Main.java:113: error: exception CapteurInactifException is never thrown in body of corresponding try statement
        } catch (CapteurInactifException e) {
```
`CapteurInactifException` est une exception **vérifiée**. Une méthode qui peut la lever, directement (`throw`) ou indirectement (appel de `analyser()`), doit soit la **traiter** (`try/catch`), soit **déclarer** qu'elle la laisse passer (`throws`). Le troisième message est une conséquence : sans `throws`, le compilateur considère qu'`analyserCapteur` ne peut pas lever cette exception, et le `catch` du `main` lui paraît donc inutile.

**Version corrigée : l'exception est transmise à l'appelant**
```java
public static void analyserCapteur(Capteur capteur) throws CapteurInactifException {
    if (!capteur.isActif()) {
        throw new CapteurInactifException(capteur, "d'analyser");
    }
    System.out.print(capteur.getIdentifiant() + " : ");
    capteur.analyser();
}
```
Elle est traitée dans `main` :
```java
l1.desactiver();
try {
    analyserCapteur(t1);
    analyserCapteur(l1);
    analyserCapteur(p1);
} catch (CapteurInactifException e) {
    System.out.println("Erreur : " + e.getMessage());
}
```
Résultat :
```
T01 : Température normale
Erreur : Impossible d'analyser le capteur L01 : le capteur est inactif.
```
**`P01` n'est pas analysé.** L'exception levée dans `analyserCapteur(l1)` remonte dans `main` et interrompt le bloc `try` : `analyserCapteur(p1)` n'est jamais exécuté, et l'exécution reprend dans le `catch`. Pour analyser tous les capteurs malgré une erreur, il faut placer le `try/catch` **à l'intérieur** d'une boucle, comme dans la Partie 5.

Le chemin de l'exception : `throw` dans `analyserCapteur` → sortie immédiate de `analyserCapteur` → retour dans `main` → `catch` de `main`. Si `main` ne la traitait pas non plus, il faudrait écrire `main(...) throws CapteurInactifException`. L'exception atteindrait alors la JVM, qui arrêterait le programme avec une *stack trace*.

**Déclencher / propager / traiter**
- **Déclencher** (lever) : *créer* l'exception et la lancer avec `throw new ...`. C'est l'**origine** de l'erreur : ici, `analyserCapteur` constate que le capteur est inactif.
- **Propager** : *ne pas* traiter l'exception, la laisser **remonter** à la méthode appelante. La méthode s'interrompt immédiatement. En Java, pour une exception vérifiée, cela se déclare avec `throws` dans la signature. Ici, `analyserCapteur` propage vers `main`.
- **Traiter** (intercepter) : *attraper* l'exception avec `try/catch` et décider quoi faire (afficher un message, choisir une valeur par défaut, réessayer…). La propagation s'arrête là et le programme continue après le `catch`. Ici, c'est `main` qui la traite.

Une même exception peut ainsi être déclenchée dans une méthode, propagée à travers plusieurs méthodes, et traitée là où l'on sait **comment** réagir. Souvent, la méthode qui détecte le problème n'est pas celle qui sait quoi en faire.

**À quoi sert `throws` ?**
`throws` s'écrit dans la **signature** d'une méthode. Il **déclare** les exceptions vérifiées que la méthode peut laisser sortir, sans les traiter elle-même.
- C'est une **information pour l'appelant**, et un contrat vérifié par le compilateur : quiconque appelle `analyserCapteur` doit à son tour traiter `CapteurInactifException` ou la déclarer.
- À ne pas confondre avec `throw` (sans « s »), qui est une **instruction** et lance effectivement une exception.

| Mot-clé  | Où ?                       | Rôle                                            |
|----------|----------------------------|-------------------------------------------------|
| `throw`  | dans le corps de la méthode | déclenche une exception                         |
| `throws` | dans la signature           | déclare que la méthode peut propager l'exception |

> **Organisation du code à partir de la Partie 7** : le code des Parties 1 à 15 a été déplacé dans `Exercices.java`, qui contient aussi `analyserCapteur` de la Partie 6. `Main.java` contient l'application finale de la Partie 17. La sauvegarde et le chargement se trouvent dans `FichierCapteurs.java`. Les chemins de fichiers (`TP3/capteurs.txt`…) sont relatifs au dossier `2-IUT-Programming`, depuis lequel le programme est lancé.

## Partie 7 — Une hiérarchie d'exceptions

```
Exception
├── CapteurException                     (identifiant du capteur concerné)
│   ├── CapteurInactifException          (capteur concerné)
│   └── ValeurCapteurInvalideException   (valeur incorrecte)
└── FormatCapteurException               (Parties 13 à 15 : ligne de fichier incorrecte)
```
```java
public class CapteurException extends Exception { ... }
public class CapteurInactifException extends CapteurException { ... }
public class ValeurCapteurInvalideException extends CapteurException { ... }
```
`setTemperature` et `setLuminosite` lèvent désormais une `ValeurCapteurInvalideException` au lieu de l'`IllegalArgumentException` de la Partie 4. Il s'agit d'une exception vérifiée, donc les setters déclarent `throws ValeurCapteurInvalideException`. Les **constructeurs** appellent maintenant les setters : un capteur ne peut plus être créé avec une valeur incorrecte (par exemple `new CapteurTemperature("T02", "X", 250)`).

`FormatCapteurException` n'hérite pas de `CapteurException` : elle signale une erreur dans un **fichier**, pas dans un capteur. Au moment de l'erreur, il n'existe d'ailleurs pas forcément de capteur, ni même d'identifiant si la ligne est trop courte.

**Tests — chaque type traité séparément**
```java
try { ... }
catch (ValeurCapteurInvalideException e) { System.out.println("  -> [valeur invalide] " + e.getMessage()); }
catch (CapteurInactifException e)        { System.out.println("  -> [capteur inactif] " + e.getMessage()); }
```
```
t1.setTemperature(28.0)
  -> OK
t1.setTemperature(120.0)
  -> [valeur invalide] Valeur incorrecte pour le capteur T01 (température comprise entre -50.0 et 80.0 °C attendue).
l1.setLuminosite(-20)
  -> [valeur invalide] Valeur incorrecte pour le capteur L01 (une luminosité ne peut pas être négative).
analyserCapteur(p1) (P01 désactivé)
  -> [capteur inactif] Impossible d'analyser le capteur P01 : le capteur est inactif.
```

**Tests — toutes les exceptions des capteurs de manière commune**
```java
try { ... }
catch (CapteurException e) {
    System.out.println("  -> Erreur sur le capteur " + e.getIdentifiant() + " : " + e.getMessage());
}
```
Un seul `catch` intercepte les deux types, puisque ce sont tous deux des `CapteurException`.

> Si l'on combine les deux approches, le `catch` le plus spécifique doit être écrit **avant** le plus général. `catch (CapteurException e)` suivi de `catch (CapteurInactifException e)` ne compile pas, car le second ne pourrait jamais être atteint.

**Intérêt d'une classe `CapteurException` commune ?**
- On peut **traiter toutes les erreurs de capteur en une fois** (`catch (CapteurException e)`) quand le traitement est le même, ou les distinguer quand c'est utile.
- Une méthode peut déclarer `throws CapteurException` au lieu de lister chaque sous-classe.
- On peut ajouter plus tard un nouveau type d'erreur de capteur (par exemple `CapteurDeconnecteException`) sans modifier les `catch (CapteurException e)` existants.
- Ce qui est commun à toutes ces erreurs, ici l'**identifiant** du capteur, est écrit une seule fois dans la classe mère.
- On ne mélange pas les erreurs de capteurs avec les autres erreurs du programme, comme on le ferait avec `catch (Exception e)`.

**Lien avec l'héritage du TP précédent ?**
C'est exactement le même mécanisme que `Capteur` / `CapteurTemperature` / `CapteurLuminosite` :
- **Une exception est une classe** : `extends` crée une relation « est un ». Une `CapteurInactifException` *est une* `CapteurException`, qui *est une* `Exception`.
- **Héritage des membres** : `getIdentifiant()` est défini une seule fois dans `CapteurException` et utilisable sur les deux sous-classes. De même, `getMessage()` est hérité de `Throwable`.
- **Surclassement / polymorphisme** : `catch (CapteurException e)` accepte n'importe quelle sous-classe, comme une variable `Capteur c` acceptait un `CapteurTemperature`, ou comme `ArrayList<Capteur>` contenait tous les types de capteurs.
- **`super(...)`** : le constructeur transmet le message à la classe mère, comme `super(identifiant, piece)` dans les capteurs.
- `CapteurException` joue le rôle d'une classe « générique » commune, comme `Capteur` dans le TP précédent.

## Partie 8 — Ajouter des informations dans une exception

```java
public class ValeurCapteurInvalideException extends CapteurException {

    private double valeur;

    public ValeurCapteurInvalideException(String identifiant, double valeur, String contrainte) {
        super(identifiant, "Valeur incorrecte pour le capteur " + identifiant + " (" + contrainte + ").");
        this.valeur = valeur;
    }

    public double getValeur() { return valeur; }
}
```
L'identifiant est stocké dans `CapteurException` et récupéré avec `getIdentifiant()`, hérité. La valeur est récupérée avec `getValeur()`.

Code qui intercepte l'exception :
```java
} catch (ValeurCapteurInvalideException e) {
    System.out.println("Valeur incorrecte pour le capteur " + e.getIdentifiant() + ".");
    System.out.println("Valeur reçue : " + e.getValeur());
}
```
Tests :
```
Valeur incorrecte pour le capteur T01.
Valeur reçue : 150.0
Valeur incorrecte pour le capteur T01.
Valeur reçue : -51.0
Valeur incorrecte pour le capteur T01.
Valeur reçue : 80.5
Valeur incorrecte pour le capteur T01.
Valeur reçue : NaN
Valeur incorrecte pour le capteur L01.
Valeur reçue : -0.5
```
> `NaN` (« not a number ») est accepté par `Double.parseDouble("NaN")`. Comme toute comparaison avec `NaN` est fausse, `NaN < -50 || NaN > 80` vaut `false` : sans le test `Double.isNaN(...)` ajouté dans les setters, cette valeur aurait été acceptée.

**Une exception Java est-elle seulement un message d'erreur ?**
Non. C'est un **objet**, instance d'une classe qui hérite de `Throwable`. Il contient :
- un **type** (sa classe), qui permet de choisir le bon `catch` ;
- un **message** (`getMessage()`) ;
- la **pile d'appels** (`getStackTrace()`, affichée par `printStackTrace()`), qui indique où l'exception est née ;
- éventuellement une **cause** (`getCause()`, Partie 15) ;
- et, comme tout objet, ses **propres attributs et méthodes** : ici `getIdentifiant()` et `getValeur()`.

Le code qui intercepte l'exception peut donc utiliser ces données pour réagir, et pas seulement les afficher.

## Partie 9 — Sauvegarder les capteurs dans un fichier

Ajouts dans les classes :
- dans `Capteur` : deux méthodes abstraites `getType()` (`"TEMPERATURE"`, `"LUMINOSITE"`, `"PRESENCE"`) et `getValeurTexte()`, et une méthode `versLigne()` qui construit `type;identifiant;piece;actif;valeur`. Chaque sous-classe fournit son type et sa valeur, par polymorphisme ;
- les accesseurs `getTemperature()`, `getLuminosite()` et `isPresence()`.

```java
public static void sauvegarder(ArrayList<Capteur> capteurs, String nomFichier) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier))) {
        for (Capteur capteur : capteurs) {
            writer.write(capteur.versLigne());
            writer.newLine();
        }
    }
}
```
`TP3/capteurs.txt` obtenu :
```
TEMPERATURE;T01;Salle A;true;21.5
LUMINOSITE;L01;Hall;true;640.0
PRESENCE;P01;Salle B;true;true
```
Avec 5 capteurs, dont un capteur désactivé :
```
...
TEMPERATURE;T02;Bureau;false;19.0
PRESENCE;P02;Couloir;true;false
```

**Que se passe-t-il si le fichier ne peut pas être créé ou écrit ?**
Test avec un dossier qui n'existe pas (`TP3/dossier_inexistant/capteurs.txt`) :
```
java.io.FileNotFoundException: TP3\dossier_inexistant\capteurs.txt (The system cannot find the path specified)
```
Le constructeur `new FileWriter(...)` lève une **`FileNotFoundException`**, qui est une sous-classe d'**`IOException`**. On obtient la même exception si le fichier est en lecture seule ou si l'on n'a pas les droits. Une erreur pendant l'écriture elle-même (disque plein…) lève une `IOException`.

**Doit-elle obligatoirement être traitée ou déclarée ?**
Oui. `IOException` est une exception **vérifiée** : sans `try/catch` ni `throws IOException`, le compilateur refuse le code (`unreported exception IOException; must be caught or declared to be thrown`). Ici, `sauvegarder` la **déclare** (`throws IOException`) et laisse l'appelant décider du message à afficher.

## Partie 10 — Garantir la fermeture d'un fichier

```java
public static void sauvegarderAvecFinally(ArrayList<Capteur> capteurs, String nomFichier) throws IOException {
    BufferedWriter writer = null;
    try {
        writer = new BufferedWriter(new FileWriter(nomFichier));
        for (Capteur capteur : capteurs) {
            writer.write(capteur.versLigne());
            writer.newLine();
        }
    } finally {
        if (writer != null) {   // l'ouverture a pu échouer : writer est alors null
            writer.close();
        }
    }
}
```
**Test.** Une liste contenant `null` en 2e position provoque une `NullPointerException` au milieu de l'écriture :
```
Exception pendant l'écriture : NullPointerException
Contenu du fichier (vidé sur le disque par close() dans finally) :
  TEMPERATURE;T01;Salle A;true;21.5
```
C'est la preuve que `close()` a bien été exécuté malgré l'exception. Un `BufferedWriter` garde les données en mémoire et ne les écrit sur le disque qu'à la fermeture : sans `close()`, le fichier serait resté vide.

**À quel moment les instructions de `finally` sont-elles exécutées ?**
**Toujours**, à la sortie du bloc `try` (et du `catch` éventuel), quelle que soit la façon dont on en sort : fin normale, exception, ou même `return`.

**Lorsqu'aucune exception ne se produit ?**
Tout le bloc `try` s'exécute, puis le bloc `finally`, puis la suite du programme. Le fichier est fermé normalement.

**Lorsqu'une exception se produit ?**
Le bloc `try` est interrompu à l'instruction fautive. Si un `catch` correspond, il s'exécute, puis le `finally` s'exécute. **Ensuite**, s'il n'y avait pas de `catch` adapté (comme ici), l'exception continue de se propager vers l'appelant. Le fichier est donc fermé **avant** que l'exception ne quitte la méthode.

## Partie 11 — Try-with-resources

```java
public static void sauvegarder(ArrayList<Capteur> capteurs, String nomFichier) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomFichier))) {
        for (Capteur capteur : capteurs) {
            writer.write(capteur.versLigne());
            writer.newLine();
        }
    }
}
```
La ressource déclarée entre parenthèses après `try` est **fermée automatiquement** à la fin du bloc, que celui-ci se termine normalement ou par une exception. Cela fonctionne avec tout objet qui implémente l'interface `AutoCloseable`.

**Tests :**
- écriture normale : les 3 lignes sont écrites ;
- exception pendant l'écriture (`null` dans la liste) : `NullPointerException`, et le fichier contient `TEMPERATURE;T01;Salle A;true;21.5`. Le comportement est identique à la version `finally`, donc le fichier a bien été fermé.

**Comparaison avec `finally`**

|                                   | `finally`                                     | try-with-resources                  |
|-----------------------------------|-----------------------------------------------|-------------------------------------|
| Fermeture                         | écrite à la main                              | automatique                         |
| Variable déclarée hors du `try`   | oui (`writer = null`)                         | non                                 |
| Test `writer != null`             | nécessaire                                    | inutile                             |
| Risque d'oubli de `close()`       | oui                                           | non                                 |
| Plusieurs ressources              | `finally` imbriqués ou complexes              | `try (A a = ...; B b = ...)`        |
| Exception dans `close()`          | **remplace** l'exception d'origine, perdue    | ajoutée comme exception « supprimée » (`getSuppressed()`), l'exception d'origine est conservée |

**Quelle solution nécessite le moins de code ?** Try-with-resources : 4 lignes de moins ici, sans variable `null` ni test.

**Pourquoi est-il particulièrement adapté aux fichiers ?**
Un fichier est une **ressource du système** qu'il faut **toujours** libérer : un fichier non fermé peut rester verrouillé, et les données en mémoire tampon peuvent ne jamais être écrites. Les opérations sur les fichiers peuvent échouer à tout moment (`IOException`). Try-with-resources garantit la fermeture dans tous les cas, avec un code plus court et sans risque d'erreur. Il ferme même plusieurs ressources dans l'ordre inverse de leur ouverture.

## Partie 12 — Charger les capteurs depuis un fichier

```java
public static ArrayList<Capteur> chargerStrict(String nomFichier) throws IOException, FormatCapteurException {
    ArrayList<Capteur> capteurs = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
        String ligne;
        int numeroLigne = 0;
        while ((ligne = reader.readLine()) != null) {
            numeroLigne++;
            if (!ligne.isBlank()) {
                capteurs.add(lireCapteur(ligne, numeroLigne));
            }
        }
    }
    return capteurs;
}
```
`lireCapteur` découpe la ligne avec `split(";")` et crée selon le type un `CapteurTemperature`, un `CapteurLuminosite` ou un `DetecteurPresence`. Si l'état vaut `false`, le capteur est désactivé. Le chargement de `TP3/capteurs.txt` affiche bien les 3 capteurs, T01, L01 et P01.

**Fichier inexistant** (`TP3/fichier_inconnu.txt`), avec `catch (FileNotFoundException e)` :
```
Impossible d'ouvrir le fichier TP3/fichier_inconnu.txt.
(java.io.FileNotFoundException: TP3\fichier_inconnu.txt (The system cannot find the file specified))
```
**Quelle exception ?** **`java.io.FileNotFoundException`**, sous-classe d'`IOException`, levée par le constructeur `new FileReader(...)`. Avec l'API `java.nio` (`Files.readAllLines`), on obtiendrait une `NoSuchFileException`, elle aussi une `IOException`.

## Partie 13 — Un fichier contenant des données incorrectes

**Comportement avant `FormatCapteurException`** (chargement simple, sans vérification) :

| Fichier                                | Résultat                                                                              |
|----------------------------------------|---------------------------------------------------------------------------------------|
| `LUMINOSITE;L01;Hall;true;bonjour`     | arrêt : `NumberFormatException: For input string: "bonjour"`                          |
| `TEMPERATURE;T01;Salle A`              | arrêt : `ArrayIndexOutOfBoundsException: Index 4 out of bounds for length 3`          |
| `INCONNU;X01;Salle C;true;25`          | **aucune erreur**, la ligne est ignorée en silence : `0 capteur(s) chargé(s)`         |

Les deux premiers cas font planter le programme avec des messages techniques qui ne mentionnent ni le fichier, ni la ligne. Le troisième cas est le plus dangereux : une donnée est **perdue sans que personne ne le sache**. On remarque aussi que `Boolean.parseBoolean` renvoie `false` pour n'importe quel texte (`"bonjour"` → `false`) au lieu de signaler l'erreur.

**`FormatCapteurException`** : elle conserve le numéro et le contenu de la ligne :
```java
public class FormatCapteurException extends Exception {
    private int numeroLigne;
    private String ligne;
    // + constructeur (numeroLigne, ligne, message) et getNumeroLigne(), getLigne()
}
```
`lireCapteur` vérifie chaque point du format et lève cette exception en cas de problème :
- nombre de champs différent de 5 ;
- état autre que `true` ou `false` ;
- valeur non numérique, ou hors limites, pour la température et la luminosité ;
- présence autre que `true` ou `false` ;
- type inconnu.

Résultats :
```
Erreur dans le fichier à la ligne 2 :
LUMINOSITE;L01;Hall;true;bonjour

Valeur incorrecte pour un capteur de luminosité.
```
```
Erreur dans le fichier à la ligne 1 :
TEMPERATURE;T01;Salle A

5 champs attendus (type;identifiant;piece;actif;valeur), 3 trouvé(s).
```
```
Erreur dans le fichier à la ligne 1 :
INCONNU;X01;Salle C;true;25

Type de capteur inconnu : INCONNU.
```

## Partie 14 — Continuer le chargement malgré une erreur

Le `try/catch (FormatCapteurException e)` est placé **dans la boucle**, autour du traitement d'**une** ligne :
```java
while ((ligne = reader.readLine()) != null) {
    numeroLigne++;
    ...
    try {
        Capteur capteur = lireCapteur(ligne, numeroLigne);
        capteurs.add(capteur);
        System.out.println(capteur.getIdentifiant() + " chargé.");
    } catch (FormatCapteurException e) {
        lignesIncorrectes++;
        afficherErreur(e);
    }
}
```
Résultat :
```
T01 chargé.
L01 chargé.

Erreur ligne 3 : Valeur incorrecte pour un capteur de température.
TEMPERATURE;T02;Salle B;true;erreur
Cause : java.lang.NumberFormatException: For input string: "erreur"

P01 chargé.
T03 chargé.

Chargement terminé.
4 capteurs chargés.
1 ligne incorrecte.
```

**Traiter autour de tout le chargement, ou pour chaque ligne ?**
- **Autour de tout le chargement** (`chargerStrict`, Parties 12–13) : à la première erreur, l'exception fait sortir de la boucle, et le reste du fichier n'est **jamais lu**. C'est le comportement « tout ou rien » : on ne récupère aucun capteur, même si une seule ligne est mauvaise. Ce choix peut être voulu quand un fichier partiellement correct ne doit pas être utilisé.
- **Pour chaque ligne** (`charger`, Partie 14) : l'exception est traitée **dans** l'itération. La boucle continue avec la ligne suivante, et seule la ligne fautive est perdue. On peut aussi compter et signaler les erreurs.
- Dans les deux cas, l'`IOException` (fichier introuvable…) reste propagée **hors** de la boucle : si le fichier ne peut pas être lu, il est inutile de continuer. **L'endroit du `catch` détermine jusqu'où l'erreur interrompt le programme.**

## Partie 15 — Conserver la cause d'une exception

Java permet de **chaîner** les exceptions : le constructeur `Exception(String message, Throwable cause)` mémorise l'exception d'origine, qu'on récupère ensuite avec `getCause()`. On peut aussi utiliser `initCause(...)` après la création.

```java
public FormatCapteurException(int numeroLigne, String ligne, String message, Throwable cause) {
    super(message, cause);
    ...
}
```
Dans `lireCapteur`, avec un *multi-catch* (`|`) :
```java
try {
    capteur = new CapteurTemperature(identifiant, piece, Double.parseDouble(valeur));
} catch (NumberFormatException | ValeurCapteurInvalideException e) {
    throw new FormatCapteurException(numeroLigne, ligne,
            "Valeur incorrecte pour un capteur de température.", e);   // e = la cause
}
```
Résultats :
```
Erreur ligne 3 : Valeur incorrecte pour un capteur de température.
Cause : java.lang.NumberFormatException: For input string: "bonjour"

Erreur ligne 3 : Valeur incorrecte pour un capteur de température.
Cause : TP3.ValeurCapteurInvalideException: Valeur incorrecte pour le capteur T02 (température comprise entre -50.0 et 80.0 °C attendue).
```
Le même message « Valeur incorrecte » cache deux causes différentes : un texte qui n'est pas un nombre, et un nombre hors limites. Seule la cause permet de les distinguer. Avec `e.printStackTrace()`, Java affiche les deux piles d'appels, celle de la cause étant précédée de `Caused by: ...`.

**Intérêt de conserver la cause ?**
- **Ne pas perdre d'information** : la `FormatCapteurException` donne le **contexte métier** (quelle ligne de quel fichier), et la cause donne le **détail technique** (quelle conversion a échoué, et où dans le code).
- **Débogage** : la pile d'appels de la cause mène directement à l'instruction fautive.
- **Abstraction** : l'appelant ne manipule qu'une `FormatCapteurException` et n'a pas besoin de connaître `NumberFormatException`, `ValeurCapteurInvalideException`, etc. Le détail reste pourtant accessible si nécessaire.
- Sans cause, on « avalerait » l'exception d'origine : on saurait qu'il y a une erreur, mais plus pourquoi.

## Partie 16 — Exceptions vérifiées et non vérifiées

```
Throwable
├── Error                               (non vérifiées : problèmes graves de la JVM)
└── Exception                           ← vérifiées...
    ├── RuntimeException                ← ...sauf cette branche : non vérifiées
    │   ├── IllegalArgumentException
    │   │   └── NumberFormatException
    │   └── IndexOutOfBoundsException
    ├── IOException
    ├── CapteurException
    │   ├── CapteurInactifException
    │   └── ValeurCapteurInvalideException
    └── FormatCapteurException
```
- **Exception vérifiée (*checked*)** : le **compilateur** oblige à la traiter (`try/catch`) ou à la déclarer (`throws`). Sinon : `unreported exception ...; must be caught or declared to be thrown`. Ce sont les classes qui héritent d'`Exception` **sans** passer par `RuntimeException`.
- **Exception non vérifiée (*unchecked*)** : aucune obligation. On *peut* l'intercepter, mais le compilateur ne vérifie rien. Ce sont les classes qui héritent de `RuntimeException` (ou d'`Error`).

| Exception                         | Catégorie          | Raison                                                     |
|-----------------------------------|--------------------|------------------------------------------------------------|
| `NumberFormatException`           | **non vérifiée**   | hérite de `IllegalArgumentException` → `RuntimeException`  |
| `IndexOutOfBoundsException`       | **non vérifiée**   | hérite de `RuntimeException`                               |
| `IOException`                     | **vérifiée**       | hérite directement d'`Exception`                           |
| `CapteurInactifException`         | **vérifiée**       | `CapteurException` → `Exception`                           |
| `ValeurCapteurInvalideException`  | **vérifiée**       | `CapteurException` → `Exception`                           |
| `FormatCapteurException`          | **vérifiée**       | hérite directement d'`Exception`                           |

Les trois dernières sont vérifiées **parce que nous l'avons choisi** en héritant d'`Exception`. En héritant de `RuntimeException`, elles auraient été non vérifiées.

**Pourquoi Java n'impose-t-il pas le même traitement pour toutes les exceptions ?**
- Les exceptions **vérifiées** représentent des situations **normales mais imprévisibles**, extérieures au programme : un fichier absent, un disque plein, un réseau coupé, une donnée externe incorrecte. Le programme ne peut pas les éviter, même s'il est parfaitement écrit. Il est donc raisonnable d'**obliger** le développeur à prévoir une réaction.
- Les exceptions **non vérifiées** signalent le plus souvent une **erreur de programmation** évitable : un indice hors limites, un appel sur `null`, un argument invalide. Il vaut mieux corriger le code (vérifier l'indice, tester `null`) que d'entourer chaque instruction d'un `try/catch`.
- Si toutes les exceptions étaient vérifiées, il faudrait des `try/catch` ou des `throws` partout. Chaque accès à un tableau, chaque appel de méthode et chaque division peuvent lever une exception : le code deviendrait illisible. Les développeurs prendraient aussi l'habitude d'écrire des `catch` vides, ce qui est pire que tout.
- Cas de `NumberFormatException` : elle est non vérifiée, alors qu'elle provient souvent d'une **saisie utilisateur**. C'est pourquoi nous avons quand même choisi de l'intercepter dans le menu.

## Partie 17 — Application finale

`Main.java` propose le menu demandé. Le principe de robustesse est le suivant :
- **un `try/catch` dans la boucle du menu** : quelle que soit l'erreur dans une opération, on affiche un message et on **revient au menu** ;

  | Exception interceptée               | Situation                                         |
  |-------------------------------------|---------------------------------------------------|
  | `NumberFormatException`             | choix, numéro ou mesure non numérique             |
  | `IndexOutOfBoundsException`         | numéro de capteur inexistant                      |
  | `ValeurCapteurInvalideException`    | température hors [-50 ; 80], luminosité négative  |
  | `IllegalArgumentException`          | réponse autre que oui/non pour la présence        |
  | `NoSuchElementException`            | fin de l'entrée clavier : on quitte proprement    |

- **des `try/catch` plus locaux** quand une erreur ne doit pas tout interrompre ou demande un message précis :
  - *Analyser* : un capteur désactivé n'empêche pas l'analyse des suivants (`catch` dans la boucle) ;
  - *Charger* : `FileNotFoundException` → « Impossible d'ouvrir le fichier … ». Les lignes incorrectes sont signalées et ignorées (`FichierCapteurs.charger`, Partie 14) ;
  - *Sauvegarder* : `IOException` → « Impossible d'écrire dans le fichier … ».

**Tests effectués** (après chaque erreur, le menu réapparaît) :
```
Votre choix : bonjour
Erreur : vous devez saisir un nombre.

Votre choix : 9
Erreur : choix inconnu.

Votre choix : 2   Numéro du capteur : 15
Erreur : aucun capteur ne correspond à ce numéro.

Votre choix : 2   Numéro du capteur : 0   Nouvelle température : 150
Erreur : Valeur incorrecte pour le capteur T01 (température comprise entre -50.0 et 80.0 °C attendue).
Valeur reçue : 150.0

Votre choix : 2   Numéro du capteur : 1   Nouvelle luminosité : -20
Erreur : Valeur incorrecte pour le capteur L01 (une luminosité ne peut pas être négative).
Valeur reçue : -20.0

Votre choix : 4   Numéro du capteur : 2      →  Capteur P01 désactivé.
Votre choix : 5
T01 : Température normale
L01 : Luminosité normale
P01 : Erreur : Impossible d'analyser le capteur P01 : le capteur est inactif.

Votre choix : 6   Nom du fichier : TP3/fichier_inconnu.txt
Impossible d'ouvrir le fichier TP3/fichier_inconnu.txt.

Votre choix : 6   Nom du fichier : TP3/fichiers/erreur_ligne3.txt
T01 chargé.
L01 chargé.
Erreur ligne 3 : Valeur incorrecte pour un capteur de température.
TEMPERATURE;T02;Salle B;true;erreur
Cause : java.lang.NumberFormatException: For input string: "erreur"
P01 chargé.
T03 chargé.
Chargement terminé.
4 capteurs chargés.
1 ligne incorrecte.

Votre choix : 7   Nom du fichier : TP3/dossier_inexistant/x.txt
Impossible d'écrire dans le fichier TP3/dossier_inexistant/x.txt.
```
Les cas sans erreur ont aussi été vérifiés : modification d'une température et d'une présence, activation et désactivation, sauvegarde puis rechargement.
