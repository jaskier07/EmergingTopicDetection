package pl.kania.etd.io;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@Slf4j
public class IntReader {

    public int read() {
        Integer value = null;
        log.info("Provide int");

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            do {
                String line = br.readLine();
                try {
                    value = Integer.parseInt(line);
                } catch (NumberFormatException nfe) {
                    log.info("Provide int");
                }
                Thread.sleep(1000);
            } while (value == null);

        } catch (Exception e) {
            log.error("Error reading int", e);
        }

        return value;
    }
}
