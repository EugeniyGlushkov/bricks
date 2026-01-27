package ru.briks.dao;

import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.JPQLQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@NoRepositoryBean
public interface CustomQueryDslJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID>, QuerydslPredicateExecutor<T> {
    long delete(Predicate predicate);

    T findOne(FactoryExpression<T> factoryExpression, Predicate predicate);

    List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate);

    List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, OrderSpecifier<?> orderSpecifier);

    Page<T> findAll(FactoryExpression<T> factoryExpression, Pageable pageable);

    Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable);

    JPQLQueryFactory query();
}
