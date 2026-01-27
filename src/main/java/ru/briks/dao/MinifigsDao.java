package ru.briks.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.briks.entity.Minifig;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Repository
public interface MinifigsDao extends CrudRepository<Minifig,Long> {
    Page<Minifig> findAll(Pageable pageable);
}
