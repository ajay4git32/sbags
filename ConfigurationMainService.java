package com.vibrent.sense.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibrent.sense.domain.ConfigurationMain;
import com.vibrent.sense.repository.ConfigurationMainRepository;

@Service
public class ConfigurationMainService {
	@Autowired
	 private  ConfigurationMainRepository configurationMainRepository;

	    public List<ConfigurationMain> findAll() {
	        return configurationMainRepository.findAll();
	    }

	    public Optional<ConfigurationMain> findById(Long id) {
	        return configurationMainRepository.findById(id);
	    }

	    public ConfigurationMain save(ConfigurationMain configurationMain) {
	        return configurationMainRepository.save(configurationMain);
	    }

	    public void deleteById(Long id) {
	    	configurationMainRepository.deleteById(id);
	    }
}
