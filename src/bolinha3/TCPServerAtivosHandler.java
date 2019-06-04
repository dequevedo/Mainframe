package bolinha3;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

public class TCPServerAtivosHandler extends Thread {

    private TCPServerConnection cliente;
    private TCPServerAtivosMain caller;

    public TCPServerAtivosHandler(TCPServerConnection cliente, TCPServerAtivosMain caller) throws IOException {
        this.cliente = cliente;
        this.caller = caller;
    }

    @Override
    protected void finalize() throws Throwable {
        encerrar();
    }

    private void encerrar() {
        this.caller.removeConnection(this.cliente);
    }

    public synchronized void messageDispatcher(String message) throws IOException {
        List<TCPServerConnection> clientes = this.caller.getConnections();
        for (TCPServerConnection cli : clientes) {
            if (cli.getSocket() != null && cli.getSocket().isConnected() && cli.getMessage()!= "") {
                cli.SendToClient(message);
            }
        }
    }

    @Override
    public void run() {

        String message;
        while (true) {
            try {
                if (this.cliente.getSocket().isConnected() && this.cliente.getMessage()!= "") {
                    message = this.cliente.getMessage();
                } else {
                    break;
                }
                if (message == null || message.equals("")) {
                    break;
                }
                int tecla = Integer.parseInt(message);
                switch (tecla) {
                    case KeyEvent.VK_A:
                        if (caller.inc > 1) {
                            caller.inc--;
                        }
                        break;
                    case KeyEvent.VK_S:
                        if (caller.inc < 10) {
                            caller.inc++;
                        }
                        break;
                    case KeyEvent.VK_Q:
                        if (caller.d > 2) {
                            caller.d--;
                        }
                        break;
                    case KeyEvent.VK_W:
                        if (caller.d < 20) {
                            caller.d++;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        caller.x += caller.inc;
                        break;
                    case KeyEvent.VK_LEFT:
                        caller.x -= caller.inc;
                        break;
                    case KeyEvent.VK_DOWN:
                        caller.y += caller.inc;
                        break;
                    case KeyEvent.VK_UP:
                        caller.y -= caller.inc;
                        break;
                    default:
                        break;
                }
                StringBuilder sb = new StringBuilder();
                sb.append(this.caller.x);
                sb.append("|");
                sb.append(this.caller.y);
                sb.append("|");
                sb.append(this.caller.d);
                sb.append("|");
                sb.append(this.caller.inc);
                message = sb.toString();
                messageDispatcher(message);

                System.out.println(
                        this.cliente.getSocket().getInetAddress().toString()
                        + " - "
                        + message);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }
        encerrar();
    }
}
