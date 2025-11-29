package com.sample.base_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*\\.common\\.base(\\..*)?"))
@EnableJpaRepositories(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*\\.common\\.base(\\..*)?"))
@EnableCaching
@SpringBootApplication
public class BaseProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BaseProjectApplication.class, args);
	}

}
