package ru.briks.service;

import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.briks.dao.PartDao;
import ru.briks.dto.PartAdminDto;
import ru.briks.entity.Part;
import ru.briks.entity.QPart;

/**
 * @author EGlushkov
 * Date: 06.04.2026
 * Time: 18:54
 */

@Service
@Transactional(readOnly = true)
public class PartService {
    private PartDao partDao;
    private static final QPart meta = QPart.part;

    public PartService(PartDao partDao) { this.partDao = partDao; }

    public Page<Part> getParts(int pageNum, int size) {
        return partDao.findAll(pageNum, size);
    }

    public Page<PartAdminDto> findPartsForAdmin(String search, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        if (search != null && !search.isBlank()) {
            String term = search.trim();
            builder.and(meta.partNum.containsIgnoreCase(term)
                    .or(meta.name.containsIgnoreCase(term)));
        }

        return partDao.findPartsForAdmin(builder, pageable);
    }
}
