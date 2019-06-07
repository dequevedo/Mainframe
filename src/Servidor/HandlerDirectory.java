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

    public String NavigateForward(String folderName) {

        File path = new File(actualPath);

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

    public String NavigateBack() {
        StringTokenizer st = new StringTokenizer(actualPath, "\\");
        StringBuilder sb = new StringBuilder();
        List<String> elements = new ArrayList<>();

        while (st.hasMoreTokens()) {
            elements.add(st.nextToken());
        }

        elements.remove(elements.size() - 1);

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

    public String MoveFile(String source, String destination) {
        String messageReturn = "";
        try {
            Path temp = Files.move(
                    Paths.get(source),
                    Paths.get(destination)
            );
            if (temp != null) {
                messageReturn = "File moved from '" + source + "' to '" + destination + "' successfully";
                System.out.println(messageReturn);
            } else {
                messageReturn = "Failed to move the file";
                System.out.println(messageReturn);
            }
        } catch (Exception e) {
            messageReturn = "Failed to move the file: " + e.getMessage();
            System.out.println(messageReturn);
        }
        return messageReturn;
    }

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

    private List<String> search(final File folder) {
        List<String> result = new ArrayList<>();
        folder.list();
        int i = 0;
        for (String nome : folder.list()) {
            result.add("[" + i++ + "] " + nome);
        }
        return result;
    }

    public String CopyFile(String source, String destination) {
        String messageReturn = "";
        try {
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(destination);

            int b;
            while ((b = fis.read()) != -1) {
                fos.write(b);
            }

            fis.close();
            fos.close();

            messageReturn = "File copied from '" + source + "' to '" + destination + "' successfully";
            System.out.println(messageReturn);
        } catch (Exception e) {
            messageReturn = "Failed to copy the file: " + e.getMessage();
            System.out.println(messageReturn);
        }
        return messageReturn;
    }

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
