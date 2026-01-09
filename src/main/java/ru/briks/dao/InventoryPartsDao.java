package ru.briks.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import ru.briks.entity.InventoryPart;

public interface InventoryPartsDao extends CrudRepository<InventoryPart,Long> {
    Page<InventoryPart> findAll(Pageable pageable);
}
