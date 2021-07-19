package test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Second {
    public static void main(String[] args) throws IOException {
        System.out.println("\n Парсинг HTML-страницы ");
        // System.out.println("\n URL-адрес: " + scan_url());
        parse();
    }

    public static String scan_url() {
        System.out.println("\n Введите адрес HTML-страницы: ");
        Scanner scanner = new Scanner(System.in);
        String url_name = scanner.nextLine();
        return url_name;
    }

    public static Document getPage() throws IOException {
        //String url = "https://www.simbirsoft.com/";
        String url = scan_url();
        Document page = Jsoup.parse(new URL(url), 3000);
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

        String pageText = getPage().text();
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
    }

    public static class Item  {
        String word;
        int count;
    }
}
