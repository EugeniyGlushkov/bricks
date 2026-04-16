package ru.briks.dao;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.briks.entity.Part;
import ru.briks.entity.QPart;

/**
 * @author EGlushkov
 * Date: 06.04.2026
 * Time: 18:44
 */

@Repository
public class PartsDao extends AbstractDao<Part,Long> {
    private static final QPart meta;

    static {
        meta = QPart.part;
    }

    public PartsDao(EntityManager em) {
        super(Part.class, em);
    }

    public Page<Part> findAll(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        return findAll(pageable);
    }
}
