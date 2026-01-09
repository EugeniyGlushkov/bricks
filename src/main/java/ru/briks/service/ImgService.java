package ru.briks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.briks.dao.InventoryPartsDao;
import ru.briks.dao.MinifigsDao;
import ru.briks.dao.SetsDao;
import ru.briks.entity.InventoryPart;
import ru.briks.entity.Minifig;
import ru.briks.entity.Set;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

@Slf4j
@Service
public class ImgService {

    @Autowired
    private MinifigsDao minifigsDao;
    @Autowired
    private SetsDao setsDao;
    @Autowired
    private InventoryPartsDao inventoryPartsDao;

    @Transactional
    public boolean downloadMinifigImgBatch(int batchNum, String basePath) throws IOException {
        Page<Minifig> minifigPage = minifigsDao.findAll(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d".formatted(batchNum));

        if (minifigPage.getSize() == 0) {
            return true;
        }

        for (Minifig minifig : minifigPage.getContent()) {
            String relFileName = downloadImg(basePath, minifig.getOuterImgUrl());

            if (!relFileName.isBlank()) {
                minifig.setImgPath(relFileName);
                minifigsDao.save(minifig);
            }
        }

        return false;
    }

    @Transactional
    public boolean downloadSetImgBatch(int batchNum, String basePath) throws IOException {
        Page<Set> setsPage = setsDao.findAll(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d".formatted(batchNum));

        if (setsPage.getSize() == 0) {
            return true;
        }

        for (Set set : setsPage.getContent()) {
            String relFileName = downloadImg(basePath + "\\sets", set.getOuterImgUrl());

            if (StringUtils.hasText(relFileName)) {
                set.setImgPath(relFileName);
                setsDao.save(set);
            }
        }

        return false;
    }

    @Transactional
    public boolean downloadInventoryPartImgBatch(int batchNum, String basePath) throws IOException {
        Page<InventoryPart> setsPage = inventoryPartsDao.findAll(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d".formatted(batchNum));

        if (setsPage.getSize() == 0) {
            return true;
        }

        for (InventoryPart invPart : setsPage.getContent()) {
            String relFileName = downloadImg(basePath + "\\invParts", invPart.getOuterImgUrl());

            if (StringUtils.hasText(relFileName)) {
                invPart.setImgPath(relFileName);
                inventoryPartsDao.save(invPart);
            }
        }

        return false;
    }

    @Transactional
    public String downloadImg1(String basePath, String imgUrl) throws IOException {

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

        if (file.exists()) {
            file.delete();
        }

        file.createNewFile();
        BufferedImage img;

        try {
            img = ImageIO.read(new URL(imgUrl));
        } catch (IIOException ex) {
            log.error("File Not Found: %s".formatted(imgUrl), ex);
            file.delete();
            return null;
        }

        if (img == null) {
            return null;
        }

        if (imgUrl.contains("/03-1")) {
            System.out.println("its");
        }

        String format = imgUrlArr[imgUrlArr.length - 1].split("\\.")[1];
        ImageIO.write(img, format, file);
        return relFileName.toString();
    }

    @Transactional
    public String downloadImg(String basePath, String imgUrl) throws IOException {

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
            log.error("File Not Found: %s".formatted(imgUrl), ex);
            if(in != null) {
                in.close();
            }
            return null;
        }

        return relFileName.toString();
    }
}
