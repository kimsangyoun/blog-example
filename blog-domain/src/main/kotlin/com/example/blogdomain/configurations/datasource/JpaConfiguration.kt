package com.example.blogdomain.configurations.datasource

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = ["com.example.blogdomain"])
@EnableJpaRepositories(basePackages = ["com.example.blogdomain"])
internal class JpaConfiguration