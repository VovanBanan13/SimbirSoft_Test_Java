package task;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("\n Парсинг HTML-страницы ");
        LOGGER.log(Level.INFO,"Начало работы парсера (запуск приложения)");
        Parse parse = new Parse();
        ScanURL scanURL = new ScanURL();
        parse.parse(scanURL.scan_url());
    }

    static Logger LOGGER;
    static {
        try (FileInputStream ins = new FileInputStream("src\\main\\java\\task\\log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Main.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
