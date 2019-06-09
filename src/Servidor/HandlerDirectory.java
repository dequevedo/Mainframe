package Servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.*;
import java.util.StringTokenizer;

public class HandlerDirectory {

    public static String serverPath = System.getProperty("user.home") + "\\Desktop" + "\\ServerMainframe";
    public static String actualPath = serverPath;

    //Método para navegar para outro diretório
    public String NavigateForward(String folderName) {

        File path = new File(actualPath);

        //Procura por uma pasta que tenha o nome igual o parametro recebido,
        //Caso não encontrar, uma mensagem de erro será retornada
        int i = 0;
        for (String nome : path.list()) {
            if (folderName.equals(nome)) {
                File file = new File(actualPath + "\\" + folderName);
                if (file.isDirectory()) {
                    actualPath = actualPath + "\\" + folderName;
                    return "Successfully navigated to folder: " + actualPath;
                }
            }
            i++;
        }

        return "Directory " + folderName + " not found";
    }

    //Navegar para o diretório anterior
    public String NavigateBack() {
        //Subdivide o endereço atual pelo token "\\"
        StringTokenizer st = new StringTokenizer(actualPath, "\\");
        StringBuilder sb = new StringBuilder();
        List<String> elements = new ArrayList<>();

        //Armazena cada elemento obtido pelo tokenizer em uma lista
        while (st.hasMoreTokens()) {
            elements.add(st.nextToken());
        }

        //Remove apenas o último elemento da lista
        //Dessa forma obtém-se o endereço da pasta anterior.
        //Porem ainda falta colocar "\\" novamente
        elements.remove(elements.size() - 1);

        //Percorre cada elemento da lista, e adiciona "\\" entre eles
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i));
            if (i != elements.size() - 1) {
                sb.append("\\");
            }
        }

        String targetPath = sb.toString();

        if (actualPath.equals(serverPath)) {
            return "Already at root path: " + actualPath;
        } else {
            actualPath = targetPath;
            return "Successfully returned to folder: " + actualPath;
        }
    }

    //Método para mostrar ao usuário informações gerais
    public String FolderStatus() {
        CreateFolder("");
        StringBuilder sb = new StringBuilder();
        sb.append("Directory:");
        sb.append(actualPath);
        sb.append("\t");
        sb.append(";Content:;");
        sb.append(";" + ListDirectory());
        sb.append("; Available commands: \t");
        sb.append("\t Create");
        sb.append("\t Move");
        sb.append("\t Copy");
        sb.append("\t Delete");
        System.out.println(sb.toString());
        return sb.toString();
    }

    //Método para criar uma nova pasta
    public String CreateFolder(String folderName) {
        String messageReturn = "";
        try {
            new File(actualPath + "\\" + folderName).mkdirs();
            messageReturn = "Folder '" + folderName + "' Created";
        } catch (Exception e) {
            messageReturn = "Failed to create the folder: " + e.getMessage();
        }
        return messageReturn;
    }

    //Método para criar um novo arquivo
    public String CreateFile(String fileName) {
        String messageReturn = "";
        try {

            File file = new File(actualPath + "\\" + fileName);
            file.createNewFile();
            messageReturn = "File Created: " + file.getAbsolutePath();
        } catch (Exception e) {
            messageReturn = "Failed to create the file: " + e.getMessage();
        }
        return messageReturn;
    }

    //Método para mover um arquivo, através de um Id recebido
    public String MoveFile(String fileId, String destination) {
        String messageReturn = "";
        String originPath = "";
        String destinationPath = serverPath + destination;

        //Procura o arquivo que tenha um Id igual ao fileId recebido
        File path = new File(actualPath);
        int i = 0;
        for (String nome : path.list()) {
            if (i == Integer.parseInt(fileId)) {
                originPath = (actualPath + "\\" + nome);
            }
            i++;
        }

        try {
            Path temp = Files.move(
                    Paths.get(originPath),
                    Paths.get(destinationPath)
            );
            if (temp != null) {
                messageReturn = "File moved from '" + originPath + "' to '" + destinationPath + "' successfully";
                System.out.println(messageReturn);
            } else {
                messageReturn = "Failed to move the file";
                System.out.println(messageReturn);
            }
        } catch (Exception e) {
            messageReturn = "Failed to move the file: " + e.getMessage() + " Remember to use this destination format: \\Folder1\\Folder2\\fileName.txt";
            System.out.println(messageReturn);
        }
        return messageReturn;
    }

    //Método para copiar um arquivo
    public String CopyFile(String fileId, String destination) {
        String messageReturn = "";
        String originPath = "";
        String destinationPath = serverPath + destination;

        File path = new File(actualPath);

        //Procura o arquivo que tenha um Id igual ao fileId recebido
        int i = 0;
        for (String nome : path.list()) {
            if (i == Integer.parseInt(fileId)) {
                originPath = (actualPath + "\\" + nome);
            }
            i++;
        }

        try {

            FileInputStream fis = new FileInputStream(originPath);
            FileOutputStream fos = new FileOutputStream(destinationPath);

            int b;
            while ((b = fis.read()) != -1) {
                fos.write(b);
            }

            fis.close();
            fos.close();
            messageReturn = "File copied from '" + originPath + "' to '" + destinationPath + "' successfully";
        } catch (Exception e) {
            messageReturn = "Failed to copy the file: " + e.getMessage() + " Remember to use this destination format: \\Folder1\\Folder2\\fileName.txt";
        }

        return messageReturn;
    }

    //Lista todos os arquivos e pastas no diretório atual (actualPath)
    public String ListDirectory() {
        List<String> result = new ArrayList<>();
        try {
            System.out.println("Listing folder: " + actualPath);
            final File folder = new File(actualPath);

            result = search(folder);

            StringBuilder sb = new StringBuilder();

            for (String s : result) {
                sb.append("   " + s);
                sb.append(";");
            }
            return sb.toString();
        } catch (Exception e) {

        }
        return "Erro ao listar";
    }

    //Encontra e obtem o Id e nome de pastas e arquivos na pasta recebida
    private List<String> search(final File folder) {
        List<String> result = new ArrayList<>();
        folder.list();
        int i = 0;
        for (String nome : folder.list()) {
            result.add("[" + i++ + "] " + nome);
        }
        return result;
    }

    //Deleta um determinado arquivo, com base em um Id recebido
    public String DeleteFile(int id) {
        File path = new File(actualPath);

        int i = 0;
        for (String nome : path.list()) {
            if (i == id) {
                File file = new File(actualPath + "\\" + nome);
                file.delete();
                return "File '" + nome + "' deleted successfully";
            }
            i++;
        }

        return "Failed to delete the file with id[" + id + "]";
    }

    //Deleta um determinado arquivo, com base em um nome recebido
    public String DeleteFile(String fileName) {
        try {
            File file = new File(actualPath + "\\" + fileName);
            file.delete();
            return "File '" + fileName + "' deleted successfully";
        } catch (Exception e) {
            return "Failed to delete the file " + fileName;
        }
    }
}
