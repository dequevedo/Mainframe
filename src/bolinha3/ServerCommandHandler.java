package bolinha3;

import java.io.IOException;
import java.net.Socket;

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
            String message = this.connection.getMessage();
            if(message.equals("stats")){
                this.connection.SendToClient("O sistema está rodando corretamente!");
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
