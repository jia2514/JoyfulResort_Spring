package com.joyfulresort.fun.positionauthority.model;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionAuthorityRepository extends JpaRepository<PositionAuthority, PositionAuthority.CompositeDetail>{
//第一個參數 PositionAuthority：這是您希望這個存儲庫管理的實體類型。
/*通常情況下，當實體有一個單一的主鍵時，如 Integer 或 Long 等，JpaRepository 的第二個參數會是這個主鍵的型態。但是，當您的實體使用複合主鍵時，情況就會有所不同。
 * 
 * 在使用複合主鍵的情況下，JpaRepository 的第二個參數應該是一個表示這個複合主鍵的類。在您的例子中，PositionAuthority 類有一個複合主鍵，它由兩個字段 position_id 和 function_id 組成。因此，您需要一個專門的類來表示這個複合主鍵。
 */
	
	 void deleteByPositionId(Integer positionId);
}
