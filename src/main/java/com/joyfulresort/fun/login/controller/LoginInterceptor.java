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
        String empAccountStr = request.getParameter("empAccount");
        String empPassword = request.getParameter("empPassword");
        String captcha = request.getParameter("captcha");
        
        
        HttpSession session = request.getSession();
        String sessionCaptcha = (String) session.getAttribute("captcha");

    
        if (sessionCaptcha == null || !sessionCaptcha.equalsIgnoreCase(captcha)) {
            response.sendRedirect("/loginfailedCaptcha");
            return false;
        }
        

        if (!empAccountStr.matches("\\d+")) {
            response.sendRedirect("/loginfailed"); 
            return false;
        }


        Integer empAccount = Integer.valueOf(empAccountStr); 
        Employee employee = employeeService.getOneEmp(empAccount);

   
        if (employee != null && employee.getEmpPassword().equals(empPassword)) {
          
            if (Boolean.FALSE.equals(employee.getEmpState())) {
                response.sendRedirect("/loginfailedNoAcess");
                return false;
            }
          
            return true;
        } else {
         
            response.sendRedirect("/loginfailed");
            return false;
        }
    }
    
    
    
    

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
  
        Integer empAccount = Integer.valueOf(request.getParameter("empAccount"));

        Employee employees = employeeService.getOneEmp(empAccount);

        HttpSession session = request.getSession();
        session.setAttribute("employees", employees);
        
        

        

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
       
    }
}
