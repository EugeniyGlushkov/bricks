package ru.briks.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.briks.entity.QSet;
import ru.briks.entity.Set;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Repository
public class SetsDao extends AbstractDao<Set,Long> {
    private static final QSet meta;

    static {
        meta = QSet.set;
    }

    public SetsDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(Set.class, em, queryFactory);
    }

    public Page<Set> findAllWithoutImg(Pageable pageable) {
        return findAll(meta.imgPath.isNull().and(meta.outerImgUrl.isNotEmpty()), pageable);
    }
}
