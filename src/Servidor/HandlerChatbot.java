package Servidor;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class HandlerChatbot {

    public String returnAnswer(String question) {
        StringTokenizer tok = new StringTokenizer(question, " ");
        List<String> elements = new ArrayList<>();
        
        while(tok.hasMoreElements()){
            elements.add(tok.nextToken());
        }
        
        for(String word: elements){
            System.out.println(word);
            if(word.equals("que")){
                System.out.println("que é");
                for(String word2: elements){
                    if(word2.equals("servidor") || word2.equals("server") || word2.equals("servidor?") || word2.equals("server?")){
                        System.out.println("o que é servidor");
                        return "Servidor pode ser definido como uma maquina, conectada a uma rede ; "
                                + "que prove serviços a clientes.";
                    }else if(word2.equals("brasil") || word2.equals("Brasil") || word2.equals("Brazil") 
                            || word2.equals("brasil?") || word2.equals("Brasil?") || word2.equals("Brazil?")){
                        System.out.println("o que é Brasil");
                        return "Brasil ou República Federativa do Brasil é um país localizado na america latina,;"
                                + " colonizado por Portugal, e sua lingua oficial é Português";
                    }
                }
            }
        }
        
        return "Não entendi a sua pergunta";
    }
}
