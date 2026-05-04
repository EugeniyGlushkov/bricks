package ru.briks.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.ElementDao;
import ru.briks.dao.ElementInfoDao;
import ru.briks.dto.ElementInfoFormDto;
import ru.briks.entity.Element;
import ru.briks.entity.ElementInfo;
import ru.briks.entity.State;
import java.util.Optional;
import java.util.Set;

/**
 * @author EGlushkov
 * Date: 19.04.2026
 * Time: 0:41
 */

@Service
@Transactional
public class ElementInfoService {
    private final ElementInfoDao elementInfoDao;
    private final ElementDao elementDao;

    public ElementInfoService(ElementInfoDao elementInfoDao, ElementDao elementDao) {
        this.elementInfoDao = elementInfoDao;
        this.elementDao = elementDao;
    }

    @Transactional(readOnly = true)
    public Optional<ElementInfo> findById(Long id) {
        return elementInfoDao.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<ElementInfo> findByElementIdAndState(Long elementId, State state) {
        return elementInfoDao.findByElementIdAndState(elementId, state);
    }

    public <S extends ElementInfo> S save(S entity) {
        return elementInfoDao.save(entity);
    }

    public void deleteById(Long id) {
        elementInfoDao.deleteById(id);
    }

    public void saveFromForm(ElementInfoFormDto dto) {
        ElementInfo info;

        if (dto.getId() != null) {
            // Update existing
            info = elementInfoDao.findById(dto.getId())
                    .orElseThrow(() -> new IllegalArgumentException("Offer not found: " + dto.getId()));
            info.setCount(dto.getCount());
            info.setPrice(dto.getPrice());
            info.setPriceKuboka(dto.getPriceKuboka());
        } else {
            if (elementInfoDao.existsByElementIdAndState(dto.getElementId(), dto.getState())) {
                throw new IllegalArgumentException("Offer with state " + dto.getState() + " already exists for this element.");
            }

            // Create new
            info = new ElementInfo();
            Element element = elementDao.findById(dto.getElementId())
                    .orElseThrow(() -> new IllegalArgumentException("Element not found: " + dto.getElementId()));
            info.setElement(element);
            info.setState(dto.getState());
            info.setCount(dto.getCount());
            info.setPrice(dto.getPrice());
            info.setPriceKuboka(dto.getPriceKuboka());
        }

        elementInfoDao.save(info);
    }

    @Transactional(readOnly = true)
    public Set<State> getExistingStates(Long elementId) {
        return elementInfoDao.findExistingStatesByElementId(elementId);
    }
}