package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class TCPClientHandler extends Thread {

    private Socket socket;
    private ClienteInterface caller;
    private BufferedReader input;

    public TCPClientHandler(Socket socket, ClienteInterface caller) throws IOException {
        this.socket = socket;
        this.caller = caller;
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                if (this.socket.isConnected() && this.input != null) {
                    message = this.input.readLine();
                } else {
                    break;
                }
                if (message == null || message.equals("")) {
                    break;
                }
                StringTokenizer tok = new StringTokenizer(message, "|");
                caller.x = Integer.parseInt(tok.nextToken());
                caller.y = Integer.parseInt(tok.nextToken());
                caller.d = Integer.parseInt(tok.nextToken());
                caller.inc = Integer.parseInt(tok.nextToken());
                caller.atualizaCampos();
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
    }
}
