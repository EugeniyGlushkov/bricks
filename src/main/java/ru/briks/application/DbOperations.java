package ru.briks.application;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.briks.dao.InventoryPartDao;
import ru.briks.entity.InventoryPart;
import ru.briks.service.DownloadService;
import ru.briks.service.InventoryPartService;
import ru.briks.service.MinifigService;
import ru.briks.service.SetService;
import ru.briks.service.price.ElementInfoPriceService;
import ru.briks.utils.ImageUtils;

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
    private DownloadService downloadService;
    @Autowired
    private InventoryPartDao inventoryPartDao;
    @Autowired
    private ElementInfoPriceService elementInfoPriceService;
    @Autowired
    private ImageUtils imageUtils;

    @Value("${app.images.path:D:\\lego\\images}")
    private String basePath;

    @Test
    @SneakyThrows
    public void downloadMinifigImages()  {
        minifigService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadSetsImages()  {
        setService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadInvPartsImages()  {
        inventoryPartService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadImageById()  {
        InventoryPart invP = inventoryPartDao.findById(99108L).get();
        ImageUtils.downloadImgAndWrightToDisk(basePath, invP.getOuterImgUrl());
    }

    //move to other service
    @Test
    @SneakyThrows
    public void downloadPrices()  {
        //TODO uncomment after downloadPrices fix
        //elementInfoPriceService.downloadPrices();
        System.out.println();
    }
}