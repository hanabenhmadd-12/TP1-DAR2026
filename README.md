# TP1-DAR2026 – Sockets TCP en mode connecté

**Hana Ben Hmad** – LSI3 – Développement d'applications réparties

# Activité 1-1 : Première connexion Client/Serveur

## Étape 1 : Vérification de Git
![git version](images/capture1.png)

Git est installé : la commande `git --version` affiche la version installée
(git version 2.56.0.windows.2).

## Étape 2 : Vérification de Java
![java version](images/capture2.png)

La commande `java --version` échoue (« Unrecognized option: --version ») car cette
option n'existe qu'à partir de Java 9. Avec le JDK 8 (Temurin 1.8.0_482), la commande
`java -version` affiche bien la version, et `javac -version` confirme la présence
du compilateur. Le projet est exécuté dans IntelliJ avec le JDK 25.

![dossier](images/capture3.png)

Création du répertoire de travail `hanabenhmad_LSI3_DevAppReparties` sur le Bureau.
Il contient le projet `TP1-DAR2026`.

## Étape 3 : Création du serveur
![projet](images/capture4.png)

Création du projet `TP1-DAR2026` avec dépôt Git, du package `serverPackage`
et de la classe `Server` (`ServerSocket` sur le port 1234, `accept()`, fermeture des sockets).

![console serveur](images/capture5.png)

**Interprétation** : le `ServerSocket` s'ouvre sur le port 1234, puis `accept()`
est une méthode bloquante : le serveur attend sans rien faire qu'un client se
connecte. Le message « un client est connecté » n'apparaît pas encore, ce qui
prouve qu'aucun client n'est connecté.

## Étape 4 : Création du client
![console client](images/capture6.png)

Client lancé avec `localhost` et le port 1234.

![console serveur](images/capture7.png)

**Interprétation** : le client crée un `Socket` vers `localhost:1234`. Le serveur
débloque son `accept()` et affiche « un client est connecté ». Le client affiche
« je suis un client connecté » : la connexion TCP est établie. Les deux programmes
se terminent après la fermeture des sockets (`Process finished with exit code 0`).

### Avec une adresse IP (127.0.0.1)
![ip](images/capture8.png)

`127.0.0.1` est l'adresse de bouclage, équivalente à `localhost` : le résultat
est identique, la connexion est établie.

---

# Activité 1-2 : Service de calculatrice

## Étape 1 : Préparation de l'espace de travail
Le projet `TP1-DAR2026` de l'Activité 1-1 est réutilisé et étendu : le client envoie
un entier au serveur, qui le multiplie par 5 et renvoie le résultat.
Les échanges utilisent `DataInputStream` et `DataOutputStream`.

## Étape 2 : Service de multiplication

### Code du serveur
![code serveur](images/capture9.png)

Le serveur attend un client, lit un entier avec `readInt()`, calcule `x * 5`,
puis renvoie le résultat avec `writeInt()`.

### Code du client
![code client](images/capture10.png)

Le client lit un entier au clavier (`Scanner`), l'envoie avec `writeInt()`,
puis reçoit et affiche le résultat avec `readInt()`.

### Tests avec différentes valeurs
![test positif](images/capture11.png)

**x = 7** : le client reçoit 35 (7 × 5).

![test négatif](images/capture12.png)

**x = -4** : le client reçoit -20. Les entiers négatifs sont bien transmis grâce à
`DataInputStream` / `DataOutputStream`, qui envoient l'entier sur 4 octets.

![test zéro](images/capture13.png)

**x = 0** : le client reçoit 0 (0 × 5 = 0).

### Version finale avec boucle
![boucle client](images/capture14.png)

Le client saisit plusieurs entiers successivement. Après chaque saisie, il envoie
l'entier, reçoit le résultat (x * 5) et l'affiche. Quand l'utilisateur saisit **0**,
la boucle s'arrête, le serveur répond 0 puis les deux côtés ferment leurs sockets
proprement (`Process finished with exit code 0`).

---

# Activité 1-3 : Amélioration du service de calculatrice

## Étape 1 : Connexion Client/Serveur avec l'adresse IP réelle

![ipconfig](images/capture15.png)

La commande `ipconfig` donne l'adresse IPv4 de la machine : **192.168.1.15**.

![code serveur](images/capture16.png)

Côté serveur, `InetAddress.getByName(...)` crée l'adresse IP, puis le `ServerSocket`
est lié avec `bind(new InetSocketAddress(adresse, 1234))` : le serveur écoute sur
l'IP réelle de la machine et sur le port 1234 (et non plus sur `localhost`).

![code client](images/capture17.png)

Côté client, `localhost` est remplacé par l'adresse IP du serveur, grâce à
`InetAddress` et `InetSocketAddress` avec `socket.connect(...)`.

![serveur en attente](images/capture18.png)

Le serveur affiche qu'il attend sur `192.168.1.15:1234`.

![client connecté](images/capture19.png)

Le client se connecte au serveur en utilisant l'adresse IP réelle.

![serveur connecté](images/capture20.png)

Le serveur affiche l'adresse IP du client connecté.

**Remarque** : ce test a été réalisé sur **une seule machine**, mais avec l'adresse IP
réelle (192.168.1.15) à la place de `localhost`. Pour deux machines distinctes du même
réseau local, il suffit de lancer `Client.java` sur la 2e machine avec l'IP du serveur
(192.168.1.15), et d'autoriser le port 1234 dans le pare-feu Windows du serveur.

## Étape 2 : Service de calculatrice amélioré

![code client menu](images/capture21.png)

Le client affiche un menu (1. Addition, 2. Soustraction, 3. Multiplication,
4. Division, 0. Quitter), lit le choix puis les deux nombres, et les envoie au serveur
   avec `writeInt()`.

![code serveur menu](images/capture22.png)

Le serveur lit le choix et les deux nombres, effectue le calcul avec un `switch`,
puis renvoie le résultat avec `writeUTF()`. La division par zéro est détectée et
renvoie un message d'erreur.

### Tests

![addition](images/capture23.png)

**Addition** : 8 + 3 = 11.

![soustraction](images/capture24.png)

**Soustraction** : 8 - 3 = 5.

![multiplication](images/capture25.png)

**Multiplication** : 8 × 3 = 24.

![division](images/capture26.png)

**Division** : 8 / 2 = 4.0, et 8 / 0 renvoie « Erreur : division par zéro ».