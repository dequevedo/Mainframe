package Servidor;

import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerCommandHandler extends Thread {

    private Socket socket;
    private HandlerStats stats;
    private TCPServerConnection connection;

    public ServerCommandHandler(Socket socket) {
        this.socket = socket;
        this.connection = new TCPServerConnection(socket);
        stats = new HandlerStats();
    }

    @Override
    public void run() {
        Socket socket;
        while (true) {
            try {
                String fullMessage = this.connection.getMessage();

                System.out.println(fullMessage);
                
                if (fullMessage == null || fullMessage.equals("")) {
                } else {
                    StringTokenizer tok = new StringTokenizer(fullMessage, "|");
                    String[] subMessages = new String[tok.countTokens()];

                    for (int i = 0; i < tok.countTokens(); i++) {
                        subMessages[i] = tok.nextToken();
                        System.out.println("Mensagem " + i + " " + subMessages[i]);
                    }

                    switch (subMessages[0]) {
                        case "status":
                            System.out.println("recebeu status");
                            this.connection.SendToClient("CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                            System.out.println("enviou: " + "CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                            break;
                        default:
                            break;
                    }
                }
            } catch (Exception e) {

            }

//            try {
//                socket = this.server.accept();
//                TCPServerConnection connection = new TCPServerConnection(socket);
//                newConnection(connection);
//                (new TCPServerAtivosHandler(connection, this)).start();
//            } catch (IOException ex) {
//                System.out.println("Erro 4: " + ex.getMessage());
//            }
        }
    }
}
