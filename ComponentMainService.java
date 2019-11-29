package com.vibrent.sense.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vibrent.sense.domain.ComponentMain;
import com.vibrent.sense.repository.ComponentMainRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ComponentMainService {
	@Autowired
	 private ComponentMainRepository componentMainRepository;

	    public List<ComponentMain> findAll() {
	        return componentMainRepository.findAll();
	    }

	    public Optional<ComponentMain> findById(Long id) {
	        return componentMainRepository.findById(id);
	    }

	    public ComponentMain save(ComponentMain componentMain) {
	        return componentMainRepository.save(componentMain);
	    }

	    public void deleteById(Long id) {
	    	componentMainRepository.deleteById(id);
	    }
}
