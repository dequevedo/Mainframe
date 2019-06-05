package Cliente;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPClientMain {

    private Send sender;
    private Receive receiver;
    private Socket socket;

    public TCPClientMain(String serverAddress, int serverPort, ClienteInterface caller) throws UnknownHostException, IOException {
        this.socket = new Socket(serverAddress, serverPort);
        //this.socket.setKeepAlive(true);
        
        //cria o sender e o receiver
        this.sender = new Send(this.socket);
        this.receiver = new Receive(this.socket);
    }

    public void writeMessage(String outMessage) {
        this.sender.Send(outMessage);
    }

    public String readMessage() throws IOException {
        return this.receiver.getData();
    }

    public void closeConnection() throws IOException, Throwable {
        this.sender.finalize();
        this.receiver.finalize();
        this.socket.close();
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.closeConnection();
        } finally {
            super.finalize();
        }
    }

}
