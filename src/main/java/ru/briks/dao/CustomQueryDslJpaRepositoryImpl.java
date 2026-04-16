package ru.briks.dao;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslJpaPredicateExecutor;
//import org.springframework.data.jpa.repository.support.QuerydslJpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.EntityPathResolver;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.Assert;

import javax.persistence.LockModeType;
import java.io.Serializable;
import java.util.*;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

public class CustomQueryDslJpaRepositoryImpl<T, ID extends Serializable>
        extends QuerydslJpaPredicateExecutor<T>
        implements CustomQueryDslJpaRepository<T, ID> {
    private static final EntityPathResolver DEFAULT_ENTITY_PATH_RESOLVER = SimpleEntityPathResolver.INSTANCE;
    private static final String ID_MUST_NOT_BE_NULL = "The given id must not be null!";


    private final EntityPath<T> path;
    private final Querydsl querydsl;
    protected final EntityManager em;
    protected final JpaEntityInformation<T, ID> entityInformation;

    @Autowired
    private JPQLQueryFactory query;

    public CustomQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
                                           EntityManager entityManager) {
        this(entityInformation, entityManager, DEFAULT_ENTITY_PATH_RESOLVER);
    }

    public CustomQueryDslJpaRepositoryImpl(JpaEntityInformation<T, ID> entityInformation,
                                           EntityManager entityManager,
                                           EntityPathResolver resolver) {
        super(entityInformation, entityManager, resolver, null);
        this.path = resolver.createPath(entityInformation.getJavaType());
        this.querydsl = new Querydsl(entityManager, new PathBuilder<>(path.getType(), path.getMetadata()));
        this.em = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public long delete(Predicate predicate) {
        return query().delete(path).where(predicate).execute();
    }

    @Override
    public T findOne(FactoryExpression<T> factoryExpression, Predicate predicate) {
        final JPQLQuery<?> query = createQuery(predicate);
        return query.select(factoryExpression).fetchFirst();
    }

    @Override
    public Optional<T> findById(ID id) {

        Assert.notNull(id, ID_MUST_NOT_BE_NULL);

        Class<T> domainType = entityInformation.getJavaType();
        return Optional.ofNullable(em.find(domainType, id));
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        final JPQLQuery<?> countQuery = createCountQuery();
        JPQLQuery<T> query = querydsl.applyPagination(pageable, createQuery().select(path));

        return PageableExecutionUtils.getPage(
                query.distinct().fetch(),
                pageable,
                countQuery::fetchCount);
    }

    @Override
    public List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate) {
        findAll();
        final JPQLQuery<?> queryItems = createQuery(predicate);
        return queryItems.select(factoryExpression).fetch();
    }

    @Override
    public List<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, OrderSpecifier<?> orderSpecifier) {
        final JPQLQuery<?> queryItems = createQuery(predicate);
        queryItems.orderBy(orderSpecifier);
        return queryItems.select(factoryExpression).fetch();
    }

    @Override
    public Page<T> findAll(FactoryExpression<T> factoryExpression, Pageable pageable) {
        JPQLQuery<?> countQuery = createQuery();
        JPQLQuery<?> query = querydsl.applyPagination(pageable, createQuery());

        long total = countQuery.fetchCount();
        List<T> content = total > pageable.getOffset() ? query.select(factoryExpression).fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<T> findAll(FactoryExpression<T> factoryExpression, Predicate predicate, Pageable pageable) {
        JPQLQuery<?> countQuery = createQuery(predicate);
        JPQLQuery<?> query = querydsl.applyPagination(pageable, createQuery(predicate));

        long total = countQuery.fetchCount();
        List<T> content = total > pageable.getOffset() ? query.select(factoryExpression).fetch() : Collections.emptyList();

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public <S extends T> S save(S entity) {

        Assert.notNull(entity, "Entity must not be null.");

        if (entityInformation.isNew(entity)) {
            em.persist(entity);
            return entity;
        } else {
            return em.merge(entity);
        }
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {

        Assert.notNull(entities, "Entities must not be null!");

        List<S> result = new ArrayList<>();

        for (S entity : entities) {
            result.add(save(entity));
        }

        return result;
    }

    @Override
    public JPQLQueryFactory query() {
        return this.query;
    }
}
