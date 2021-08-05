package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.*;

public class ParseHTML {
    public static void main(String[] args) throws IOException {
        System.out.println("\n Парсинг HTML-страницы ");
        LOGGER.log(Level.INFO,"Начало работы парсера (запуск приложения)");
        parse(scan_url());
    }

    public static String scan_url() throws IOException {
        System.out.println("\n Введите адрес HTML-страницы: ");
        Scanner scanner = new Scanner(System.in);
        String url_name = scanner.nextLine();
        try {
            URL url = new URL(url_name);
            URLConnection conn = url.openConnection();
            conn.connect();
        } catch (MalformedURLException e) {
            System.out.println("\n Неверный адрес URL, будет парсинг шаблонной HTML-страницы ");
            System.out.println("\n  https://www.simbirsoft.com/");
            LOGGER.log(Level.WARNING,"Неверный адрес URL (" + url_name + ") ");
            url_name = "https://www.simbirsoft.com/";
        } catch (IOException e) {
            System.out.println("\n Не удалось установить соединение, будет парсинг шаблонной HTML-страницы ");
            System.out.println("\n  https://www.simbirsoft.com/");
            LOGGER.log(Level.WARNING,"Не удалось установить соединение (" + url_name + ") ");
            url_name = "https://www.simbirsoft.com/";
        } finally {
            LOGGER.log(Level.INFO,"Парсинг URL (" + url_name + ") ");
            return url_name;
        }
    }

    public static Document getPage(String url) throws IOException {
        // String url = "https://www.simbirsoft.com/";
        // url = scan_url();
        try {
            Document page = Jsoup.parse(new URL(url), 5000);
            return page;
        } catch (SocketTimeoutException e)
        {
            System.out.println("\n Вышло время ожидания запроса к URL-адресу, повторите попытку снова ");
            LOGGER.log(Level.WARNING,"Вышло время ожидания (" + url + ") ");
        }
        return null;
    }

    public static void parse(String url) throws IOException {
        try {
            ArrayList<String> listOfSeparators = new ArrayList<>();
            listOfSeparators.add(" ");
            listOfSeparators.add(",");
            listOfSeparators.add(".");
            listOfSeparators.add("!");
            listOfSeparators.add("?");
            listOfSeparators.add("(");
            listOfSeparators.add(")");
            listOfSeparators.add("[");
            listOfSeparators.add("]");
            listOfSeparators.add(";");
            listOfSeparators.add(":");
            listOfSeparators.add("«");
            listOfSeparators.add("»");
            listOfSeparators.add("—");
            listOfSeparators.add("/");
            listOfSeparators.add("\"");
            listOfSeparators.add("\n");
            listOfSeparators.add("\r");
            listOfSeparators.add("\t");

            String pageText = getPage(url).text().toUpperCase();
            String separatorsString = String.join("|\\", listOfSeparators);
            Map<String, Item> wordsMap = new HashMap<String, Item>();

            BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pageText.getBytes(StandardCharsets.UTF_8))));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.split(separatorsString);
                for (String word : words) {
                    if ("".equals(word)) {
                        continue;
                    }

                    Item item = wordsMap.get(word);
                    if (item == null) {
                        item = new Item();
                        item.word = word;
                        item.count = 0;
                        wordsMap.put(word, item);
                    }
                    item.count++;
                }
            }
            reader.close();

            sortByABC(wordsMap);
            sortByCount(wordsMap);

//            System.out.println("\n Статистика по количеству уникальных слов: \n");
//            for (Item item : wordsMap.values()) {
//                System.out.println(item.word + " - " + item.count);
//            }

            FileWriter out_file = new FileWriter("statistic.txt", true);
            Date date = new Date();
            SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            out_file.append(formatDate.format(date));
            out_file.append(" — парсинг: " + url);
            out_file.append(" — количество уникальных слов: " + wordsMap.size() + "\n");
            out_file.close();
            LOGGER.log(Level.INFO, "Конец работы парсера (остановка приложения) \n");
        } catch (NullPointerException e)
        {
            System.out.println("\n Не получен URL-адрес, повторите попытку снова ");
            LOGGER.log(Level.WARNING,"Не получен URL-адрес (" + url + ") ");
        }
    }

    public static void sortByABC(Map map){
        System.out.println("\n Статистика по количеству уникальных слов (сортировка по алфавиту): \n");
        TreeMap<String, Item> sort = new TreeMap<>();
        sort.putAll(map);
        for (Map.Entry<String, Item> entry : sort.entrySet())
            System.out.println(entry.getKey() + " - " + entry.getValue().count);
    }

    public static void sortByCount(Map map){
        System.out.println("\n Статистика по количеству уникальных слов (сортировка по количеству): \n");
        List<Map.Entry<String, Item>> sorted = new LinkedList<Map.Entry<String, Item>>(map.entrySet());
        Collections.sort(sorted, new Comparator<Map.Entry<String, Item>>() {
            public int compare(Map.Entry<String, Item> a, Map.Entry<String, Item> b) {
                return ( b.getValue().count - a.getValue().count );
            }
        });
        for ( Map.Entry<String, Item> sort : sorted )
            System.out.println(sort.getKey() + " - " + sort.getValue().count);
    }

    public static class Item  {
        String word;
        int count;
    }

    static Logger LOGGER;
    static {
        //try (FileInputStream ins = new FileInputStream("D:\\Project_Test_Java\\log.config")) {
        try (FileInputStream ins = new FileInputStream("src\\main\\java\\test\\log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(ParseHTML.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }
}
