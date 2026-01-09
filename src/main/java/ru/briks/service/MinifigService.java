package ru.briks.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.briks.dao.MinifigsDao;
import ru.briks.entity.Minifig;

import java.io.IOException;

@Slf4j
@Service
public class MinifigService {

    @Autowired
    private MinifigsDao minifigsDao;
    @Autowired
    private ImgService imgService;

    public void parseImages(String basePath) throws IOException {
        for (int i = 0; ; i++) {
            boolean end = imgService.downloadMinifigImgBatch(i, basePath);

            if (end) {
                break;
            }
        }
    }

    public Minifig getMinifig(Long id) {
        return minifigsDao.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Minifig with id: [%s] is not existed.".formatted(id)));
    }
}
