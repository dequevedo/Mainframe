package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerAtivosMain extends Thread {

    private List<ServerCommandHandler> serverCommandHandlerList;
    private ServerSocket server;
    private StringBuilder messagesChat;


    public TCPServerAtivosMain(int porta) throws IOException {
        this.messagesChat = new StringBuilder(); //inicia a variavel que guarda as mensagens do chat (talk)
        this.messagesChat.append("Chat: ;;");
        this.server = new ServerSocket(porta); //cria o ServerSocket passando como parâmetro a porta
        System.out.println(this.getClass().getSimpleName() + " rodando na porta: " + server.getLocalPort());
        this.serverCommandHandlerList = new ArrayList<>(); //inicializa a lista de serverCommandHandler (que são threads que lidam com as mensagens de seu cliente)
    }

    @Override
    public void run() {
        Socket socket;
        while (true) {
            try {
                socket = this.server.accept(); //comando que aceita conexões de clientes
                ServerCommandHandler serverCommandHandler = new ServerCommandHandler(socket, this); //cria a thread que lidará com as mensagens desse cliente
                this.serverCommandHandlerList.add(serverCommandHandler); //adiciona a uma lista (para conseguir fazer broadcast para todos os clientes posteriormente)
                System.out.println("novo cliente: "+this.serverCommandHandlerList.size());
                serverCommandHandler.start(); //inicia a thread responsável por esse cliente, e assim começa a esperar por mensagens e responde-las
            } catch (IOException ex) {
                System.out.println("Erro 4: " + ex.getMessage());
            }
        }
    }
    
    public String getMessage(){
        System.out.println("getMessage (Main)");
        return this.messagesChat.toString(); //retorna as mensagens do chat
    }
    
    public void addMessage(String message){ //adiciona a mensagem recebida como parâmetro nas mensagens do chat (essa msg ja vem com o nome do cliente que mandou)
        
        this.messagesChat.append(message);
        this.messagesChat.append(";");
        System.out.println("(Main) message "+message+" added");
        
        for(ServerCommandHandler cli: serverCommandHandlerList){ //broadcast para todos os clientes conectados (se estiverem com chat ativo (canTalk))
            if(cli.canTalk){
                cli.Send(messagesChat.toString());
            }
        }
    }

    public synchronized void removeConnection(ServerCommandHandler connection) { //remove o cliente que desconectar da lista e fecha o leitor da entrada e o writer da saída, além de fechar o socket
        serverCommandHandlerList.remove(connection);
        try {
            connection.getInput().close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        connection.getOutput().close();
        try {
            connection.getSocket().close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List getConnections() { //retorna a lista de clientes
        return serverCommandHandlerList;
    }

    @Override
    protected void finalize() throws Throwable { //finaliza o servidor
        super.finalize();
        this.server.close();
    }
}
