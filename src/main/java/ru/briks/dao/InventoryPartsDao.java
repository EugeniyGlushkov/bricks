package ru.briks.dao;

import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import ru.briks.entity.InventoryPart;
import ru.briks.entity.QInventoryPart;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Repository
public class InventoryPartsDao extends AbstractDao<InventoryPart,Long> {
    private static final QInventoryPart meta;

    static {
        meta = QInventoryPart.inventoryPart;
    }

    public InventoryPartsDao(EntityManager em) {
        super(InventoryPart.class, em);
    }

    public Page<InventoryPart> findAllWithoutImg(Pageable pageable) {
        return findAll(meta.imgPath.isNull().and(meta.outerImgUrl.isNotEmpty()), pageable);
    }
}
