package ru.briks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.MinifigsDao;
import ru.briks.entity.Minifig;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

@Slf4j
@Service
public class ImgService {

    @Autowired
    private MinifigsDao minifigsDao;

    @Transactional
    public boolean downloadBatch(int batchNum, String basePath) throws IOException {
        Page<Minifig> minifigPage = minifigsDao.findAll(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d".formatted(batchNum));

        if (minifigPage.getSize() == 0) {
            return true;
        }

        for (Minifig minifig : minifigPage.getContent()) {
            downloadImg(basePath, minifig);
        }

        return false;
    }

    @Transactional
    public void downloadImg(String basePath, Minifig minifig) throws IOException {
        String imgUrl = minifig.getOuterImgUrl();

        if (imgUrl == null) {
            return;
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
            return;
        }

        String format = imgUrlArr[imgUrlArr.length - 1].split("\\.")[1];
        ImageIO.write(img, format, file);
        minifig.setImgPath(relFileName.toString());
        minifigsDao.save(minifig);
    }
}
