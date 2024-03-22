package ru.moiseenko.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import javax.sql.DataSource;

@Configuration
@EnableAutoConfiguration
@EnableJdbcRepositories("ru.moiseenko.repository")
@ComponentScan("ru.moiseenko")
@PropertySource("classpath:application.properties")
public class Config {
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl("jdbc:sqlserver://LAPTOP-1Q0NKG6M:1433;databaseName=BuyTickets");
        dataSource.setUsername("UserApplication1");
        dataSource.setPassword("password");
        return dataSource;
    }
    @Bean
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
}
