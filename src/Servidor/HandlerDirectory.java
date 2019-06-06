package Servidor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.*;

public class HandlerDirectory {

    public static String serverPath = System.getProperty("user.home") + "\\Desktop" + "\\ServerMainframe";

//    public static void main(String[] args) {;
//        CreateFolder("");
//        CreateFolder("Folder1");
//        CreateFolder("Folder2");
//        CreateFile("Folder1", "file.txt");
//        ListDirectory("Folder1");
//        ListDirectory("Folder2");
//        MoveFile(serverPath + "\\Folder1\\file.txt", serverPath + "\\Folder2\\batata.txt");
//        DeleteFile(serverPath + "\\Folder2\\file.txt");
//    }
    public String getStatus() {
        CreateFolder("");
        StringBuilder sb = new StringBuilder();
        sb.append("Directory: ");
        sb.append(serverPath);
        sb.append("\t");
        sb.append("Contains Files:");
        List<String> fileList = new ArrayList<>();
        final File folder = new File(serverPath);
        System.out.println("folder "+folder.getName());
        
        fileList = search(folder);
        System.out.println(sb.toString());
        for(String fileName: fileList){
            sb.append("\t");
            sb.append(fileName);
        }
//        sb.append("you can: \t");
//        sb.append("\t Create");
//        sb.append("\t Move");
//        sb.append("\t Copy");
//        sb.append("\t Delete");
        System.out.println(sb.toString());
        return sb.toString();
    }

    public String CreateFolder(String folderName) {
        String messageReturn = "";
        try {
            new File(serverPath + "\\" + folderName).mkdirs();
            messageReturn = "Folder '" + folderName + "' Created";
        } catch (Exception e) {
            messageReturn = "Failed to create the folder: " + e.getMessage();
        }
        return messageReturn;
    }

    public String CreateFile(String path, String fileName) {
        String messageReturn = "";
        try {

            File file = new File(serverPath + "\\" + path + "\\" + fileName);
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

    public List<String> ListDirectory(String folderName) {
        List<String> result = new ArrayList<>();
        try {
            System.out.println("Listing folder: " + serverPath + "\\" + folderName);
            final File folder = new File(serverPath + "\\" + folderName);

            result = search(folder);

            for (String s : result) {
                System.out.println(s);
            }
            return result;
        } catch (Exception e) {

        }
        return result;
    }

    public List<String> search(final File folder) {
        List<String> result = new ArrayList<>();
        for (final File f : folder.listFiles()) {
            if (f.isFile()) {
                result.add(f.getName());
            }
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

    public String DeleteFile(String fileName) {
        String messageReturn = "";
        try {
            File file = new File(serverPath + "\\ServerMainframe\\" + fileName);
            file.delete();
            messageReturn = "File '" + fileName + "' deleted successfully";
            System.out.println(messageReturn);
        } catch (Exception e) {
            messageReturn = "Failed to delete the file: " + e.getMessage();
            System.out.println(messageReturn);
        }
        return messageReturn;
    }
}
