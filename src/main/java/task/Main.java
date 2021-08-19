package task;

import java.io.IOException;
import java.util.List;
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

        System.out.println("\n Статистика по количеству уникальных слов \n");
        List list1 = parse.createSortByABC(words);
        List list2 = parse.createSortByCount(words);
        System.out.printf("\n %-40s |     %-40s", "   сортировка по алфавиту" , "   сортировка по количеству");
        System.out.printf("\n %-40s |     %-40s", " " , " ");
        for (int i = 0; i < list1.size(); i++)
            System.out.printf("\n %-40s |     %-40s", list1.get(i), list2.get(i));
    }
}
