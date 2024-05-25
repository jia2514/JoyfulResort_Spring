package com.joyfulresort.fun.emp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.joyfulresort.fun.emp.model.EmployeeService;

@Controller
public class PasswordResetController {

    @Autowired
    private EmployeeService employeeService;

   

   

//    @GetMapping("/passwordReset/reset-password")
//    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
//        model.addAttribute("token", token);
//        return "resetPasswordForm"; // 返回顯示新密碼表單的視圖
//    }

    @PostMapping("/passwordReset/reset-password")
    public String processResetPassword(@RequestParam("token") String token, @RequestParam("newPassword") String newPassword, Model model) {
        boolean result = employeeService.resetPassword(token, newPassword);
        if (result) {
            model.addAttribute("message", "您的密碼已成功重設！");
            System.out.println("測試修改密碼1");
            System.out.println("message: " + model.getAttribute("message"));
        } else {
            model.addAttribute("error", "無法重設密碼。請確保鏈接有效。");
            System.out.println("測試修改密碼2");
            System.out.println("error: " + model.getAttribute("error"));
        }
        System.out.println("測試修改密碼3");
        return "back-end/resetPasswordForm";
    }



    //  verifyEmployee 方法
    @PostMapping("/passwordReset/verify-employee")
    @ResponseBody
    public boolean verifyEmployee(@RequestParam("empAccount") Integer empAccount, @RequestParam("empName") String empName, @RequestParam("positionName") String positionName) {
        return employeeService.verifyEmployee(empAccount, empName, positionName);
    }
    
    
    //  send-password 方法
    @PostMapping("/passwordReset/send-password")
    @ResponseBody
    public String sendPasswordResetLink(@RequestParam("empEmail") String empEmail, @RequestParam("empAccount") Integer empAccount) {
        boolean result = employeeService.sendPasswordResetLink(empEmail, empAccount);
        return result ? "重設密碼的鏈接已發送到您的信箱！" : "無法發送重設密碼的鏈接。請檢查您的員工帳號和信箱。";
    }
}
