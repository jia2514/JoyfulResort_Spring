package com.joyfulresort.reservesession.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("ressionService")
public class RessionService {

    @Autowired
    private RessionRepository repository;

    public void addRession(RessionVO ressionVO) {
        repository.save(ressionVO);
    }

    public void updateRession(RessionVO ressionVO) {
        repository.save(ressionVO);
    }

    public void deleteRession(Integer ressionId) {
        if (repository.existsById(ressionId)) {
            repository.deleteById(ressionId);
        }
    }

    public RessionVO getOneRession(Integer ressionId) {
        Optional<RessionVO> optional = repository.findById(ressionId);
        return optional.orElse(null);
    }

    public List<RessionVO> getAllRessions() {
        return repository.findAll();
    }
    
    public Integer getMaxPartById(Integer ressionId) {
        Optional<RessionVO> optional = repository.findById(ressionId);
        if (optional.isPresent()) {
            return optional.get().getReserveMaxPart();
        }
    	return -1;
    }
}
	

