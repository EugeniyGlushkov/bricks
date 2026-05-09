package ru.briks.dao;


import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.briks.entity.PartCategory;
import ru.briks.entity.QPartCategory;

/**
 * @author EGlushkov
 * Date: 08.05.2026
 * Time: 0:15
 */

@Repository
public class PartCategoryDao extends AbstractDao<PartCategory, Long> {
    private static final QPartCategory meta;

    static {
        meta = QPartCategory.partCategory;
    }

    public PartCategoryDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(PartCategory.class, em, queryFactory);
    }
}