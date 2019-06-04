package bolinha3;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Send {

    public Socket socket;
    public PrintWriter output;

    public Send(Socket socket) throws IOException {
        this.socket = socket;
        try {
            this.output = new PrintWriter(this.socket.getOutputStream(), true);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void Send(String message) {
        this.output.println(message);
        this.output.flush();
    }

    @Override
    protected void finalize() throws Throwable {
        this.output.close();
        this.socket.close();
    }
}
