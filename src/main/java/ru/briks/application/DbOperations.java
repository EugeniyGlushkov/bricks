package ru.briks.application;

import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.briks.dao.MinifigsDao;
import ru.briks.entity.Minifig;
import ru.briks.service.ImgService;
import ru.briks.service.MinifigService;

@SpringBootTest
//@ContextConfiguration
//@AutoConfigureMockMvc
//@WebAppConfiguration
@RunWith(SpringRunner.class)
public class DbOperations {
    @Autowired
    private MinifigService minifigService;
    @Autowired
    private ImgService imgService;
    @Autowired
    private MinifigsDao minifigsDao;

    @Value("${app.images.path:D:\\lego\\images}")
    private String basePath;

    @Test
    @SneakyThrows
    public void downloadImages()  {
        minifigService.parseImages(basePath);
    }

    @Test
    @SneakyThrows
    public void downloadImageById()  {
        Minifig minifig = minifigsDao.findById(12155L).get();
        imgService.downloadImg(basePath, minifig);
    }
}
