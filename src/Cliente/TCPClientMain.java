package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JTextArea;

public class TCPClientMain {

    private PrintWriter output;
    private Receive receiver;
    private Socket socket;

    public TCPClientMain(String serverAddress, int serverPort, ClienteInterface caller) throws UnknownHostException, IOException {
        this.socket = new Socket(serverAddress, serverPort);
        this.receiver = new Receive(this.socket, caller);
        this.receiver.start();
        this.output = new PrintWriter(this.socket.getOutputStream(), true);

    }

    public void writeMessage(String outMessage) {
        this.output.println(outMessage);
        this.output.flush();
    }

    public void closeConnection() throws IOException, Throwable {
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
