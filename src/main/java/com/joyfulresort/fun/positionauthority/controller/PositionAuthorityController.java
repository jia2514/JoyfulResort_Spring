package com.joyfulresort.fun.positionauthority.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.joyfulresort.fun.position.model.Position;
import com.joyfulresort.fun.position.model.PositionService;
import com.joyfulresort.fun.positionauthority.model.PositionAuthorityService;

@Controller
//@RestController
public class PositionAuthorityController {

	@Autowired
	PositionAuthorityService positionAuthorityService;

	
	@Autowired
	PositionService positionService;
	
	@PostMapping("/addPositionAuthority")
	public String addPositionAuthority(@RequestParam("positionId") Integer positionId,
			@RequestParam("functionIds") List<Integer> functionIds, ModelMap model) {
		System.out.println("進入addPositionAuthority方法");

		// 調用服務層來處理職位和權限的添加
		positionAuthorityService.addPositionAuthorities(positionId, functionIds);
		List<Position> list = positionService.findAllPositions();
		model.addAttribute("positionListData", list);
		// 這裡重定向到成功頁面，根據實際情況可能需要調整
		return "back-end/positionauthority/allPositionAuthority"; // 先暫時返回到addPositionAuthority.html
	}
	
	
	
	@PostMapping("/updatePositionAuthority")
	public String updatePositionAuthority(@RequestParam("positionId") Integer positionId,
			@RequestParam("functionIds") List<Integer> functionIds,  ModelMap model) {
		System.out.println("進入updatePositionAuthority方法");
		List<Position> list = positionService.findAllPositions();
		model.addAttribute("positionListData", list);
		// 調用服務層來處理職位和權限的添加
		positionAuthorityService.updatePositionAuthorities(positionId, functionIds);

		// 這裡重定向到成功頁面，根據實際情況可能需要調整
		return "back-end/positionauthority/allPositionAuthority"; // 先暫時返回到addPositionAuthority.html
	}

}
