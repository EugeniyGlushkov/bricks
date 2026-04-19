package ru.briks.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.briks.entity.Minifig;
import ru.briks.entity.QMinifig;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Repository
public class MinifigDao extends AbstractDao<Minifig,Long> {
    private static final QMinifig meta;

    static {
        meta = QMinifig.minifig;
    }

    public MinifigDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(Minifig.class, em, queryFactory);
    }

    public Page<Minifig> findAllWithoutImg(Pageable pageable) {
        return findAll(meta.imgPath.isNull().and(meta.outerImgUrl.isNotEmpty()), pageable);
    }
}
