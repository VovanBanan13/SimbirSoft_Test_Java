package task;

import java.io.FileInputStream;
import java.util.logging.LogManager;

public class Logger {
    static java.util.logging.Logger LOGGER;
    static {
        try (FileInputStream ins = new FileInputStream("src\\main\\java\\task\\log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = java.util.logging.Logger.getLogger(Main.class.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
