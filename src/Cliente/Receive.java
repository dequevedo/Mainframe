package Cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Receive extends Thread {

    public Socket socket;
    public BufferedReader input;
    private String dataRead = "";
    private ClienteInterface cInterface;

    
    public Receive(Socket socket, ClienteInterface cInterface) throws IOException {
        this.cInterface = cInterface; //necessita de acesso a classe da interface para setar o textArea com a resposta
        this.socket = socket; //tem que receber o socket para poder criar o BufferedReader (leitura das mensagens)
        this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream())); //cria o bufferedReader para leitura das mensagens
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.dataRead = this.input.readLine(); //comando bloqueante que fica aguardando recebimento de mensagem e faz a leitura dela
                if (this.dataRead.equals("")) { //se a mensagem for vazia, diz que esta vazia e não faz mais nada
                    System.out.println("mensagem vazia");
                } else { //se a mensagem conter algo
                    dataRead = dataRead.replace(";", "\n"); //substitui ; por \n para quebra de linha (necessário pois, se enviar \n no socket, havera uma mensagem por quebra de linha)
                    this.cInterface.setjText(this.dataRead); //acessa o metodo setjText da classe interface para setar o textArea com a mensagem do servidor
                    this.dataRead = ""; //limpa a variavel utilizada para armazenar a mensagem com ; trocado por \n
                }
            } catch (IOException ex) {
                System.out.println("Erro ao ler a mensagem: " + ex.getMessage()); //mensagem de erro ao ler a msg
            }
        }
    }
}
