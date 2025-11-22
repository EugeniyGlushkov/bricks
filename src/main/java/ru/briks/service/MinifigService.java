package ru.briks.service;

import jakarta.persistence.EntityNotFoundException;
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
public class MinifigService {

    @Autowired
    private MinifigsDao minifigsDao;
    @Autowired
    private ImgService imgService;

    public void parseImages(String basePath) throws IOException {
        for (int i = 0; ; i++) {
            boolean end = imgService.downloadBatch(i, basePath);
        }
    }

    public Minifig getMinifig(Long id) {
        return minifigsDao.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Minifig with id: [%s] is not existed.".formatted(id)));
    }
}
