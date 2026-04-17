package ru.briks.dao;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@NoRepositoryBean
public interface CustomQueryDslJpaRepository<T, ID extends Serializable>{
    long delete(Predicate predicate);

    T findOne(FactoryExpression<T> factoryExpression, Predicate predicate);

    Optional<T> findById(ID id);

    Page<T> findAll(Pageable pageable);

    List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate);

    List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, OrderSpecifier<?> orderSpecifier);

    Page<T> findAll(FactoryExpression<T> factoryExpression, Pageable pageable);

    Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable);

    <S extends T> S save(S entity);

    <S extends T> List<S> saveAll(Iterable<S> entities);

    JPQLQueryFactory query();
}
