package com.mentalhealthassist.spring.app.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@EnableAspectJAutoProxy
@ComponentScan("com.mentalhealthassist.spring.app")
@PropertySource({ "classpath:application.properties"})
public class MentalHealthAssistAppConfig implements WebMvcConfigurer {

	//Environment object to retrieve data from properties file
    @Autowired
    private Environment env;
    
    //logger for debugging
    private Logger logger = Logger.getLogger(getClass().getName());
    

    @Bean
    public DataSource myDataSource() {
        
        // create connection pool
        ComboPooledDataSource myDataSource = new ComboPooledDataSource();

        // set the jdbc driver
        try {
            myDataSource.setDriverClass("com.mysql.cj.jdbc.Driver");        
        }
        catch (PropertyVetoException exc) {
            throw new RuntimeException(exc);
        }
        
        //log url and user ... make sure we are reading the data
        //not password for security
        logger.info("jdbc.url=" + env.getProperty("jdbc.url"));
        logger.info("jdbc.user=" + env.getProperty("jdbc.user"));
        
        // set database connection props
        myDataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        myDataSource.setUser(env.getProperty("jdbc.user"));
        myDataSource.setPassword(env.getProperty("jdbc.password"));
        
        // set connection pool props
        myDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
        myDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
        myDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));        
        myDataSource.setMaxIdleTime(getIntProperty("connection.pool.maxIdleTime"));

        return myDataSource;
    }
    
    private Properties getHibernateProperties() {

        // set hibernate properties
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        
        return props;                
    }

    
    // need a helper method
    // read environment property and convert to int
    private int getIntProperty(String propName) {
        
    	//get environment variable
        String propVal = env.getProperty(propName);
        
        // now convert to int
        int intPropVal = Integer.parseInt(propVal);
        
        return intPropVal;
    }    
    
    @Bean
    public LocalSessionFactoryBean sessionFactory(){
        
        // create session factorys
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        
        // set the properties
        sessionFactory.setDataSource(myDataSource());
        sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());
        
        return sessionFactory;
    }
    
    @Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        
        // setup transaction manager based on session factory
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);

        return txManager;
    }    
    

	
	  //set folder for resource retrieval
	  @Override 
	  public void addResourceHandlers(ResourceHandlerRegistry registry) {
	 
		  if (!registry.hasMappingForPattern("/resources/**")) {
			     registry
			     .addResourceHandler("/resources/**")
			     .addResourceLocations("/resources/");
			  }
	  }
	 

    //THYMELEAF config
	  
	 //setting file type and access folder 
    @Bean
    @Description("Thymeleaf Template Resolver")
    public ServletContextTemplateResolver templateResolver(ServletContext servletContext) {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setPrefix("/WEB-INF/views/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");

        return templateResolver;
    }
    
    
    @Bean
    @Description("Thymeleaf Template Engine")
    public SpringTemplateEngine templateEngine(ServletContext servletContext) {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver(servletContext));
        templateEngine.setTemplateEngineMessageSource(messageSource());
        return templateEngine;
    }
    
    
    @Bean
    @Description("Thymeleaf View Resolver")
    public ThymeleafViewResolver viewResolver(ServletContext servletContext) {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine(servletContext));
        viewResolver.setOrder(1);
        return viewResolver;
    }
    
    
    @Bean
    @Description("Spring Message Resolver")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
    
    
    
}

