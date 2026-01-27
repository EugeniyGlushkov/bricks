package ru.briks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.briks.dao.InventoryPartsDao;

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
public class InventoryPartService {

    @Autowired
    private ImgService imgService;
    @Autowired
    private InventoryPartsDao inventoryPartsDao;

    public void parseImages(String basePath) {
        int totalPages = inventoryPartsDao.findAllWithoutImg(PageRequest.of(0, 10)).getTotalPages();
        log.info("Total pages: {}", totalPages);
        List<CompletableFuture<Void>> futures = new ArrayList<>();

        //должно быть с 0, но т.к. затопорилось на 60170 поднял старт сканирования
        //for (int i = 0; i < totalPages ; i++) {
        for (int i = 0; i < 500 ; i++) {
            int finalI = i;
            futures.add(CompletableFuture.runAsync(() -> {
                                try {
                                    imgService.downloadInventoryPartImgBatch(finalI, basePath);
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
}
