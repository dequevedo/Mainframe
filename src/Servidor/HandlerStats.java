package Servidor;

import com.sun.management.OperatingSystemMXBean;
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

    public double GetRAMUsage() {
        return osBean.getFreePhysicalMemorySize();
    }
}
