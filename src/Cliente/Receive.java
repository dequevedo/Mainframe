package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive extends Thread {

    public Socket socket;
    public BufferedReader input;
    private String dataRead = "";
    private ClienteInterface cInterface;

    public Receive(Socket socket, ClienteInterface cInterface) throws IOException {
        this.cInterface = cInterface;
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.dataRead = this.input.readLine();
                if (this.dataRead.equals("")) {
                    System.out.println("mensagem vazia");
                } else {
                    dataRead = dataRead.replace(";", "\n");
                    this.cInterface.setjText(this.dataRead);
                    this.dataRead = "";
                }
            } catch (IOException ex) {
                System.out.println("Erro ao ler a mensagem: " + ex.getMessage());
            }
        }
    }
}
