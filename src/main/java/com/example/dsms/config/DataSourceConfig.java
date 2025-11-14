//package com.example.dsms.config;
//
//import javax.sql.DataSource;
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import java.util.HashMap;
//
//@Configuration
//public class DataSourceConfig {
//
//    // Dakar - primary JPA datasource
//    @Bean
//    @Primary
//    @ConfigurationProperties("spring.datasource.dakar")
//    public DataSourceProperties dakarDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean
//    @Primary
//    public DataSource dakarDataSource() {
//        return dakarDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "entityManagerFactory")
//    @Primary
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("dakarDataSource") DataSource dakarDataSource) {
//        LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
//        emf.setDataSource(dakarDataSource);
//        emf.setPackagesToScan("com.example.dsms.model");
//        emf.setPersistenceUnitName("dakar-pu");
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        emf.setJpaVendorAdapter(vendorAdapter);
//
//        HashMap<String, Object> props = new HashMap<>();
//        props.put("hibernate.hbm2ddl.auto", "update");
//        props.put("hibernate.show_sql", "true");
//        props.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
//        emf.setJpaPropertyMap(props);
//
//        return emf;
//    }
//
//    @Bean
//    @Primary
//    public JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    // Thies
//    @Bean
//    @ConfigurationProperties("spring.datasource.thies")
//    public DataSourceProperties thiesDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "thiesDataSource")
//    public DataSource thiesDataSource() {
//        return thiesDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "thiesJdbcTemplate")
//    public JdbcTemplate thiesJdbcTemplate(@Qualifier("thiesDataSource") DataSource ds) {
//        return new JdbcTemplate(ds);
//    }
//
//    // Saint-Louis
//    @Bean
//    @ConfigurationProperties("spring.datasource.stl")
//    public DataSourceProperties stlDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean(name = "stlDataSource")
//    public DataSource stlDataSource() {
//        return stlDataSourceProperties().initializeDataSourceBuilder().build();
//    }
//
//    @Bean(name = "stlJdbcTemplate")
//    public JdbcTemplate stlJdbcTemplate(@Qualifier("stlDataSource") DataSource ds) {
//        return new JdbcTemplate(ds);
//    }
//}
