package com.footballmanager.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

@Configuration
class BeanConfiguration {
    @Bean
    fun pathMatchingResourcePatternResolver() = PathMatchingResourcePatternResolver()
}