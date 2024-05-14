package com.joyfulresort.reservecontent.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("rescontentService")
public class ResContentService {

    @Autowired
    private ResContentRepository repository;

    public void addContent(ResContentVO rescontentVO) {
        repository.save(rescontentVO);
    }

    public void updateContent(ResContentVO rescontentVO) {
        repository.save(rescontentVO);
    }

    public void deleteContent(Integer rescontentVO) {
        if (repository.existsById(rescontentVO)) {
            repository.deleteById(rescontentVO);
        }
    }

    public ResContentVO getOneContent(Integer rescontentVO) {
        Optional<ResContentVO> optional = repository.findById(rescontentVO);
        return optional.orElse(null);
    }

    public List<ResContentVO> getAllContent() {
        return repository.findAll();
    }
}
	

