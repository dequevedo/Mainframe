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
    private HandlerChatbot chatbot;
    private TCPServerConnection connection;
    private BufferedReader input;

    public ServerCommandHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.connection = new TCPServerConnection(socket);
        stats = new HandlerStats();
        directory = new HandlerDirectory();
        chatbot = new HandlerChatbot();
    }

    @Override
    public void run() {
        Socket socket;

        while (true) {
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
                
                int i=0;
                while(tok.hasMoreElements()){
                    subMessages[i] = tok.nextToken();
                    i++;
                }

                switch (subMessages[0]) {
                    case "files":
                        System.out.println(directory.FolderStatus());
                        String messageReturn = directory.FolderStatus();
                        System.out.println("GetStatus: " + messageReturn);
                        this.connection.SendToClient(messageReturn);
                        break;
                    case "status":
                        this.connection.SendToClient(stats.getStatus());
                        System.out.println(stats.getStatus());
                        break;
                    case "chatbot":
                        if (subMessages.length >= 2) {
                            if (subMessages[1] != null) {
                                if (subMessages[1].equals("none")) {
                                    System.out.println("none");
                                    this.connection.SendToClient("Olá! faça-me uma pergunta! :D");
                                } else {
                                    System.out.println("returnAnswer");
                                    this.connection.SendToClient(this.chatbot.returnAnswer(fullMessage));
                                    System.out.println("enviou para o cliente: "+this.chatbot.returnAnswer(fullMessage));
                                }
                            }
                        }
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
