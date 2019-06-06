package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCommandHandler extends Thread {

    private Socket socket;
    private HandlerStats stats;
    private HandlerDirectory directory;
    private TCPServerConnection connection;
    private BufferedReader input;

    public ServerCommandHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.connection = new TCPServerConnection(socket);
        stats = new HandlerStats();
        directory = new HandlerDirectory();
    }

    @Override
    public void run() {
        Socket socket;

        while (true) {

            System.out.print(".");
            //String fullMessage = this.connection.getMessage();
            String fullMessage = "";
            try {
                fullMessage = this.input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ServerCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("recebido: " + fullMessage);
            if (fullMessage == null || fullMessage.equals("")) {
            } else {
                //System.out.println(fullMessage);
                StringTokenizer tok = new StringTokenizer(fullMessage, "|");
                String[] subMessages = new String[tok.countTokens()];

                for (int i = 0; i < tok.countTokens(); i++) {
                    subMessages[i] = tok.nextToken();
                    //System.out.println("Mensagem " + i + " " + subMessages[i]);
                }

                switch (subMessages[0]) {
                    case "files":
                        System.out.println("recebeu files");
                        System.out.println(directory.getStatus());
                        String messageReturn = directory.getStatus();
                        System.out.println("GetStatus: " + messageReturn);
                        this.connection.SendToClient(messageReturn);
                        break;
                    case "status":
                        System.out.println("recebeu status");
                        this.connection.SendToClient(stats.getStatus());
                        System.out.println(stats.getStatus());
                        break;
                    case "chatbot":
                        System.out.println("recebeu chatbot");
                        this.connection.SendToClient("CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                        System.out.println("enviou: " + "CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                        break;
                    case "database":
                        System.out.println("recebeu database");
                        this.connection.SendToClient("CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                        System.out.println("enviou: " + "CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                        break;
                    case "talk":
                        System.out.println("recebeu talk");
                        this.connection.SendToClient("CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                        System.out.println("enviou: " + "CPU Usage: " + stats.GetCPUUsage() + ", RAM Usage: " + stats.GetRAMUsage());
                        break;
                    default:
                        break;
                }
                this.connection.clearMessage();
            }

        }
    }
}
