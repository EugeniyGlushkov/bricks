package ru.briks.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.briks.entity.ElementExternalId;
import ru.briks.entity.QElementExternalId;

import java.util.Collections;
import java.util.List;

/**
 * @author EGlushkov
 * Date: 10.05.2026
 * Time: 20:30
 */

@Repository
public class ElementExternalIdDao extends AbstractDao<ElementExternalId, Long> {
    private static final QElementExternalId meta;

    static {
        meta = QElementExternalId.elementExternalId;
    }

    public ElementExternalIdDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(ElementExternalId.class, em, queryFactory);
    }

    public List<ElementExternalId> findAllByExternalIds(List<String> externalIds) {
        if (externalIds == null || externalIds.isEmpty()) {
            return Collections.emptyList();
        }

        return query().select(meta)
                .from(meta)
                .where(meta.externalId.in(externalIds))
                .fetch();
    }
}
