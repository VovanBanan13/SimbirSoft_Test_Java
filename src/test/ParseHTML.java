package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.logging.*;

public class ParseHTML {
    public static void main(String[] args) throws IOException {
        System.out.println("\n Парсинг HTML-страницы ");
        LOGGER.log(Level.INFO,"Начало работы парсера (запуск приложения)");
        // System.out.println("\n URL-адрес: " + scan_url());
        parse();
    }

    public static String scan_url() throws IOException {
        System.out.println("\n Введите адрес HTML-страницы: ");
        Scanner scanner = new Scanner(System.in);
        String url_name = scanner.nextLine();
        try {
            URL url = new URL(url_name);
            URLConnection conn = url.openConnection();
            ((URLConnection) conn).connect();
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
        //String url = "https://www.simbirsoft.com/";
        //url = scan_url();
        Document page = Jsoup.parse(new URL(url), 5000);
        return page;
    }

    public static void parse() throws IOException {
        ArrayList<String> listOfSeparators = new ArrayList<String>();
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
        listOfSeparators.add("\n");
        listOfSeparators.add("\r");
        listOfSeparators.add("\t");

        String pageText = getPage(scan_url()).text();
        String separatorsString = String.join("|\\", listOfSeparators);
        Map<String, Item> wordsMap = new HashMap<String, Item>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pageText.getBytes(StandardCharsets.UTF_8))));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] words = line.split(separatorsString);
            for (String word : words) {
                if ("".equals(word)){
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
        System.out.println("\n Статистика по количеству уникальных слов: \n");
        for (Item item : wordsMap.values()) {
            System.out.println(item.word + " - " + item.count);
        }
        LOGGER.log(Level.INFO,"Конец работы парсера (остановка приложения)");
    }

    public static class Item  {
        String word;
        int count;
    }

    static Logger LOGGER;
    static {
        //try (FileInputStream ins = new FileInputStream("D:\\Project_Test_Java\\log.config")) {
        try (FileInputStream ins = new FileInputStream("src\\test\\log.config")) {
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(Main.class.getName());
        } catch (Exception ignore) {
            ignore.printStackTrace();
        }
    }
}
