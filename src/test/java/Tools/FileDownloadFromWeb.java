package Tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class FileDownloadFromWeb implements Runnable {

    Logger log = LoggerFactory.getLogger(FileDownloadFromWeb.class.getName());
    String link;
    File out;

    public FileDownloadFromWeb(String link, File out) {
        this.link = link;
        this.out = out;
    }

    public FileDownloadFromWeb(){}

    public void fileDownload() {
        URL url;
        HttpURLConnection http = null;
        double fileSize = 0.0;

        try {
            url = new URL(link);
            http = (HttpURLConnection) url.openConnection();
            fileSize = (double) http.getContentLengthLong();
        } catch (Exception e) {
            log.info("Произошла ошибка \"{}\"", e.getMessage());
        }

        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(http.getInputStream());
             FileOutputStream fileOutputStream = new FileOutputStream(this.out);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream, 1024)) {
            log.info("Скачивание файла началось");
            byte[] buffer = new byte[1024];
            double downloaded = 0.00;
            int read = 0;
            double percentDownloaded = 0.00;
            while ((read = bufferedInputStream.read(buffer, 0, 1024)) >= 0) {
                bufferedOutputStream.write(buffer, 0, read);
                downloaded += read;
                percentDownloaded = (downloaded * 100) / fileSize;
                String percent = String.format("%.2f", percentDownloaded);
                log.info("Скачено: " + percent + " %-та файла");
            }
            bufferedOutputStream.close();
            bufferedInputStream.close();
            log.info("Загрузка файла совершена");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getCurrentDateAndTime() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy_HH.mm.ss");
        Date today = Calendar.getInstance().getTime();
        String date = dateFormat.format(today);
        return date;
    }

    public String getFileAbsolutePath(String filePath) {
        File file = new File(filePath);
        return file.getAbsolutePath();
    }

    public void ifDirectoryExists(String pathFolder, String folderName) throws IOException {
        log.info("Приступаем к проверке директории \"{}\"", folderName);
        FileDownloadFromWeb fileDownloadFromWeb = new FileDownloadFromWeb();

        File directoryToCheckPath = new File(fileDownloadFromWeb.getFileAbsolutePath(pathFolder) +
                File.separator + folderName);
        Path path = Paths.get(String.valueOf(directoryToCheckPath));
        if (!Files.exists(path)) {
            Files.createDirectory(path);
            log.info("Директория \"{}\" создана!", folderName);
        } else {
            log.info("Директория \"{}\" уже существует!", folderName);
        }
    }

    public void ifFileExists(String filePath) {
        Path p = Paths.get(filePath);
        log.info("Проверяем - есть ли файл: \"{}\"?", p.getFileName());
        boolean exists = Files.exists(p);
        boolean notExists = Files.notExists(p);

        if (exists) {
            log.info("Файл: \"{}\" существует!", p.getFileName());
        } else if (notExists) {
            log.info("Файл: \"{}\" не существует!", p.getFileName());
        } else {
            log.info("Статус файла: \"{}\" не известен!", p.getFileName());
        }
    }

    @Override
    public void run() { fileDownload();}

}
