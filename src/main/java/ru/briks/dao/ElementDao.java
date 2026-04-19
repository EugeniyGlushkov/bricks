package ru.briks.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ru.briks.dto.ElementOfferDto;
import ru.briks.entity.Element;
import ru.briks.entity.QElement;
import ru.briks.entity.QElementInfo;
import ru.briks.entity.QInventoryPart;

import java.util.List;
import java.util.Optional;

/**
 * @author EGlushkov
 * Date: 30.03.2026
 * Time: 21:42
 */

@Repository
public class ElementDao extends AbstractDao<Element,Long> {
    private static final QElement meta;
    private static final QElementInfo EI = QElementInfo.elementInfo;
    private static final QInventoryPart IP = QInventoryPart.inventoryPart;

    static {
        meta = QElement.element;
    }

    public ElementDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(Element.class, em, queryFactory);
    }

    public List<Element> findByElementIds(List<String> elementIds) {
        return query().from(meta).select(meta).where(meta.elementId.in(elementIds)).fetch();
    }

    public Optional<Element> findByElementId(String elementId) {
        return Optional.ofNullable(query().from(meta).select(meta).where(meta.elementId.eq(elementId)).fetchFirst());
    }

    public Page<ElementOfferDto> findOffers(Predicate predicate, Pageable pageable) {
        var query = query()
                .select(Projections.constructor(ElementOfferDto.class,
                        meta.id, meta.part.partNum, meta.part.name, meta.color.name, meta.color.rgb,
                        EI.id, EI.state, EI.count, EI.price, EI.priceKuboka,
                        IP.outerImgUrl
                ))
                .from(meta)
                .leftJoin(EI).on(EI.element.id.eq(meta.id))
                .leftJoin(IP).on(
                        IP.partId.eq(meta.part.id)
                                .and(IP.colorId.eq(meta.color.id))
                                .and(IP.outerImgUrl.isNotNull())
                                .and(IP.outerImgUrl.ne(""))
                )
                .where(predicate)
                .distinct()
                .orderBy(IP.outerImgUrl.desc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        var countQuery = query()
                .select(EI.id.countDistinct())
                .from(meta)
                .leftJoin(EI).on(EI.element.id.eq(meta.id))
                .where(predicate);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> countQuery.fetchOne());
    }

    public Page<ElementOfferDto> findOffersByPartId(Long partId, Predicate predicate, Pageable pageable) {
        var query = query()
                .select(Projections.constructor(ElementOfferDto.class,
                        meta.id, meta.part.partNum, meta.part.name, meta.color.name, meta.color.rgb,
                        EI.id, EI.state, EI.count, EI.price, EI.priceKuboka,
                        IP.outerImgUrl
                ))
                .from(meta)
                .where(meta.part.id.eq(partId))
                .leftJoin(EI).on(EI.element.id.eq(meta.id))
                .leftJoin(IP).on(
                        IP.partId.eq(meta.part.id)
                                .and(IP.colorId.eq(meta.color.id))
                                .and(IP.outerImgUrl.isNotNull())
                                .and(IP.outerImgUrl.ne(""))
                )
                .where(predicate)
                .distinct()
                .orderBy(IP.outerImgUrl.desc().nullsLast())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        var countQuery = query()
                .select(EI.id.countDistinct())
                .from(meta)
                .where(meta.part.id.eq(partId))
                .where(predicate);

        return PageableExecutionUtils.getPage(query.fetch(), pageable, () -> countQuery.fetchOne());
    }
}
