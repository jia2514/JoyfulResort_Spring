package com.qalist.model;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("QaListRepository")
public interface QaListRepository extends JpaRepository<QaList, Integer > {

    Set<QaList> findByKeyWordContainingIgnoreCase(String keyWord);

}

