package ru.briks.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.briks.dao.MinifigsDao;
import ru.briks.entity.Minifig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Slf4j
@Service
public class MinifigService {

    @Autowired
    private MinifigsDao minifigsDao;
    @Autowired
    private DownloadService downloadService;

    public void parseImages(String basePath) throws IOException {
        int totalPages = minifigsDao.findAllWithoutImg(PageRequest.of(0, 10)).getTotalPages();
        log.info("Total pages: {}", totalPages);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < totalPages; i++) {
            int finalI = i;
            futures.add(CompletableFuture.runAsync(() -> {
                                try {
                                    downloadService.downloadMinifigImgBatch(finalI, basePath, totalPages);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
            );
        }

        CompletableFuture<Void> finalFuture =  CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        finalFuture.join();
        log.info("Downloading sets images is finished");
    }

    public Minifig getOne(Long id) {
        return minifigsDao.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Minifig with id: [%s] is not existed.".formatted(id)));
    }
}
