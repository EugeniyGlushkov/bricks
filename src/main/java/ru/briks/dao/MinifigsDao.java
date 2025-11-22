package ru.briks.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.briks.entity.Minifig;

import java.util.List;

@Repository
public interface MinifigsDao extends CrudRepository<Minifig,Long> {
    Page<Minifig> findAll(Pageable pageable);
}
