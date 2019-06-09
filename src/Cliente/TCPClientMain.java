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
        this.socket = new Socket(serverAddress, serverPort); //cria o socket com o ip e porta passados como parametro pela interface
        this.receiver = new Receive(this.socket, caller); //cria a classe receive, que lidara com o recebimento das mensagens do servidor
        this.receiver.start(); //starta a thread do receiver
        this.output = new PrintWriter(this.socket.getOutputStream(), true); //inicializa o PrintWriter (output), responsável pelo envio da mensagem ao servidor

    }

    //metodo que escreve no printWriter e envia a mensagem para o servidor
    public void writeMessage(String outMessage) {
        this.output.println(outMessage);
        this.output.flush();
    }

    //encerra a conexão do socket
    public void closeConnection() throws IOException, Throwable {
        this.socket.close();
    }

    @Override
    protected void finalize() throws Throwable { //finaliza a conexão e após, ele mesmo
        try {
            this.closeConnection();
        } finally {
            super.finalize();
        }
    }

}
