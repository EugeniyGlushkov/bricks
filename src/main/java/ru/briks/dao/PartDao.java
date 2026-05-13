package ru.briks.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ru.briks.dto.PartAdminDto;
import ru.briks.entity.Part;
import ru.briks.entity.QInventoryPart;
import ru.briks.entity.QPart;

import java.util.List;

/**
 * @author EGlushkov
 * Date: 06.04.2026
 * Time: 18:44
 */

@Repository
public class PartDao extends AbstractDao<Part, Long> {
    private static final QPart meta;
    private static final QInventoryPart IP = QInventoryPart.inventoryPart;

    static {
        meta = QPart.part;
    }

    public PartDao(EntityManager em, JPQLQueryFactory queryFactory) {
        super(Part.class, em, queryFactory);
    }

    public Page<Part> findAll(int pageNum, int size) {
        Pageable pageable = PageRequest.of(pageNum, size);
        return findAll(pageable);
    }

    /**
     * Админский список деталей: 1 строка на part.
     * LEFT JOIN к inventory_parts для получения любой валидной картинки.
     * GROUP BY + MIN(IP.outerImgUrl) гарантирует ровно 1 строку на деталь.
     */
    public Page<PartAdminDto> findPartsForAdmin(Predicate predicate, Pageable pageable) {
        var query = query()
                .select(Projections.constructor(PartAdminDto.class,
                        meta.id,
                        meta.partNum,
                        meta.partNumBricklink,
                        meta.name,
                        meta.partCategory.id,  // ← Идём через @ManyToOne связь
                        IP.outerImgUrl.min()
                ))
                .from(meta)
                .leftJoin(IP).on(
                        IP.partId.eq(meta.id)
                                .and(IP.outerImgUrl.isNotNull())
                                .and(IP.outerImgUrl.ne(""))
                )
                .where(predicate)
                .groupBy(meta.id, meta.partNum, meta.name, meta.partCategory.id) // ← Группируем по тем же полям
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        var countQuery = query()
                .select(meta.id.count())
                .from(meta)
                .where(predicate);

        return PageableExecutionUtils.getPage(
                query.fetch(),
                pageable,
                () -> countQuery.fetchOne()
        );
    }

    public List<PartAdminDto> findAnalogsById(Long id) {
        QPart partAnalog = new QPart("analog");
        return query()
                .select(Projections.constructor(PartAdminDto.class,
                        partAnalog.id,
                        partAnalog.partNum,
                        Expressions.nullExpression(String.class),
                        Expressions.nullExpression(String.class),
                        Expressions.nullExpression(Long.class),
                        Expressions.nullExpression(String.class)))
                .from(meta)
                .join(partAnalog).on(partAnalog.partNumBricklink.eq(meta.partNumBricklink))
                .where(meta.id.eq(id).and(meta.id.ne(partAnalog.id)))
                .distinct()
                .fetch();
    }
}
