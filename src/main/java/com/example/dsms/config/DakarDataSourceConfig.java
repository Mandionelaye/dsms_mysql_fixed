package com.example.dsms.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.dsms.dakar.repository",
        entityManagerFactoryRef = "dakarEntityManagerFactory",
        transactionManagerRef = "dakarTransactionManager"
)
public class DakarDataSourceConfig {

    @Primary
    @Bean(name = "dakarDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.dakar")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "dakarEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("dakarDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.dsms.model")
                .persistenceUnit("dakar")
                .build();
    }

    @Primary
    @Bean(name = "dakarTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("dakarEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    // ✅ Pour la synchro
    @Bean(name = "dakarJdbcTemplate")
    public JdbcTemplate dakarJdbcTemplate(@Qualifier("dakarDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
