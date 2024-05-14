package hibernate.util.CompositeQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.joyfulresort.fun.emp.model.Employee;

@Component
public class EmployeeSpecifications {

	
	/*
	 * 
這個 EmployeeSpecifications 類別負責構建用於查詢員工的條件。通過 compositeQuery 方法，你可以根據傳入的條件 criteriaMap 創建一個 Specification<Employee> 對象
，該對象包含了符合條件的員工查詢條件。
	 */
    public static Specification<Employee> compositeQuery(final Map<String, String> criteriaMap) {
        return new Specification<Employee>() {
            /**
			 * 
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
            public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
                List<Predicate> predicates = new ArrayList<>();

                if (criteriaMap.containsKey("empno")) {
                    int empno = Integer.parseInt(criteriaMap.get("empno"));
                    predicates.add(builder.equal(root.get("empno"), empno));
                }

                if (criteriaMap.containsKey("positionId")) {
                    int positionId = Integer.parseInt(criteriaMap.get("positionId"));
                    predicates.add(builder.equal(root.get("position").get("positionId"), positionId));
                }

                if (criteriaMap.containsKey("ename")) {
                    String ename = criteriaMap.get("ename");
                    predicates.add(builder.like(root.get("empName"), "%" + ename + "%"));
                }

                if (criteriaMap.containsKey("starthiredate") && criteriaMap.containsKey("endhiredate")) {
                    LocalDate start = LocalDate.parse(criteriaMap.get("starthiredate"));
                    LocalDate end = LocalDate.parse(criteriaMap.get("endhiredate"));
                    predicates.add(builder.between(root.get("hiredate"), start, end));
                } else if (criteriaMap.containsKey("starthiredate")) {
                    LocalDate start = LocalDate.parse(criteriaMap.get("starthiredate"));
                    predicates.add(builder.greaterThanOrEqualTo(root.get("hiredate"), start));
                } else if (criteriaMap.containsKey("endhiredate")) {
                    LocalDate end = LocalDate.parse(criteriaMap.get("endhiredate"));
                    predicates.add(builder.lessThanOrEqualTo(root.get("hiredate"), end));
                }

                if (criteriaMap.containsKey("empState")) {
                    Boolean empState = Boolean.parseBoolean(criteriaMap.get("empState"));
                    predicates.add(builder.equal(root.get("empState"), empState));
                }

                return builder.and(predicates.toArray(new Predicate[predicates.size()]));
                
                //edicates.toArray(new Predicate[predicates.size()]) 這個部分僅僅是將多個 Predicate 整合成一個複合條件的 Predicate。這個整合的 Predicate 還沒有真正地被使用或執行。
            }
        };
    }
}
