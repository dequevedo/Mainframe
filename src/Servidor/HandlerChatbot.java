package Servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HandlerChatbot {

    public String returnAnswer(String question) {
        StringTokenizer tok = new StringTokenizer(question, " ");
        List<String> elements = new ArrayList<>();
        
        while(tok.hasMoreElements()){
            elements.add(tok.nextToken()); //separa as palavras da pergunta por espaços e coloca cada palavra na lista "elements"
        }
        
        for(String word: elements){
            System.out.println(word);
            if(word.equals("que")){ //se a mensagem conter "que"
                for(String word2: elements){
                    if(word2.equals("servidor") || word2.equals("server") || word2.equals("servidor?") || word2.equals("server?")){ //se conter server, servidor, server? ou servidor?, assume que a pergunta foi "oque é um servidor"
                        System.out.println("o que é um servidor");
                        return "Servidor pode ser definido como uma maquina, conectada a uma rede ; " //retorna a resposta para a pergunta "o que é servidor?"
                                + "que prove serviços a clientes.";
                    }else if(word2.equals("brasil") || word2.equals("Brasil") || word2.equals("Brazil")  //se conter brasil, Brasil, Brazil, brasil?, Brasil?, Brazil? assume que a pergunta foi "o que é brasil?"
                            || word2.equals("brasil?") || word2.equals("Brasil?") || word2.equals("Brazil?")){
                        System.out.println("o que é Brasil");
                        return "Brasil ou República Federativa do Brasil é um país localizado na america latina,;" //retorna a resposta para a pergunta "o que é brasil?"
                                + " colonizado por Portugal, e sua lingua oficial é Português";
                    }
                }
            }
        }
        
        return "Não entendi a sua pergunta"; //se não reconhecer nenhuma das possibilidades, retorna uma resposta padrão de que não entendeu a pergunta
    }
}
