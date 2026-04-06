package ru.briks.dao;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import ru.briks.entity.Element;
import ru.briks.entity.QElement;

import java.util.List;
import java.util.Optional;

/**
 * @author EGlushkov
 * Date: 30.03.2026
 * Time: 21:42
 */

@Repository
public class ElementsDao extends AbstractDao<Element,Long> {
    private static final QElement meta;

    static {
        meta = QElement.element;
    }

    public ElementsDao(EntityManager em) {
        super(Element.class, em);
    }

    public List<Element> findByElementIds(List<String> elementIds) {
        return query().from(meta).select(meta).where(meta.elementId.in(elementIds)).fetch();
    }

    public Optional<Element> findByElementId(String elementId) {
        return Optional.ofNullable(query().from(meta).select(meta).where(meta.elementId.eq(elementId)).fetchFirst());
    }
}
