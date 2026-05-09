package ru.briks.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

/**
 * @author EGlushkov
 * Date: 06.05.2026
 * Time: 15:00
 */

@Slf4j
@UtilityClass
public class ImageUtils {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(3))
            .build();

    public static String downloadImgAndWrightToDisk(String basePath, String imgUrl) throws IOException {

        if (imgUrl == null) {
            return null;
        }

        String path = imgUrl.replace("https://", "").replace("http://", "");
        String[] imgUrlArr = path.split("/");
        StringBuilder relFileName = new StringBuilder();

        for (int j = 1; j < imgUrlArr.length; j++) {
            relFileName.append("\\")
                    .append(imgUrlArr[j]);
        }

        String fileName = basePath + relFileName;
        File file = new File(fileName);

        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        InputStream in = null;

        try {
            in = new URL(imgUrl).openStream();
            Files.copy(in, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            in.close();
        } catch (FileNotFoundException ex) {
            log.info("File Not Found: {}}", imgUrl, ex);
            if(in != null) {
                in.close();
            }
            return null;
        }

        return relFileName.toString();
    }

    /** Скачивает картинку по URL и уменьшает так, чтобы большая сторона ≤ maxSize */
    public static byte[] downloadAndResize(String url, int maxSize) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(5))
                    .GET()
                    .build();

            HttpResponse<byte[]> response = CLIENT.send(request, HttpResponse.BodyHandlers.ofByteArray());
            if (response.statusCode() != 200) return null;

            BufferedImage img = ImageIO.read(new ByteArrayInputStream(response.body()));
            if (img == null) return null;

            int w = img.getWidth(), h = img.getHeight();
            if (w <= maxSize && h <= maxSize) return response.body();

            double scale = (double) maxSize / Math.max(w, h);
            int newW = (int) (w * scale), newH = (int) (h * scale);

            BufferedImage resized = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = resized.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(img, 0, 0, newW, newH, null);
            g2.dispose();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resized, "jpg", baos);
            return baos.toByteArray();
        } catch (Exception e) {
            return null; // Тихо игнорируем, отчёт не должен падать из-за одной битой ссылки
        }
    }
}