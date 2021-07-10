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

public class Main {
    private static Document getPage() throws IOException {
        //String url = "https://www.simbirsoft.com/";
        String url = "https://materialcalc.herokuapp.com/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public static void main(String[] args) throws IOException {
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
        //System.out.println(getPage());

        String pagetext = getPage().text();
        String separatorsString = String.join("|\\", listOfSeparators);
        Map<String, Item> wordsMap = new HashMap<String, Item>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(pagetext.getBytes(StandardCharsets.UTF_8))));
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

        System.out.println("\n Парсинг HTML-страницы ");
        System.out.println("\n Статитстика по количеству уникальных слов: \n");
        for (Item item : wordsMap.values()) {
            System.out.println(item.word + " - " + item.count);
        }
    }

    public static class Item implements Comparable<Item> {
        String word;
        int count;

        @Override
        public int compareTo(Item b) { return b.count - count; }
    }
}
