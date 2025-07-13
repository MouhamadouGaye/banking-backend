package com.mgaye.banking_backend.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

// // @Configuration
// // @EnableJpaRepositories(basePackages =
// "com.mgaye.banking_backend.repository", entityManagerFactoryRef =
// "entityManagerFactory")
// // public class JpaConfig {

// // @Bean
// // public LocalContainerEntityManagerFactoryBean entityManagerFactory(
// // DataSource dataSource) {
// // LocalContainerEntityManagerFactoryBean em = new
// LocalContainerEntityManagerFactoryBean();
// // em.setDataSource(dataSource);
// // em.setPackagesToScan("com.mgaye.banking_backend.model");

// // JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
// // em.setJpaVendorAdapter(vendorAdapter);

// // return em;
// // }
// // }

// // @Configuration
// // @EnableJpaRepositories(basePackages =
// "com.mgaye.banking_backend.repository")
// // public class JpaConfig {
// // @Bean
// // public LocalContainerEntityManagerFactoryBean entityManagerFactory(
// // EntityManagerFactoryBuilder builder,
// // DataSource dataSource) {
// // return builder
// // .dataSource(dataSource)
// // .packages("com.mgaye.banking_backend.model")
// // .persistenceUnit("default")
// // .properties(jpaProperties())
// // .build();
// // }

// // private Map<String, Object> jpaProperties() {
// // Map<String, Object> props = new HashMap<>();
// // props.put("hibernate.dialect",
// "org.hibernate.dialect.PostgreSQL82Dialect"); // This is the problem
// // return props;
// // }
// // }

@Configuration
@EnableJpaRepositories(basePackages = "com.mgaye.banking_backend.repository", entityManagerFactoryRef = "entityManagerFactory")
public class JpaConfig {

    // @Bean
    // public LocalContainerEntityManagerFactoryBean entityManagerFactory(
    // EntityManagerFactoryBuilder builder,
    // DataSource dataSource) {

    // return builder
    // .dataSource(dataSource)
    // .packages("com.mgaye.banking_backend.model")
    // .persistenceUnit("default")
    // .build();
    // }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            DataSource dataSource) {

        Map<String, Object> properties = new HashMap<>();
        // Add only if needed for your DB setup
        properties.put("hibernate.connection.charSet", "UTF-8");
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("com.mgaye.banking_backend.model")
                .persistenceUnit("default")
                .properties(properties)
                .build();
    }
}
