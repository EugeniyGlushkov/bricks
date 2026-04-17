package ru.briks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.PartsDao;
import ru.briks.entity.Part;

/**
 * @author EGlushkov
 * Date: 06.04.2026
 * Time: 18:54
 */

@Repository
public class PartsService {
    @Autowired
    private PartsDao dao;

    @Transactional(readOnly = true)
    public Page<Part> getParts(int pageNum, int size) {
        return dao.findAll(pageNum, size);
    }
}
