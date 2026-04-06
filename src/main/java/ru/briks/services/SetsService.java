package ru.briks.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.briks.dao.SetsDao;

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
public class SetsService {

    @Autowired
    private DownloadService downloadService;
    @Autowired
    private SetsDao setsDao;

    public void parseImages(String basePath) {
        int totalPages = setsDao.findAllWithoutImg(PageRequest.of(0, 10)).getTotalPages();
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        for (int i = 0; i < totalPages; i++) {
            int finalI = i;
            futures.add(CompletableFuture.runAsync(() -> {
                                try {
                                    downloadService.downloadSetImgBatch(finalI, basePath, totalPages);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    )
            );
        }

        CompletableFuture<Void> finalFuture =  CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        finalFuture.join();
        System.out.println("Downloading sets images is finished");
    }
}
