package ru.briks.dao.config;

import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author EGlushkov
 * Date: 11.01.2026
 * Time: 20:41
 */

@Configuration
public class DomainConfiguration {
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public JPQLQueryFactory jpqlQueryFactory(EntityManager entityManager) {
        return new JPAQueryFactory(() -> entityManager);
    }
}
