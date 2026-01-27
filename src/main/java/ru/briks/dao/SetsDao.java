package ru.briks.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.briks.entity.Set;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

public interface SetsDao extends CrudRepository<Set,Long> {
    Page<Set> findAll(Pageable pageable);
}
