package Cliente;
import Servidor.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receive{

    public Socket socket;
    public BufferedReader input;

    public Receive(Socket socket)  throws IOException {
        this.socket = socket;
        try {
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    protected void finalize() throws Throwable {
        this.input.close();
        this.socket.close();
    }
    
    public String getData() throws IOException{
        return this.input.readLine();
    }
}
