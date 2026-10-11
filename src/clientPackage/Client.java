package clientPackage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            System.out.println("Je suis un client pas encore connecté…");

            Socket socket = new Socket("127.0.0.1", 1234);
            System.out.println("je suis un client connecté");

            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            Scanner clavier = new Scanner(System.in);

            int x;
            do {
                System.out.print("Tapez un entier x (0 pour arrêter) : ");
                x = clavier.nextInt();
                out.writeInt(x);
                int resultat = in.readInt();
                System.out.println("Résultat reçu (x * 5) : " + resultat);
            } while (x != 0);

            System.out.println("Communication terminée.");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}