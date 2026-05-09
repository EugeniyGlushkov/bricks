package ru.briks.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.briks.dao.PartCategoryDao;
import ru.briks.entity.PartCategory;

import java.util.List;

/**
 * @author EGlushkov
 * Date: 08.05.2026
 * Time: 0:19
 */

@Slf4j
@Service
public class PartCategoryService {
    @Autowired
    private PartCategoryDao partCategoryDao;

    public List<PartCategory> getAll() {
        return partCategoryDao.findAll();
    }
}