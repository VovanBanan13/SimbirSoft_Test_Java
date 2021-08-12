package task;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;
import java.util.logging.Level;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ScanURL {
    public String scan_url() {
        System.out.println("\n Введите адрес HTML-страницы: ");
        Scanner scanner = new Scanner(System.in);
        String url_name = scanner.nextLine();
        try {
            URL url = new URL(url_name);
            URLConnection conn = url.openConnection();
            conn.connect();
            Main.LOGGER.log(Level.INFO, "Парсинг URL (" + url_name + ") ");
            return url_name;
        } catch (MalformedURLException e) {
            System.out.println("\n Неверный адрес URL ");
            Main.LOGGER.log(Level.WARNING, "Неверный адрес URL (" + url_name + ") ");
        } catch (IOException e) {
            System.out.println("\n Не удалось установить соединение ");
            Main.LOGGER.log(Level.WARNING, "Не удалось установить соединение (" + url_name + ") ");
        }
        return scan_url();
    }

    public Document getPage(String url) throws IOException {
        try {
            return Jsoup.parse(new URL(url), 5000);
        } catch (SocketTimeoutException e)
        {
            System.out.println("\n Вышло время ожидания запроса к URL-адресу, производится повторный запрос ... ");
            Main.LOGGER.log(Level.WARNING,"Вышло время ожидания (" + url + ") ");
        }
        return getPage(url);
    }
}
