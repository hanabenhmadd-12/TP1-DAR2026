package serverPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Je suis un serveur en attente la connexion d'un client ");

            Socket socket = serverSocket.accept();
            System.out.println("un client est connecté");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            int x;
            do {
                x = in.readInt();
                System.out.println("Nombre reçu : " + x);
                int resultat = x * 5;
                out.writeInt(resultat);
                System.out.println("Résultat envoyé : " + resultat);
            } while (x != 0);

            System.out.println("Communication terminée, fermeture des sockets.");
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}