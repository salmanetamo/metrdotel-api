package dev.devmonks.metrdotel.configuration.db

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.core.JdbcTemplate
import javax.sql.DataSource

@Configuration
class DatabaseConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties(prefix = "datasource.primary")
    fun provideDatasource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    fun primaryJDBCTemplate(): JdbcTemplate {
        return  JdbcTemplate(provideDatasource())
    }
}