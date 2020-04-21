package com.dna.bifincan.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dna.bifincan.model.config.Configuration;
import com.dna.bifincan.repository.configuration.ConfigurationRepository;

@Service
@Named("configurationService")
public class ConfigurationService {
	@SuppressWarnings("unused")
	private static final Logger log = LoggerFactory.getLogger(ConfigurationService.class);

	@Inject
	private ConfigurationRepository configurationRepository;
	
	public ConfigurationService() { }
	
	public ConfigurationService(ConfigurationRepository configurationRepository) { 
		this.configurationRepository = configurationRepository; 
	}
	
	/**
	 * This method returns a list of configuration parameter entities.
	 * @return the list of copies
	 */
	public List<Configuration> getParameters() {
        List<Order> sortOrders = new ArrayList<Order>();

        Order order = new Order(Direction.ASC, "key");
        sortOrders.add(order);

        Sort sort = new Sort(sortOrders);
        return this.configurationRepository.findAll(sort);
	}
	
	public Configuration find(String key) {
		return configurationRepository.findByKey(key);
	}
	
	/**
	 * This method persists a parameter in the database, reloads the cache and then returns
	 * a "copy" of the persisted object so as to avoid any accidental changes in the client code. 
	 * NOTE: this method normally should not be used frequently.
	 * @param config the configuration parameter that is to be persisted
	 * @return a copy of the cached configuration parameter object
	 */
	@Transactional
	public Configuration saveOrUpdate(Configuration config) {
		return configurationRepository.save(config);  // persist the object
	}	
	
}
