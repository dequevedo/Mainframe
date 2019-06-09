package Servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerCommandHandler extends Thread {

    private Socket socket;
    private HandlerStats stats;
    private HandlerDirectory directory;
    private HandlerChatbot chatbot;
    private TCPServerAtivosMain caller;
    private BufferedReader input;
    private PrintWriter output;
    private String clienteName = "cliente";
    public boolean canTalk = false;

    public ServerCommandHandler(Socket socket, TCPServerAtivosMain caller) throws IOException {
        this.caller = caller;
        
        this.socket = socket;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.output = new PrintWriter(this.socket.getOutputStream(), true);
        
        stats = new HandlerStats();
        directory = new HandlerDirectory();
        chatbot = new HandlerChatbot();
    }
    
    public void Send(String message) {
        this.output.println(message);
        this.output.flush();
    }
    
    public BufferedReader getInput(){
        return this.input;
    }
    
    public PrintWriter getOutput(){
        return this.output;
    }
    
    public Socket getSocket(){
        return this.socket;
    }

    @Override
    public void run() {
        while (true) {
            String fullMessage = "";
            try {
                fullMessage = this.input.readLine(); //comando bloqueante em que o servidor fica aguardando o recebimento de algum comando do seu cliente.
            } catch (IOException ex) {
                Logger.getLogger(ServerCommandHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("recebido: " + fullMessage);
            if (fullMessage == null || fullMessage.equals("")) { //se a mensagem for nula ou vazia, não faz nada
            } else {  //se houver algo na mensage, divide ela com o tokenizer "|", cria um vetor com as mensagens divididas,verifica e encaminha os comandos
                StringTokenizer tok = new StringTokenizer(fullMessage, "|");
                String[] subMessages = new String[tok.countTokens()];

                int i = 0;
                while (tok.hasMoreElements()) {
                    subMessages[i] = tok.nextToken();
                    System.out.println("tok[" + i + "] = " + subMessages[i]);
                    i++;
                }

                switch (subMessages[0]) {
                    case "files": //se receber o comando files, verifica se recebeu 3 ou mais atributos (talk+comando+parametro), e chama o metodo responsável na classe HandlerDirectory, depois retorna a resposta para o cliente
                        canTalk = false;
                        if (subMessages.length >= 3) {
                            if (subMessages[1] != null) {
                                if (subMessages[1].equals("cd") || subMessages[1].equals("cd ")) {
                                    if (subMessages[2].equals("..") || subMessages[2].equals(".. ")) {
                                        Send(";;--------" + this.directory.NavigateBack() + "--------;;" + this.directory.FolderStatus());
                                    } else if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        Send(";;--------" + this.directory.NavigateForward(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("createfile") || subMessages[1].equals("createfile ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        Send(";;--------" + this.directory.CreateFile(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("createfolder") || subMessages[1].equals("createfolder ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        Send(";;--------" + this.directory.CreateFolder(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("copy") || subMessages[1].equals("copy ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null && !subMessages[3].equals("") && subMessages[3] != null) {
                                        Send(";;--------" + this.directory.CopyFile(subMessages[2], subMessages[3])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("move") || subMessages[1].equals("move ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null && !subMessages[3].equals("") && subMessages[3] != null) {
                                        Send(";;--------" + this.directory.MoveFile(subMessages[2], subMessages[3])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                } else if (subMessages[1].equals("delete") || subMessages[1].equals("delete ")) {
                                    if (!subMessages[2].equals("") && subMessages[2] != null) {
                                        Send(";;--------" + this.directory.DeleteFile(subMessages[2])
                                                + "--------;;" + this.directory.FolderStatus());
                                    }
                                }
                            }
                        } else if (subMessages.length == 1) {
                            Send(this.directory.FolderStatus());
                        }

                        break;
                    case "status": //ao receber o comando status, acessa a classe stats, e manda a resposta de volta para o cliente (CPU, RAM, HD ...)
                        canTalk = false;
                        Send(stats.getStatus());
                        System.out.println(stats.getStatus());
                        break;
                        
                    case "chatbot": //ao receber o comando chatbot, se ouver um segundo atributo, manda a msg para ser tratada na classe chatbot e depois retorna a resposta ao usuário
                        canTalk = false;
                        if (subMessages.length >= 2) {
                            if (subMessages[1] != null) {
                                if (subMessages[1].equals("none")) {
                                    System.out.println("none");
                                    Send("Olá! faça-me uma pergunta! :D");
                                } else {
                                    System.out.println("returnAnswer");
                                    Send(this.chatbot.returnAnswer(fullMessage));
                                    System.out.println("enviou para o cliente: " + this.chatbot.returnAnswer(fullMessage));
                                }
                            }
                        }
                        break;
                        
                    case "database": //comando database ainda sem implementação
                        canTalk = false;
                        System.out.println("recebeu database");
                        Send("; Em breve!");
                        break;
                        
                    //ao receber o comando talk, verifica se possui mais outros atributos, 
                    //se possuir, adiciona o segundo atributo como nova mensagem e atualiza as mensagens para o cliente
                    //se não possuir, apenas atualiza as mensagens para o cliente
                    case "talk": 
                        canTalk = true;
                        System.out.println("recebeu talk");

                        if (subMessages.length >= 2) {
                            this.caller.addMessage(this.clienteName + " says: " + subMessages[1]);
                        } else {
                            Send(this.caller.getMessage());
                        }

                        break;
                    case "name": //caso receba o comando name, seta o segundo token como o nome desse cliente.
                        String antName = this.clienteName;
                        this.clienteName = subMessages[1];
                        this.caller.addMessage("('" + antName + "' trocou o nome para: '" + this.clienteName + "')"); //adiciona mensagem no chat que esse cliente trocou de nome
                        if (!this.canTalk) {
                            Send("'" + antName + "' trocou o nome para: '" + this.clienteName + "'"); //se ele não estiver com chat ativo, retorna a msg que mudou de nome
                        }
                        System.out.println("Trocou o nome para: " + this.clienteName);
                        break;
                    default:
                        break;
                }
            }

        }
    }
}
