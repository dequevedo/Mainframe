package bolinha3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TCPServerConnection {

    private Socket socket;
    private Send sender;
    private Receive receiver;

    public TCPServerConnection(Socket socket) {
        this.socket = socket;
        try {
            sender = new Send(socket);
            receiver = new Receive(socket);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.sender.finalize();
        this.receiver.finalize();
        this.socket.close();
    }

    public Socket getSocket() {
        return socket;
    }

    public String getMessage() {
        return receiver.dataRead;
    }

    public void SendToClient(String message) {
        sender.Send(message);
    }
}
