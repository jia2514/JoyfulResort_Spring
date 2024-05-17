package com.joyfulresort.reserveorder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.joyfulresort.reserveorder.model.ResService;
import com.joyfulresort.reservesession.model.RessionService;

@Controller
@RequestMapping("/joyfulresort")
public class ResTimeQueryController {
	@Autowired
	RessionService ressionSvc;
	@Autowired
	ResService resSvc;
}
