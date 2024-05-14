package hibernate.util.CompositeQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.joyfulresort.fun.emp.model.Employee;

public class HibernateUtil_CompositeQuery {

//	
//	// 複合查詢
//		@Override
//		public List<Employee> getByCompositeQuery(Map<String, String> map) {
//			if (map.size() == 0)
//				return getAll();
//			CriteriaBuilder builder = getSession().getCriteriaBuilder();
//			CriteriaQuery<Employee> criteria = builder.createQuery(Employee.class);
//			Root<Employee> root = criteria.from(Employee.class);
//
//			List<Predicate> predicates = new ArrayList<>();
//
//			// 員工編號查詢
//			if (map.containsKey("empno")) {
//				int empno = Integer.parseInt(map.get("empno"));
//				// root.get("empno") 來自資料庫的欄位 後面的empno是來自jsp裡面name標籤的value值
//				predicates.add(builder.equal(root.get("empno"), empno));
//			}
//
//			// 職位 ID 查詢
//			if (map.containsKey("positionId")) {
//				int positionId = Integer.parseInt(map.get("positionId"));
//				// root.get("position") 這邊的position是永續類別裡的實體變數名稱，再利用position永續類別取的positionId
//				predicates.add(builder.equal(root.get("position").get("positionId"), positionId));
//			}
//
//			// 員工名字模糊查詢
//			if (map.containsKey("ename")) {
//				String ename = map.get("ename");
//				predicates.add(builder.like(root.get("empName"), "%" + ename + "%"));
//			}
//
//			
//			// 入職日期範圍查詢
//			if (map.containsKey("starthiredate") && map.containsKey("endhiredate")) {
//			    LocalDate start = LocalDate.parse(map.get("starthiredate"));
//			    LocalDate end = LocalDate.parse(map.get("endhiredate"));
//			    predicates.add(builder.between(root.get("hiredate"), start, end));
//			} else if (map.containsKey("starthiredate")) {
//			    LocalDate start = LocalDate.parse(map.get("starthiredate"));
//			    predicates.add(builder.greaterThanOrEqualTo(root.get("hiredate"), start));
//			} else if (map.containsKey("endhiredate")) {
//			    LocalDate end = LocalDate.parse(map.get("endhiredate"));
//			    predicates.add(builder.lessThanOrEqualTo(root.get("hiredate"), end));
//			}
//			
//			
//			
//
//			// 員工狀態查詢
//			if (map.containsKey("empState")) {
//				Boolean empState = Boolean.parseBoolean(map.get("empState"));
//				predicates.add(builder.equal(root.get("empState"), empState));
//			}
//
//			/*
//			 * criteria.where() 方法用於設定 Criteria 查詢的 where 條件。 builder.and()
//			 * 方法是用來組合多個查詢條件（Predicate）的邏輯與（AND）操作。 這允許將多個條件結合成一個條件表達式，只有當所有條件同時滿足時，記錄才會被選取。
//			 */
//			criteria.where(builder.and(predicates.toArray(new Predicate[predicates.size()])));
//			criteria.orderBy(builder.asc(root.get("empno")));
//			TypedQuery<Employee> query = getSession().createQuery(criteria);
//
//			// getResultList() 方法執行查詢並返回符合條件的所有實體列表。在這個案例中，返回的是 Employee
//			// 類型的對象列表，這些對象代表數據庫中的員工紀錄。
//			return query.getResultList();
//
//		}

	
}
