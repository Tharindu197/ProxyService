package com.fidenz.academy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.Properties;

@SpringBootApplication
@EnableJpaRepositories("com.fidenz.academy.repo")
public class Application {

    @Autowired
    private Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    //enable cors orgin globally
    @Bean
    public CorsFilter corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Content-Type", "Accept"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH"));
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean lef = new LocalContainerEntityManagerFactoryBean();
        lef.setPackagesToScan(environment.getRequiredProperty("spring.base_package_to_scan"));
        lef.setJpaVendorAdapter(jpaVendorAdapter());
        Properties properties = new Properties();
        properties.setProperty("hibernate.ogm.datastore.provider", environment.getRequiredProperty("hibernate.ogm.datastore.provider"));
        properties.setProperty("javax.persistence.transactionType", environment.getRequiredProperty("javax.persistence.transactionType"));
        properties.setProperty("hibernate.ogm.datastore.host", environment.getRequiredProperty("hibernate.ogm.datastore.host"));
        properties.setProperty("hibernate.ogm.datastore.port", environment.getRequiredProperty("hibernate.ogm.datastore.port"));
        properties.setProperty("hibernate.ogm.datastore.database", environment.getRequiredProperty("hibernate.ogm.datastore.database"));
        properties.setProperty("hibernate.ogm.datastore.create_database", environment.getRequiredProperty("hibernate.ogm.datastore.create_database"));
        lef.setJpaProperties(properties);
        return lef;
    }

    @Bean(name = "jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        return vendorAdapter;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager
                = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(
                entityManagerFactory().getObject());
        return transactionManager;
    }
}
