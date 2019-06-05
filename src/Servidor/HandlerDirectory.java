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

    public static void main(String[] args) {
        CreateFolder("");
        CreateFolder("Folder1");
        CreateFolder("Folder2");
        CreateFile("Folder1", "file.txt");
        ListDirectory("Folder1");
        ListDirectory("Folder2");
        MoveFile(serverPath + "\\Folder1\\file.txt", serverPath + "\\Folder2\\batata.txt");
        DeleteFile(serverPath + "\\Folder2\\file.txt");
    }

    public static void CreateFolder(String folderName) {
        try {
            new File(serverPath + "\\" + folderName).mkdirs();
        } catch (Exception e) {
        }
    }

    public static void CreateFile(String path, String fileName) {
        try {
            File file = new File(serverPath + "\\" + path + "\\" + fileName);
            file.createNewFile();
        } catch (Exception e) {
        }
    }

    public static void MoveFile(String source, String destination) {
        try {
            Path temp = Files.move(
                    Paths.get(source),
                    Paths.get(destination)
            );
            if (temp != null) {
                System.out.println("File moved from '" + source + "' to '" + destination + "' successfully");
            } else {
                System.out.println("Failed to move the file");
            }
        } catch (Exception e) {
        }
    }

    public static void ListDirectory(String folderName) {
        try {
            System.out.println("Listing folder: " + serverPath + folderName);
            final File folder = new File(serverPath + "\\" + folderName);

            List<String> result = new ArrayList<>();

            search(".*\\.java", folder, result);

            for (String s : result) {
                System.out.println(s);
            }
        } catch (Exception e) {

        }
    }

    public static void search(final String pattern, final File folder, List<String> result) {
        for (final File f : folder.listFiles()) {
            /*if (f.isDirectory()) {
             search(pattern, f, result);
             }*/
            if (f.isFile()) {
                result.add(f.getAbsolutePath());
            }
        }
    }

    public static void CopyFile(String source, String destination) {
        try {
            FileInputStream fis = new FileInputStream(source);
            FileOutputStream fos = new FileOutputStream(destination);

            int b;
            while ((b = fis.read()) != -1) {
                fos.write(b);
            }

            fis.close();
            fos.close();
        } catch (Exception e) {
        }
    }

    public static void DeleteFile(String fileName) {
        File file = new File(serverPath + "\\ServerMainframe\\" + fileName);
        file.delete();
    }
}
