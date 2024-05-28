package com.qalist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/joyfulresort/qalist")
public class FrontendQaListController {


    @GetMapping("/qaList")
    public String gochat() {
        return "front-end/qalist/qaList";
    }
}

