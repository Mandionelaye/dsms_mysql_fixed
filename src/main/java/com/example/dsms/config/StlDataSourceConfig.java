package com.example.dsms.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.dsms.stl.repository",
        entityManagerFactoryRef = "stlEntityManagerFactory",
        transactionManagerRef = "stlTransactionManager"
)
public class StlDataSourceConfig {

    @Bean(name = "stlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.stl")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "stlEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("stlDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.dsms.model")
                .persistenceUnit("stl")
                .build();
    }

    @Bean(name = "stlTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("stlEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    // ✅ Pour la synchro
    @Bean(name = "stlJdbcTemplate")
    public JdbcTemplate stlJdbcTemplate(@Qualifier("stlDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

}
