package bolinha3;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TCPServerAtivosMain extends Thread {

    private List<TCPServerConnection> connections;
    private ServerSocket server;
    public int x;
    public int y;
    public int d;
    public int inc;

    public TCPServerAtivosMain(int porta) throws IOException {
        this.server = new ServerSocket(porta);
        System.out.println(this.getClass().getSimpleName() + " rodando na porta: " + server.getLocalPort());
        this.connections = new ArrayList<>();
        x = 0;
        y = 0;
        d = 10;
        inc = 3;
    }

    @Override
    public void run() {
        Socket socket;
        while (true) {
            try {
                socket = this.server.accept();
                TCPServerConnection connection = new TCPServerConnection(socket);
                newConnection(connection);
                (new TCPServerAtivosHandler(connection, this)).start();
            } catch (IOException ex) {
                System.out.println("Erro 4: " + ex.getMessage());
            }
        }
    }
    
    
    public synchronized void newConnection(TCPServerConnection connection) throws IOException {
        connections.add(connection);
    }

    public synchronized void removeConnection(TCPServerConnection connection) {
        connections.remove(connection);
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
        return connections;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        this.server.close();
    }
}
