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
        basePackages = "com.example.dsms.thies.repository",
        entityManagerFactoryRef = "thiesEntityManagerFactory",
        transactionManagerRef = "thiesTransactionManager"
)
public class ThiesDataSourceConfig {

    @Bean(name = "thiesDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.thies")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "thiesEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("thiesDataSource") DataSource dataSource) {
        return builder
                .dataSource(dataSource)
                .packages("com.example.dsms.model")
                .persistenceUnit("thies")
                .build();
    }

    @Bean(name = "thiesTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("thiesEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
    // ✅ Pour la synchro
    @Bean(name = "thiesJdbcTemplate")
    public JdbcTemplate thiesJdbcTemplate(@Qualifier("thiesDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
