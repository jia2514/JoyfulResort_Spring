package com.joyfulresort.fun.authorityfunction.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joyfulresort.fun.authorityfunction.model.AuthorityFunction;
import com.joyfulresort.fun.authorityfunction.model.AuthorityFunctionService;
import com.joyfulresort.fun.emp.model.EmployeeService;

//@RestController
@Controller
public class AuthorityFunctionController {
	
	@Autowired
	AuthorityFunctionService authorityFunctionService;
	
	
	@Autowired
	EmployeeService employeeService;
	
	@PostMapping("/AuthorityFunctionController/insert")
    public String insertAuthorityFunction(@Valid AuthorityFunction authorityFunction, BindingResult result, ModelMap model, @RequestParam(defaultValue = "0") int page) {
        if (result.hasErrors()) {
            Pageable pageable = PageRequest.of(page, 10);
            Page<AuthorityFunction> authorityFunctionPage = authorityFunctionService.findAll(pageable);
            model.addAttribute("authorityFunctionPage", authorityFunctionPage);
            return "back-end/authorityfunction/listAllAuthorityFunction";
        }

        // 檢查是否存在相同的功能權限名稱
        if (authorityFunctionService.existsByName(authorityFunction.getFunctionName())) {
            result.rejectValue("functionName", "error.authorityFunction", "已存在相同的功能權限名稱。");
            Pageable pageable = PageRequest.of(page, 10);
            Page<AuthorityFunction> authorityFunctionPage = authorityFunctionService.findAll(pageable);
            model.addAttribute("authorityFunctionPage", authorityFunctionPage);
            return "back-end/authorityfunction/listAllAuthorityFunction";
        }

        authorityFunctionService.addAuthorityFunction(authorityFunction);
        return "redirect:/authorityfunction/listAllAuthorityFunction"; // 確保這是正確的視圖名稱
    }
	
	
	
	@PostMapping("/AuthorityFunctionController/update")
	public String updateAuthorityFunction(@Valid AuthorityFunction authorityFunction, BindingResult result, ModelMap model, @RequestParam(defaultValue = "0") int page) {
	    System.out.println("修改測試點1");
	    System.out.println(authorityFunction.getFunctionId());
	    
		if (result.hasErrors()) {
	        Pageable pageable = PageRequest.of(page, 10);
	        Page<AuthorityFunction> authorityFunctionPage = authorityFunctionService.findAll(pageable);
	        model.addAttribute("authorityFunctionPage", authorityFunctionPage);
	        return "back-end/authorityfunction/listAllAuthorityFunction";
	    }

	    // 檢查是否存在相同的功能權限名稱，並確保ID不同以允許更新當前記錄
	    AuthorityFunction existingFunction = authorityFunctionService.getOneAuthorityFunction(authorityFunction.getFunctionId());
	    if (existingFunction != null && !existingFunction.getFunctionId().equals(authorityFunction.getFunctionId())) {
	        result.rejectValue("functionName", "error.authorityFunction", "已存在相同的功能權限名稱。");
	        Pageable pageable = PageRequest.of(page, 10);
	        Page<AuthorityFunction> authorityFunctionPage = authorityFunctionService.findAll(pageable);
	        model.addAttribute("authorityFunctionPage", authorityFunctionPage);
	        return "back-end/authorityfunction/listAllAuthorityFunction";
	    }

	    authorityFunctionService.updateAuthorityFunction(authorityFunction);
	    return "redirect:/authorityfunction/listAllAuthorityFunction"; // 確保這是正確的視圖名稱
	}

	
	@PostMapping("/AuthorityFunctionController/delete")
	public String deleteAuthorityFunction(@RequestParam("functionId") Integer functionId) {
	    // 檢查 functionId 是否有效，如果無效可以執行相應的處理（如返回錯誤頁面或其他操作）
	    if (functionId == null) {
	        // 返回錯誤頁面或其他操作
	    }

	    // 執行刪除操作
	    authorityFunctionService.deleteAuthorityFunctionById(functionId);

	    return "redirect:/authorityfunction/listAllAuthorityFunction"; // 確保這是正確的視圖名稱
	}

	

	
	 @GetMapping("/AuthorityFunctionController/getAll")
	    @ResponseBody
	    public List<AuthorityFunction> getAllAuthorityFunctions() {
	        return authorityFunctionService.getAll();
	    }



	 
	 
	 




}



