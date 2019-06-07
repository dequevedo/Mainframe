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
    private TCPServerAtivosMain caller;
    private BufferedReader input;
    private String clienteName = "cliente";
    public boolean canTalk = false;

    public ServerCommandHandler(Socket socket, TCPServerAtivosMain caller) throws IOException {
        this.caller = caller;
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.connection = new TCPServerConnection(socket);
        stats = new HandlerStats();
        directory = new HandlerDirectory();
        chatbot = new HandlerChatbot();
    }

    public void sendMessage(String message) {
        this.connection.SendToClient(message);
    }

    @Override
    public void run() {
        Socket socket;

        while (true) {
            String fullMessage = "";
            try {
                fullMessage = this.input.readLine();
            } catch (IOException ex) {
                Logger.getLogger(ServerCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("recebido: " + fullMessage);
            if (fullMessage == null || fullMessage.equals("")) {
            } else {
                StringTokenizer tok = new StringTokenizer(fullMessage, "|");
                String[] subMessages = new String[tok.countTokens()];

                int i = 0;
                while (tok.hasMoreElements()) {
                    subMessages[i] = tok.nextToken();
                    System.out.println("tok[" + i + "] = " + subMessages[i]);
                    i++;
                }

                switch (subMessages[0]) {
                    case "files":
                        canTalk = false;
                        if (subMessages.length >= 3) {
                            if (subMessages[1] != null) {
                                if (subMessages[1].equals("cd") || subMessages[1].equals("cd ")) {
                                    if (subMessages[2].equals("..") || subMessages[2].equals(".. ")) {
                                        this.connection.SendToClient(";;--------" + this.directory.NavigateBack() + "--------;;" + this.directory.FolderStatus());
                                    } else if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        this.connection.SendToClient(";;--------" + this.directory.NavigateForward(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("createfile") || subMessages[1].equals("createfile ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        this.connection.SendToClient(";;--------" + this.directory.CreateFile(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("createfolder") || subMessages[1].equals("createfolder ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        this.connection.SendToClient(";;--------" + this.directory.CreateFolder(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("copy") || subMessages[1].equals("copy ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null && !subMessages[3].equals("") && subMessages[3] != null) {
                                        this.connection.SendToClient(";;--------" + this.directory.CopyFile(subMessages[2], subMessages[3])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("move") || subMessages[1].equals("move ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null && !subMessages[3].equals("") && subMessages[3] != null) {
                                        this.connection.SendToClient(";;--------" + this.directory.MoveFile(subMessages[2], subMessages[3])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("delete") || subMessages[1].equals("delete ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        this.connection.SendToClient(";;--------" + this.directory.DeleteFile(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                }
                            }
                        } else if (subMessages.length == 1) {
                            this.connection.SendToClient(this.directory.FolderStatus());
                        }

                        break;
                    case "status":
                        canTalk = false;
                        this.connection.SendToClient(stats.getStatus());
                        System.out.println(stats.getStatus());
                        break;
                    case "chatbot":
                        canTalk = false;
                        if (subMessages.length >= 2) {
                            if (subMessages[1] != null) {
                                if (subMessages[1].equals("none")) {
                                    System.out.println("none");
                                    this.connection.SendToClient("Olá! faça-me uma pergunta! :D");
                                } else {
                                    System.out.println("returnAnswer");
                                    this.connection.SendToClient(this.chatbot.returnAnswer(fullMessage));
                                    System.out.println("enviou para o cliente: " + this.chatbot.returnAnswer(fullMessage));
                                }
                            }
                        }
                        break;
                    case "database":
                        canTalk = false;
                        System.out.println("recebeu database");
                        this.connection.SendToClient("; Em breve!");
                        break;
                    case "talk":
                        canTalk = true;
                        System.out.println("recebeu talk");

                        if (subMessages.length >= 2) {
                            this.caller.addMessage(this.clienteName + " says: " + subMessages[1]);
                        } else {
                            this.connection.SendToClient(this.caller.getMessage());
                        }

                        break;
                    case "name":
                        String antName = this.clienteName;
                        this.clienteName = subMessages[1];
                        this.caller.addMessage("('" + antName + "' trocou o nome para: '" + this.clienteName + "')");
                        if (!this.canTalk) {
                            this.connection.SendToClient("'" + antName + "' trocou o nome para: '" + this.clienteName + "'");
                        }
                        System.out.println("Trocou o nome para: " + this.clienteName);
                        break;
                    default:
                        break;
                }
                this.connection.clearMessage();
            }

        }
    }
}
