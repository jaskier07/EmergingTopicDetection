package pl.kania.etd.debug;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryService {

    public static void printCurrentFreeMemory() {
        log.info("=========> Free memory: " + NumberFormatter.formatPercentage(Runtime.getRuntime().freeMemory(), Runtime.getRuntime().totalMemory()));
    }

    public static void saveAndPrintCurrentFreeMemory() {
        System.gc();
        printCurrentFreeMemory();
    }
}
