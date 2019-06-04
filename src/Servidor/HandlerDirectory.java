package Servidor;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.*;

public class HandlerDirectory {

    public double cpu;
    public double ram;
    public double disk;
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public static void main(String[] args) {
        System.out.println("Eu sou o seu primeiro programa.");
        GetFiles();
        MoveFile();
    }

    public static void MoveFile() {
        try {
            Path temp = Files.move(Paths.get("C:\\Users\\danie\\Documents\\NetbeansProjects\\Mainframe\\testeeee.txt"),
                    Paths.get("C:\\Users\\danie\\Desktop\\445.txt"));

            if (temp != null) {
                System.out.println("File renamed and moved successfully");
            } else {
                System.out.println("Failed to move the file");
            }
        } catch (Exception e) {

        }
    }

    public static void GetFiles() {
        try {
            final File folder = new File("C:\\Users\\danie\\Documents\\NetbeansProjects\\Mainframe");

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

            if (f.isDirectory()) {
                search(pattern, f, result);
            }

            if (f.isFile()) {
                if (f.getName().matches(pattern)) {
                    result.add(f.getAbsolutePath());
                }
            }

        }
    }
}
