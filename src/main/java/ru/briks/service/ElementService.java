package ru.briks.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.ElementDao;
import ru.briks.dto.ElementOfferDto;
import ru.briks.entity.State;
import ru.briks.service.filter.ElementInfoFilterService;

/**
 * @author EGlushkov
 * Date: 19.04.2026
 * Time: 11:52
 */

@Service
@Transactional(readOnly = true)
public class ElementService {
    private final ElementDao elementDao;
    private final ElementInfoFilterService filterService;

    public ElementService(ElementDao elementDao, ElementInfoFilterService filterService) {
        this.elementDao = elementDao;
        this.filterService = filterService;
    }

    public Page<ElementOfferDto> findOffersByPart(
            Long partId, State state, Boolean inStock, Pageable pageable) {

        var predicate = filterService.buildPredicate(
                ElementInfoFilterService.Visibility.ADMIN,
                null, null, null, null, state, null, null, inStock);
        return elementDao.findOffersByPartId(partId, predicate, pageable);
    }
}