package com.ksl.myLists.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;

import liquibase.integration.spring.SpringLiquibase;

/**
 * Configuration for services, daos, entities, and actions
 * 
 * @author Keith Lopez
 */
@Configuration
@ComponentScan({"com.ksl.myLists.services","com.ksl.myLists.dao","com.ksl.myLists.actions"})
@EnableTransactionManagement
public class RootContextConfig {

	/**
	 * Enable liquibase database management. Liquibase administers the database based on 
	 * changes described in classpath:/db/changelog/db.changelog-master.xml
	 * 
	 * @param dataSource
	 * @return
	 */
	@Bean
	public SpringLiquibase getSpringLiquibase(DriverManagerDataSource dataSource) {
		SpringLiquibase liquibase = new SpringLiquibase();
		liquibase.setDataSource(dataSource);
		liquibase.setChangeLog("classpath:/db/changelog/db.changelog-master.xml");
		return liquibase;
	}
	
	/**
	 * Will be working with persistence so will need transaction management for the entity manager.
	 * 
	 * @param entityManagerFactory
	 * @param dataSource
	 * @return
	 */
    @Bean(name = "transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory,
                                                         DriverManagerDataSource dataSource) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        transactionManager.setDataSource(dataSource);
        return transactionManager;
    }
 
    /**
     * MySql database pulling connection information from System properties so as not to embed in code.
     * 
     * @return
     */
    @Bean(name = "datasource")
    public DriverManagerDataSource dataSource() {
    	DriverManagerDataSource dataSource = new DriverManagerDataSource();
    	dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    	

    	String mySqlUrl = System.getProperty("com.ksl.myLists.mysql.url");
    	String mySqlUserName = System.getProperty("com.ksl.myLists.mysql.userName");
    	String mySqlPassword = System.getProperty("com.ksl.myLists.mysql.password");
    	
    	dataSource.setUrl(mySqlUrl);
    	dataSource.setUsername(mySqlUserName);
    	dataSource.setPassword(mySqlPassword);
    
    	return dataSource;
    }
    
    /**
     * YEAH for annotations! EntityManager will be injected into daos via @PersistenceContext
     * 
     * @param dataSource
     * @return
     */
    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DriverManagerDataSource dataSource) {

        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setPackagesToScan(new String[]{"com.ksl.myLists.entities"});
        entityManagerFactoryBean.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());
        entityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());

        Map<String, Object> jpaProperties = new HashMap<String, Object>();
        jpaProperties.put("hibernate.show_sql", "true");
        jpaProperties.put("hibernate.format_sql", "true");
        jpaProperties.put("hibernate.use_sql_comments", "true");
        jpaProperties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        entityManagerFactoryBean.setJpaPropertyMap(jpaProperties);

        return entityManagerFactoryBean;
    }
}
