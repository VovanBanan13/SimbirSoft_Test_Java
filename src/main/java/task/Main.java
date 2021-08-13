package task;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("\n Парсинг HTML-страницы ");
        Logger.LOGGER.log(Level.INFO,"Начало работы парсера (запуск приложения)");
        Parse parse = new Parse();
        ScanURL scanURL = new ScanURL();
        Map<String, Item> words;
        words = parse.parse(scanURL.scan_url());

        System.out.println("Сортировать по: \n 1 - в алфавитном порядке \n 2 - по количеству слов ");
        System.out.println("\n Выберите сортировку: ");
        int c = System.in.read();
        if (c == '1')
            parse.sortByABC(words);
        else if (c == '2')
            parse.sortByCount(words);
        else {
            System.out.println("Неправильный ввод, сортировка не выбрана");
            System.out.println("\n Статистика по количеству уникальных слов: \n");
            for (Item item : words.values()) {
                System.out.println(item.getWord() + " - " + item.getCount());
            }
        }
    }
}
