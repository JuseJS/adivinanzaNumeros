package servidortipo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class ServidorTIpo {

    public static void main(String[] args) {
        int port = 12345;

        try {
            Random random = new Random();
            int secretNum = random.nextInt(99) + 1;
            System.out.println("El número secreto es: " + secretNum);

            ServerSocket server = new ServerSocket(port);
            System.out.println("Servidor iniciado en el puerto: " + port + ".");

            while (true) {
                Socket client = server.accept();
                System.out.println("Cliente conectado: " + client.getInetAddress());

                // Manejar al cliente en un nuevo hilo
                new Thread(new ClientHandler(client, secretNum)).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Clase para manejar al cliente
    static class ClientHandler implements Runnable {

        private Socket client;
        private int secretNum;

        public ClientHandler(Socket client, int secretNum) {
            this.client = client;
            this.secretNum = secretNum;
        }

        @Override
        public void run() {
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                PrintWriter output = new PrintWriter(client.getOutputStream(), true);

                String line;
                while ((line = input.readLine()) != null) {
                    int clientNum = Integer.parseInt(line);
                    System.out.println("Número recibido: " + clientNum);

                    String answer;
                    if (clientNum < secretNum) {
                        answer = "Es mayor.";
                    } else if (clientNum > secretNum) {
                        answer = "Es menor.";
                    } else {
                        answer = "¡Correcto!";
                        output.println(answer);
                        break;
                    }
                    output.println(answer);
                }
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
