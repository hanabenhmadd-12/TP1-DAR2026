package clientPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;
public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Je suis un client pas encore connecté…");
            InetAddress adresseServeur = InetAddress.getByName("192.168.1.15");
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(adresseServeur, 1234));
            System.out.println("je suis un client connecté à " + adresseServeur.getHostAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner clavier = new Scanner(System.in);
            int choix;
            do {
                System.out.println("\nMENU ");
                System.out.println("1. Addition");
                System.out.println("2. Soustraction");
                System.out.println("3. Multiplication");
                System.out.println("4. Division");
                System.out.println("0. Quitter");
                System.out.print("Votre choix : ");
                choix = clavier.nextInt();
                out.writeInt(choix);
                if (choix >= 1 && choix <= 4) {
                    System.out.print("Premier nombre : ");
                    int a = clavier.nextInt();
                    System.out.print("Deuxième nombre : ");
                    int b = clavier.nextInt();
                    out.writeInt(a);
                    out.writeInt(b);
                    String resultat = in.readUTF();
                    System.out.println("Résultat : " + resultat);
                } else if (choix != 0) {
                    System.out.println("Choix invalide, réessayez.");
                    out.writeInt(0);
                    out.writeInt(0);
                    System.out.println("Réponse du serveur : " + in.readUTF());
                }
            } while (choix != 0);
            System.out.println("Communication terminée.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}