package com.joyfulresort.fun.authorityfunction.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityFunctionRepository extends JpaRepository<AuthorityFunction, Integer>{
	 // 根據需要，這裡可以添加更多自定義方法
	
	/*在 Spring Data JPA 中，當您創建一個接口繼承自 JpaRepository，Spring 框架會自動為您實現該接口，並生成一個 bean，可以直接在您的應用程序中注入並使用。這是 Spring Data 的一個特點，使得數據訪問層的實現變得非常簡潔和高效。
	 * 
	 */
	
	boolean existsByFunctionName(String functionName);

}
