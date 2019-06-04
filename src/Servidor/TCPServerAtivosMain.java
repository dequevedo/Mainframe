package Servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerAtivosMain extends Thread {

    private List<ServerCommandHandler> serverCommandHandlerList;
    private ServerSocket server;
    public int x;
    public int y;
    public int d;
    public int inc;

    public TCPServerAtivosMain(int porta) throws IOException {
        this.server = new ServerSocket(porta);
        System.out.println(this.getClass().getSimpleName() + " rodando na porta: " + server.getLocalPort());
        this.serverCommandHandlerList = new ArrayList<>();
    }

    @Override
    public void run() {
        Socket socket;
        while (true) {
            try {
                socket = this.server.accept();
                ServerCommandHandler serverCommandHandler = new ServerCommandHandler(socket);
                newServer(serverCommandHandler);
                serverCommandHandler.start();
            } catch (IOException ex) {
                System.out.println("Erro 4: " + ex.getMessage());
            }
        }
    }
    
    
    public synchronized void newServer(ServerCommandHandler serverCommandHandler) throws IOException {
        serverCommandHandlerList.add(serverCommandHandler);
    }

    public synchronized void removeConnection(TCPServerConnection connection) {
        //connections.remove(connection);
//        try {
//            connection.getInput().close();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
//        connection.getOutput().close();
//        try {
//            connection.getSocket().close();
//        } catch (IOException ex) {
//            System.out.println(ex.getMessage());
//        }
    }

    public List getConnections() {
        return serverCommandHandlerList;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.server.close();
    }
}
