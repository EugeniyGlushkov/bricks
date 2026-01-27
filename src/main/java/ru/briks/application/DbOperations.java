package ru.briks.application;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.briks.dao.InventoryPartsDao;
import ru.briks.dao.MinifigsDao;
import ru.briks.entity.InventoryPart;
import ru.briks.service.ImgService;
import ru.briks.service.InventoryPartService;
import ru.briks.service.MinifigService;
import ru.briks.service.SetsService;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@SpringBootTest
//@ContextConfiguration
//@AutoConfigureMockMvc
//@WebAppConfiguration
@RunWith(SpringRunner.class)
public class DbOperations {
    @Autowired
    private MinifigService minifigService;
    @Autowired
    private SetsService setsService;
    @Autowired
    private InventoryPartService inventoryPartService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private MinifigsDao minifigsDao;
    @Autowired
    private InventoryPartsDao inventoryPartsDao;

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
        setsService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadInvPartsImages()  {
        inventoryPartService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadImageById()  {
        InventoryPart invP = inventoryPartsDao.findById(99108L).get();
        imgService.downloadImg(basePath, invP.getOuterImgUrl());
    }
}
