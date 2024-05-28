package com.qalist.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qalist.model.QaList;
import com.qalist.model.QaListService;

@RestController
@RequestMapping("/qalist")
public class QaListConController {

    @Autowired
    QaListService qaListService;

    @GetMapping("/reply")
    public Set<QaList> getQaAnswer(@RequestParam("keyWord") String keyWord) {
        return qaListService.getQaAnswer(keyWord);
    }
}
