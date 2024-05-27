package com;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Error implements ErrorController {
//要debug把這個關掉
    @RequestMapping("/error")
    public String handleError() {
        
        return "/error404";
    }

    
}