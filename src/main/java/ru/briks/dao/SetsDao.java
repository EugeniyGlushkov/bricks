package ru.briks.dao;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.briks.entity.QSet;
import ru.briks.entity.Set;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

public class SetsDao extends AbstractDao<Set,Long> {
    private static final QSet meta;

    static {
        meta = QSet.set;
    }

    public SetsDao(EntityManager em) {
        super(Set.class, em);
    }

    public Page<Set> findAllWithoutImg(Pageable pageable) {
        return findAll(meta.imgPath.isNull(), pageable);
    }
}
