package com.qalist.model;

import com.qalist.model.QaList;
import com.qalist.model.QaListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service("qaListService")
public class QaListService {

    @Autowired
    private QaListRepository qaListRepository;

    public void addQaList(QaList qaList){
//      TODO:不允許重複輸入相同值
    	qaListRepository.save(qaList);
  }

  public void updateQaList(QaList qaList){
	  qaListRepository.save(qaList);
  }

  public QaList getOneQaList(Integer qaId){
      Optional<QaList> optional = qaListRepository.findById(qaId);
      return optional.orElse(null);
  }

  public List<QaList> getAll(){
      return qaListRepository.findAll();
  }

  public Set<QaList> getQaAnswer(String qaId){
      return qaListRepository.findByKeyWordContainingIgnoreCase(qaId);
  }

}
