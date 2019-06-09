package Servidor;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;

public class HandlerStats {

    public double cpu;
    public double ram;
    public double disk;
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class); //biblioteca que lê o status do computador

    public HandlerStats() {
    }

    public double GetCPUUsage() { //retorna o uso de CPU
        return osBean.getProcessCpuLoad();
    }

    public String getStatus() {
        File cDrive = new File("C:");
        String messageReturn = "";
        StringBuilder sb = new StringBuilder();

        //CPU USAGE
        sb.append("CPU USAGE;");
        sb.append(GetCPUUsage());
        sb.append(";");
        sb.append(";");

        //RAM USAGE
        sb.append("RAM USAGE;");
        sb.append(GetRAMUsage());
        sb.append(";");
        sb.append(";");

        //STORAGE USAGE
        sb.append("STORAGE USAGE;");
        sb.append(String.format("Total space: %.2f GB",
                (double) cDrive.getTotalSpace() / 1073741824));
        sb.append(";");
        sb.append(String.format("Total space: %.2f GB",
                (double) cDrive.getTotalSpace() / 1073741824));
        sb.append(";");
        sb.append(String.format("Free space: %.2f GB",
                (double) cDrive.getFreeSpace() / 1073741824));
        sb.append(";");
        sb.append(String.format("Usable space: %.2f GB",
                (double) cDrive.getUsableSpace() / 1073741824));
        sb.append(";");

        messageReturn = sb.toString();

        return messageReturn;
    }

    public double GetRAMUsage() { //retorna o uso de memória
        return (osBean.getTotalPhysicalMemorySize());
    }
}
