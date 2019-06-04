package bolinha3;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receive implements Runnable{

    public Socket socket;
    public BufferedReader input;
    public String dataRead=""; 

    public Receive(Socket socket) throws IOException {
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

    @Override
    public void run() {
        while(true){
            try {
                this.dataRead = this.input.readLine();
            } catch (IOException ex) {
                System.out.println("Erro ao ler a mensagem: "+ex.getMessage());
            }
        }
    }
}
