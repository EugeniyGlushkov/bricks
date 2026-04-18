package ru.briks.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.briks.entity.ElementInfo;
import ru.briks.entity.QElementInfo;
import ru.briks.entity.State;

import java.util.Optional;

/**
 * @author EGlushkov
 * Date: 31.03.2026
 * Time: 16:46
 */

@Repository
public class ElementInfoDao extends AbstractDao<ElementInfo, Long> {
    private static final QElementInfo meta;

    static {
        meta = QElementInfo.elementInfo;
    }

    public ElementInfoDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(ElementInfo.class, em, queryFactory);
    }

    public Optional<ElementInfo> findByElementIdAndState(Long elementId, State state) {
        return Optional.ofNullable(query()
                .from(meta)
                .select(meta)
                .where(meta.element.id.eq(elementId).and(meta.state.eq(state)))
                .fetchFirst());
    }
}
