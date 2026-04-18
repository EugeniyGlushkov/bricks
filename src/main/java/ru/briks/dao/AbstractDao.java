package ru.briks.dao;

import com.querydsl.jpa.JPQLQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.JpaEntityInformationSupport;
import ru.briks.entity.AbstractEntity;

import java.io.Serializable;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

public abstract class AbstractDao<T extends AbstractEntity<I>, I extends Serializable>
        extends CustomQueryDslJpaRepositoryImpl<T, I> {
    public AbstractDao(Class<T> entityClass, EntityManager em, JPQLQueryFactory queryFactory) {
        super((JpaEntityInformation<T, I>) JpaEntityInformationSupport.getEntityInformation(entityClass, em), em, queryFactory);
    }
}
