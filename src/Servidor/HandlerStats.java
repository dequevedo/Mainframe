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
        File cDrive = new File("C:");
        System.out.println(String.format("Total space: %.2f GB",
                (double) cDrive.getTotalSpace() / 1073741824));
        System.out.println(String.format("Free space: %.2f GB",
                (double) cDrive.getFreeSpace() / 1073741824));
        System.out.println(String.format("Usable space: %.2f GB",
                (double) cDrive.getUsableSpace() / 1073741824));

        return osBean.getProcessCpuLoad();
    }

    public double GetRAMUsage() {
        return (osBean.getTotalPhysicalMemorySize());
    }

    public double GetDiskUsage() {
        return 0;
    }
}
