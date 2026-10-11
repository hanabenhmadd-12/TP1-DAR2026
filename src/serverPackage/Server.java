package serverPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            InetAddress adresse = InetAddress.getByName("192.168.1.15");
            ServerSocket serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(adresse, 1234));
            System.out.println("Serveur en attente sur " + adresse.getHostAddress() + ":1234");
            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté : " + socket.getInetAddress().getHostAddress());
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            int choix;
            do {
                choix = in.readInt();
                if (choix == 0) {
                    break;
                }
                int a = in.readInt();
                int b = in.readInt();
                System.out.println("Opération " + choix + " avec " + a + " et " + b);
                String resultat;
                switch (choix) {
                    case 1: resultat = String.valueOf(a + b); break;
                    case 2: resultat = String.valueOf(a - b); break;
                    case 3: resultat = String.valueOf(a * b); break;
                    case 4:
                        if (b == 0) {
                            resultat = "Erreur : division par zéro";
                        } else {
                            resultat = String.valueOf((double) a / b);
                        }
                        break;
                    default: resultat = "Choix invalide";
                }
                out.writeUTF(resultat);
                System.out.println("Résultat envoyé : " + resultat);
            } while (true);
            System.out.println("Communication terminée, fermeture des sockets.");
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}