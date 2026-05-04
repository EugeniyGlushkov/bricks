package ru.briks.dao;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ru.briks.dto.ColorKey;
import ru.briks.dto.ElementOfferDto;
import ru.briks.entity.Element;
import ru.briks.entity.QElement;
import ru.briks.entity.QElementExternalId;
import ru.briks.entity.QElementInfo;
import ru.briks.entity.QInventoryPart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.LongSupplier;
import java.util.stream.Collectors;

/**
 * @author EGlushkov
 * Date: 30.03.2026
 * Time: 21:42
 */

@Repository
public class ElementDao extends AbstractDao<Element, Long> {
    private static final QElement meta;
    private static final QElementInfo EI = QElementInfo.elementInfo;
    private static final QInventoryPart IP = QInventoryPart.inventoryPart;

    static {
        meta = QElement.element;
    }

    public ElementDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(Element.class, em, queryFactory);
    }

    public Page<ElementOfferDto> findOffersByPartId(Long partId, Predicate predicate, Pageable pageable) {
        var baseQuery = query()
                .select(Projections.constructor(ElementOfferDto.class,
                        meta.id,
                        meta.part.id,
                        meta.color.id,
                        meta.part.partNum,
                        meta.part.name,
                        meta.color.name,
                        meta.color.rgb,
                        EI.id,
                        EI.state,
                        EI.count,
                        EI.price,
                        EI.priceKuboka,
                        Expressions.nullExpression(String.class),
                        Expressions.nullExpression(List.class)
                ))
                .from(meta)
                .leftJoin(EI).on(EI.element.id.eq(meta.id))
                .where(meta.part.id.eq(partId))
                .where(predicate)
                .orderBy(meta.part.partNum.asc(), meta.color.name.asc(), EI.state.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<ElementOfferDto> offers = baseQuery.fetch();

        if (!offers.isEmpty()) {
            Set<ColorKey> colorKeys = offers.stream()
                    .map(dto -> new ColorKey(dto.partId(), dto.colorId()))
                    .collect(Collectors.toSet());
            Map<ColorKey, String> imageMap = new HashMap<>();
            BooleanBuilder orConditions = new BooleanBuilder();
            for (ColorKey key : colorKeys) {
                orConditions.or(key.colorId() == null
                        ? IP.partId.eq(key.partId()).and(IP.colorId.isNull())
                        : IP.partId.eq(key.partId()).and(IP.colorId.eq(key.colorId())));
            }
            query()
                    .select(IP.partId, IP.colorId, IP.outerImgUrl)
                    .from(IP)
                    .where(orConditions)
                    .where(IP.outerImgUrl.isNotNull(), IP.outerImgUrl.ne(""))
                    .fetch()
                    .forEach(tuple -> {
                        ColorKey key = new ColorKey(tuple.get(IP.partId), tuple.get(IP.colorId));
                        imageMap.putIfAbsent(key, tuple.get(IP.outerImgUrl));
                    });

            Map<Long, List<String>> externalIdsMap = new HashMap<>();
            QElementExternalId ExtId = QElementExternalId.elementExternalId;
            Set<Long> elementIds = offers.stream()
                    .map(ElementOfferDto::elementId)
                    .collect(Collectors.toSet());

            query().select(ExtId.element.id, ExtId.externalId)
                    .from(ExtId)
                    .where(ExtId.element.id.in(elementIds))
                    .fetch()
                    .forEach(tuple -> {
                        externalIdsMap.computeIfAbsent(tuple.get(ExtId.element.id), k -> new ArrayList<>())
                                .add(tuple.get(ExtId.externalId));
                    });
            offers = offers.stream().map(dto -> new ElementOfferDto(
                    dto.elementId(), dto.partId(), dto.colorId(),
                    dto.partNum(), dto.partName(), dto.colorName(), dto.rgb(),
                    dto.infoId(), dto.state(), dto.count(),
                    dto.price(), dto.priceKuboka(),
                    imageMap.get(new ColorKey(dto.partId(), dto.colorId())),
                    // Внешние ID (пустой список, если нет)
                    externalIdsMap.getOrDefault(dto.elementId(), Collections.emptyList())
            )).toList();
        }

        LongSupplier countSupplier = () -> query()
                .from(meta)
                .leftJoin(EI).on(EI.element.id.eq(meta.id))
                .where(meta.part.id.eq(partId))
                .where(predicate)
                .fetchCount();

        return PageableExecutionUtils.getPage(offers, pageable, countSupplier);
    }
}