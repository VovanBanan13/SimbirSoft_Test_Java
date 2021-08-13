package task;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

public class Parse {
    public Map<String, Item> parse(String url) throws IOException {
        try (FileWriter out_file = new FileWriter("statistic.txt", true)){
            String[] listOfSeparators = {" ", ",", "'", ".", "!", "?", "(", ")", "[", "]", ";", ":", "«", "»", "—", "/", "©", "%", "\"", "\n", "\r", "\t"};
            ScanURL scanURL = new ScanURL();
            String pageText = scanURL.getPage(url).text().toUpperCase();
            String separatorsString = String.join("|\\", listOfSeparators);
            Map<String, Item> wordsMap = new HashMap<>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pageText.getBytes(StandardCharsets.UTF_8))));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.replaceAll("[\\d]","");
                line = line.replaceAll("\\B-","");
                String[] words = line.split(separatorsString);
                for (String word : words) {
                    if ("".equals(word)) {
                        continue;
                    }

                    Item item = wordsMap.get(word);
                    if (item == null) {
                        item = new Item();
                        item.setWord(word);
                        item.setCount(0);
                        wordsMap.put(word, item);
                    }
                    item.addCount();
                }
            }
            reader.close();

            Date date = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            out_file.append(formatDate.format(date));
            out_file.append(" — парсинг: " + url);
            out_file.append(" — количество уникальных слов: " + wordsMap.size() + "\n");
            Logger.LOGGER.log(Level.INFO, "Конец работы парсера (остановка приложения) \n");
            return wordsMap;
        } catch (NullPointerException e)
        {
            System.out.println("\n Не получен URL-адрес, повторите попытку снова ");
            Logger.LOGGER.log(Level.WARNING,"Не получен URL-адрес (" + url + ") ");
        }
        return null;
    }

    public void sortByABC(Map map){
        System.out.println("\n Статистика по количеству уникальных слов (сортировка по алфавиту): \n");
        TreeMap<String, Item> sort = new TreeMap<>(map);
        for (Map.Entry<String, Item> entry : sort.entrySet())
            System.out.println(entry.getKey() + " - " + entry.getValue().getCount());
    }

    public void sortByCount(Map map){
        System.out.println("\n Статистика по количеству уникальных слов (сортировка по количеству): \n");
        List<Map.Entry<String, Item>> sorted = new LinkedList<Map.Entry<String, Item>>(map.entrySet());
        sorted.sort((a, b) -> (b.getValue().getCount() - a.getValue().getCount()));
        for ( Map.Entry<String, Item> sort : sorted )
            System.out.println(sort.getKey() + " - " + sort.getValue().getCount());
    }
}
