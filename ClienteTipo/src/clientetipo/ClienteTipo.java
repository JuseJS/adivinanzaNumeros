package clientetipo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class ClienteTipo {

    public static void main(String[] args) {
        String host = "localhost";
        int port = 12345;
        boolean accepted = false;

        try {
            int min = 1;
            int max = 99;
            int num = getNum(min, max);

            Socket socket = new Socket(host, port);
            System.out.println("Conectado al servidor en el puerto " + port + ".");

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String answer;
            while (!accepted) {
                System.out.println("Intento con el número: " + num);
                output.println(num);

                answer = input.readLine();
                System.out.println("Respuesta del servidor: " + answer);

                if (answer != null) {
                    switch (answer) {
                        case "Es mayor.":
                            min = num + 1;
                            break;
                        case "Es menor.":
                            max = num - 1;
                            break;
                        case "¡Correcto!":
                            System.out.println("¡He adivinado el número: " + num + "!");
                            accepted = true;
                            break;
                        default:
                            break;
                    }
                    if (!accepted) {
                        num = getNum(min, max);
                    }
                } else {
                    break;
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNum(int min, int max) {
        Random random = new Random();
        return random.nextInt((max - min) + 1) + min;
    }
}
