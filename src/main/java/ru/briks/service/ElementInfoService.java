package ru.briks.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.ElementInfoDao;
import ru.briks.entity.ElementInfo;
import ru.briks.entity.State;
import java.util.Optional;

/**
 * @author EGlushkov
 * Date: 19.04.2026
 * Time: 0:41
 */

@Service
@Transactional
public class ElementInfoService {
    private final ElementInfoDao elementInfoDao;

    public ElementInfoService(ElementInfoDao elementInfoDao) {
        this.elementInfoDao = elementInfoDao;
    }

    public Optional<ElementInfo> findById(Long id) {
        return elementInfoDao.findById(id);
    }

    public Optional<ElementInfo> findByElementIdAndState(Long elementId, State state) {
        return elementInfoDao.findByElementIdAndState(elementId, state);
    }

    public <S extends ElementInfo> S save(S entity) {
        return elementInfoDao.save(entity);
    }

    public void deleteById(Long id) {
        elementInfoDao.deleteById(id);
    }
}
