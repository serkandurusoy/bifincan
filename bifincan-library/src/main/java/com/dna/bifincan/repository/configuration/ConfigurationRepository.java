package com.dna.bifincan.repository.configuration;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.model.user.User;

public interface ConfigurationRepository extends JpaRepository<Configuration, String> {
	@Query("select c from Configuration c where c.key = :key")
	public Configuration findByKey(@Param("key")String key);	
}
