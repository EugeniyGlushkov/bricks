package ru.briks.application;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.briks.dao.InventoryPartDao;
import ru.briks.dto.VariantDto;
import ru.briks.entity.InventoryPart;
import ru.briks.service.InventoryPartService;
import ru.briks.service.MinifigService;
import ru.briks.service.SetService;
import ru.briks.service.price.ElementInfoPriceService;
import ru.briks.settings.VariantsSettings;
import ru.briks.utils.ImageUtils;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class DbOperations {
    @Autowired
    private MinifigService minifigService;
    @Autowired
    private SetService setService;
    @Autowired
    private InventoryPartService inventoryPartService;
    @Autowired
    private InventoryPartDao inventoryPartDao;
    @Autowired
    private ElementInfoPriceService elementInfoPriceService;
    @Autowired
    private VariantsSettings variants;

    @Value("${app.images.path:D:\\lego\\images}")
    private String basePath;

    @Test
    @SneakyThrows
    public void downloadMinifigImages() {
        minifigService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadSetsImages() {
        setService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadInvPartsImages() {
        inventoryPartService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadImageById() {
        InventoryPart invP = inventoryPartDao.findById(99108L).get();
        ImageUtils.downloadImgAndWrightToDisk(basePath, invP.getOuterImgUrl());
    }

    //move to other service
    @Test
    @SneakyThrows
    public void downloadPrices() {
        for (Map.Entry<String, List<VariantDto>> entry : variants.getVariants().entrySet()) {
            List<VariantDto> variants = entry.getValue();

            for (VariantDto variant : variants) {
                elementInfoPriceService.downloadPrices(variant);
                TimeUnit.SECONDS.sleep(new Random().nextInt(3) + 1);
            }
        }
    }
}