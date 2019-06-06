package Servidor;

import com.sun.management.OperatingSystemMXBean;
import java.io.File;
import java.lang.management.ManagementFactory;

public class HandlerStats {

    public double cpu;
    public double ram;
    public double disk;
    OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    public HandlerStats() {
    }

    public double GetCPUUsage() {
        return osBean.getProcessCpuLoad();
    }

    public String getStatus() {
        File cDrive = new File("C:");
        String messageReturn = "";
        StringBuilder sb = new StringBuilder();

        //CPU USAGE
        sb.append("CPU Usage: ");
        sb.append(GetCPUUsage());
        sb.append("\t");

        //RAM USAGE
        sb.append("RAM Usage: ");
        sb.append(GetRAMUsage());
        sb.append("\t");

        //STORAGE USAGE
        sb.append(String.format("Total space: %.2f GB",
                (double) cDrive.getTotalSpace() / 1073741824));
        sb.append("\t");
        sb.append(String.format("Total space: %.2f GB",
                (double) cDrive.getTotalSpace() / 1073741824));
        sb.append("\t");
        sb.append(String.format("Free space: %.2f GB",
                (double) cDrive.getFreeSpace() / 1073741824));
        sb.append("\t");
        sb.append(String.format("Usable space: %.2f GB",
                (double) cDrive.getUsableSpace() / 1073741824));
        sb.append("\t");

        messageReturn = sb.toString();

        return messageReturn;
    }

    public double GetRAMUsage() {
        return (osBean.getTotalPhysicalMemorySize());
    }

    public double GetDiskUsage() {
        return 0;
    }
}
