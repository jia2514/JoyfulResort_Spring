package com.joyfulresort.fun.login.controller;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.joyfulresort.fun.emp.model.Employee;
import com.joyfulresort.fun.emp.model.EmployeeService;
import com.joyfulresort.fun.positionauthority.model.PositionAuthority; 

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private EmployeeService employeeService;

 
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // 從請求中獲取員工帳號和密碼
        String empAccountStr = request.getParameter("empAccount");
        String empPassword = request.getParameter("empPassword");
//        String captcha = request.getParameter("captcha");
//        
//        
//        HttpSession session = request.getSession();
//        String sessionCaptcha = (String) session.getAttribute("captcha");
//
//        // 檢查驗證碼是否正確
//        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
//            response.sendRedirect("/loginfailedCaptcha");
//            return false;
//        }
        
        // 檢查員工帳號是否只包含數字
        if (!empAccountStr.matches("\\d+")) {
            // 如果員工帳號不只數字，重定向返回错误信息
            response.sendRedirect("/loginfailed"); // 
            return false;
        }

        // 轉換員工照號為整數
        Integer empAccount = Integer.valueOf(empAccountStr);

        // 根據員工帳號查詢員工信息
        Employee employee = employeeService.getOneEmp(empAccount);

     // 檢查員工信息是否存在且密碼是否匹配
        if (employee != null && employee.getEmpPassword().equals(empPassword)) {
            // 檢查員工是否為離職狀態
            if (Boolean.FALSE.equals(employee.getEmpState())) {
                // 如果員工是離職狀態，重定向返回錯誤信息
                response.sendRedirect("/loginfailedNoAcess");
                return false;
            }
            // 員工驗證通過，允許繼續執行後續操作
            return true;
        } else {
            // 員工驗證失敗，重定向到登錄頁面或返回錯誤信息
            response.sendRedirect("/loginfailed");
            return false;
        }
    }
    
    
    
    

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
        // 獲取登入的員工帳號
        Integer empAccount = Integer.valueOf(request.getParameter("empAccount"));

        // 根據員工帳號查詢員工信息，包括圖片
        Employee employees = employeeService.getOneEmp(empAccount);

        // 將員工信息放入 Session 中
        HttpSession session = request.getSession();
        session.setAttribute("employees", employees);
        
        
//     // 檢查 session 中是否成功存儲了員工信息
//        Employee storedEmployee = (Employee) session.getAttribute("employees");
//        if (storedEmployee != null) {
//            // 如果成功存儲了員工信息，可以在控制台中打印一條消息以表示成功
//            System.out.println("Employee information stored in session: " + storedEmployee);
//        } else {
//            // 如果員工信息為空，可以在控制台中打印一條消息以表示失敗
//            System.out.println("Failed to store employee information in session.");
//        }
        
     // 將員工的權限放入 Session 中
        Set<Integer> authorityFunctions = new HashSet<>();
        for (PositionAuthority authority : employees.getPosition().getAuthorities()) {
            authorityFunctions.add(authority.getAuthorityFunction().getFunctionId());
//            System.out.println("登入測試");
//            System.out.println(authority);
        }
        session.setAttribute("authorityFunctions", authorityFunctions);
        
    }

    
    
    

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
            Exception ex) throws Exception {
        // 在請求完成後執行任何清理操作
    }
}
