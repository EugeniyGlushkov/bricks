package ru.briks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.briks.dao.InventoryPartDao;
import ru.briks.dao.MinifigDao;
import ru.briks.dao.SetDao;
import ru.briks.entity.InventoryPart;
import ru.briks.entity.Minifig;
import ru.briks.entity.Set;
import ru.briks.utils.ImageUtils;

import java.io.IOException;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Slf4j
@Service
public class DownloadService {

    @Autowired
    private MinifigDao minifigDao;
    @Autowired
    private SetDao setDao;
    @Autowired
    private InventoryPartDao inventoryPartDao;

    @Transactional
    public boolean downloadMinifigImgBatch(int batchNum, String basePath, int totalPages) throws IOException {
        Page<Minifig> minifigPage = minifigDao.findAllWithoutImg(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d from %d".formatted(batchNum, totalPages));

        if (minifigPage.getSize() == 0) {
            return true;
        }

        for (Minifig minifig : minifigPage.getContent()) {
            String relFileName = ImageUtils.downloadImgAndWrightToDisk(basePath, minifig.getOuterImgUrl());

            if (StringUtils.hasText(relFileName)) {
                minifig.setImgPath(relFileName);
                minifigDao.save(minifig);
            }
        }

        return false;
    }

    @Transactional
    public boolean downloadSetImgBatch(int batchNum, String basePath, int totalPages) throws IOException {
        Page<Set> setsPage = setDao.findAllWithoutImg(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d from %d".formatted(batchNum, totalPages));

        if (setsPage.getSize() == 0) {
            return true;
        }

        for (Set set : setsPage.getContent()) {
            String relFileName = ImageUtils.downloadImgAndWrightToDisk(basePath + "\\sets", set.getOuterImgUrl());

            if (StringUtils.hasText(relFileName)) {
                set.setImgPath(relFileName);
                setDao.save(set);
            }
        }

        return false;
    }

    @Transactional
    public boolean downloadInventoryPartImgBatch(int batchNum, String basePath, int totalPages) throws IOException {
        Page<InventoryPart> setsPage = inventoryPartDao.findAllWithoutImg(PageRequest.of(batchNum, 10));
        log.info("Batch is: %d from %d".formatted(batchNum, totalPages));

        if (setsPage.getSize() == 0) {
            return true;
        }

        for (InventoryPart invPart : setsPage.getContent()) {
            String relFileName = ImageUtils.downloadImgAndWrightToDisk(basePath + "\\invParts", invPart.getOuterImgUrl());

            if (StringUtils.hasText(relFileName)) {
                invPart.setImgPath(relFileName);
                inventoryPartDao.save(invPart);
            }
        }

        return false;
    }
}
