package ru.briks.service.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Service;
import ru.briks.entity.QElementInfo;
import ru.briks.entity.State;

import java.math.BigDecimal;

/**
 * @author EGlushkov
 * Date: 19.04.2026
 * Time: 0:34
 */

@Service
public class ElementInfoFilterService {
    private static final QElementInfo meta = QElementInfo.elementInfo;

    public enum Visibility { PUBLIC, ADMIN }

    public Predicate buildPredicate(
            Visibility visibility,
            String partName, String partNum, Long colorId, String colorName,
            State state, BigDecimal minPrice, BigDecimal maxPrice, Boolean inStock
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        if (partName != null && !partName.isBlank()) {
            builder.and(meta.element.part.name.containsIgnoreCase(partName.trim()));
        }
        if (partNum != null && !partNum.isBlank()) {
            builder.and(meta.element.part.partNum.containsIgnoreCase(partNum.trim()));
        }
        if (colorId != null) {
            builder.and(meta.element.color.id.eq(colorId));
        }
        if (colorName != null && !colorName.isBlank()) {
            builder.and(meta.element.color.name.containsIgnoreCase(colorName.trim()));
        }
        if (state != null) {
            builder.and(meta.state.eq(state));
        }
        if (minPrice != null) {
            builder.and(meta.price.goe(minPrice));
        }
        if (maxPrice != null) {
            builder.and(meta.price.loe(maxPrice));
        }

        // 🔹 Ролевая видимость
        if (visibility == Visibility.PUBLIC) {
            builder.and(meta.price.isNotNull());
            builder.and(meta.count.gt(0L));
        }
        if (Boolean.TRUE.equals(inStock)) {
            builder.and(meta.count.gt(0L));
        }

        return builder;
    }
}
