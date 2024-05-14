package hibernate.util.CompositeQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.joyfulresort.fun.emp.model.Employee;

public class HibernateUtil_CompositeQuery_Emp3 {



	// 原版測試整合gpt
	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder,Root<Employee> root,String columnName,  String value) {
		if (value == null || value.trim().isEmpty()) {
	        return null; // 直接返回null，避免創建基於空值的Predicate
	    }
		
		Predicate predicate = null;
		
		try {
			if("empno".equals(columnName)) {
				predicate = builder.equal(root.get(columnName), Integer.valueOf(value.trim()));
			}else if("positionId".equals(columnName)){
				predicate = builder.equal(root.get("position").get("positionId"), Integer.valueOf(value));
			}else if("empName".equals(columnName)) {
				predicate = builder.like(root.get(columnName), "%" +  value.trim() + "%");
				
			}else if("empState".equals(columnName)) {
				predicate = builder.equal(root.get(columnName), Boolean.parseBoolean(value.trim()));
			}
		} catch(NumberFormatException e) {
			System.err.println("Invalid format for " + columnName + " with value: " + value);
		}
		return predicate;
		
	}

	
	public static List<Employee> getAllC(Map<String, String[]> map, Session session) {
	    Transaction tx = session.beginTransaction();
	    List<Employee> list = null;
	    try {
	        CriteriaBuilder builder = session.getCriteriaBuilder();
	        CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
	        Root<Employee> root = criteriaQuery.from(Employee.class);

	        List<Predicate> predicates = new ArrayList<>();

	        // 處理部分或完整日期範圍
	        String startStr = map.get("starthiredate") != null ? map.get("starthiredate")[0] : null;
	        String endStr = map.get("endhiredate") != null ? map.get("endhiredate")[0] : null;
	        if (startStr != null && !startStr.isEmpty() && endStr != null && !endStr.isEmpty()) {
	            LocalDate start = LocalDate.parse(startStr);
	            LocalDate end = LocalDate.parse(endStr);
	            Predicate datePredicate = builder.between(root.get("hiredate"), start, end);
	            predicates.add(datePredicate);
	        } else if (startStr != null && !startStr.isEmpty()) {
	            LocalDate start = LocalDate.parse(startStr);
	            Predicate datePredicate = builder.greaterThanOrEqualTo(root.get("hiredate"), start);
	            predicates.add(datePredicate);
	        } else if (endStr != null && !endStr.isEmpty()) {
	            LocalDate end = LocalDate.parse(endStr);
	            Predicate datePredicate = builder.lessThanOrEqualTo(root.get("hiredate"), end);
	            predicates.add(datePredicate);
	        }

	        // 處理其他字段
	        for (String key : map.keySet()) {
	            if (!key.equals("starthiredate") && !key.equals("endhiredate")) {
	                String[] values = map.get(key);
	                if (values != null && values.length > 0 && !values[0].isEmpty()) {
	                    Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, values[0]);
	                    if (predicate != null) {
	                        predicates.add(predicate);
	                    }
	                }
	            }
	        }

	        criteriaQuery.where(predicates.toArray(new Predicate[0]));
	        criteriaQuery.orderBy(builder.asc(root.get("empno")));
	        Query query = session.createQuery(criteriaQuery);
	        list = query.getResultList();

	        tx.commit();
	    } catch (RuntimeException ex) {
	        if (tx != null) {
	            tx.rollback();
	        }
	        throw ex;
	    } finally {
	        session.close();
	    }

	    return list;
	}

	
	
	
	
	//日期起始和結束成功版本
//	public static List<Employee> getAllC(Map<String, String[]> map, Session session) {
//	    Transaction tx = session.beginTransaction();
//	    List<Employee> list = null;
//	    try {
//	        CriteriaBuilder builder = session.getCriteriaBuilder();
//	        CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
//	        Root<Employee> root = criteriaQuery.from(Employee.class);
//
//	        List<Predicate> predicates = new ArrayList<>();
//
//	        // 處理日期範圍
//	        String startStr = map.get("starthiredate") != null ? map.get("starthiredate")[0] : null;
//	        String endStr = map.get("endhiredate") != null ? map.get("endhiredate")[0] : null;
//	        if (startStr != null && !startStr.isEmpty() && endStr != null && !endStr.isEmpty()) {
//	            LocalDate start = LocalDate.parse(startStr);
//	            LocalDate end = LocalDate.parse(endStr);
//	            Predicate datePredicate = builder.between(root.get("hiredate"), start, end);
//	            predicates.add(datePredicate);
//	        }
//
//	        
//	        
//	        
//	        // 處理其他字段
//	        for (String key : map.keySet()) {
//	            if (!key.equals("starthiredate") && !key.equals("endhiredate")) {
//	                String[] values = map.get(key);
//	                if (values != null && values.length > 0 && !values[0].isEmpty()) {
//	                    Predicate predicate = get_aPredicate_For_AnyDB(builder, root, key, values[0]);
//	                    if (predicate != null) {
//	                        predicates.add(predicate);
//	                    }
//	                }
//	            }
//	        }
//
//	        criteriaQuery.where(predicates.toArray(new Predicate[0]));
//	        criteriaQuery.orderBy(builder.asc(root.get("empno")));
//	        Query query = session.createQuery(criteriaQuery);
//	        list = query.getResultList();
//
//	        tx.commit();
//	    } catch (RuntimeException ex) {
//	        if (tx != null) {
//	            tx.rollback();
//	        }
//	        throw ex;
//	    } finally {
//	        session.close();
//	    }
//
//	    return list;
//	}


//	//原版
//	public static Predicate get_aPredicate_For_AnyDB(CriteriaBuilder builder,Root<Employee> root,String columnName,  String value) {
//		Predicate predicate = null;
//		
//		if("empno".equals(columnName)) {
//			predicate = builder.equal(root.get(columnName), Integer.valueOf(value));
//		}else if("positionId".equals(columnName)){
//			predicate = builder.equal(root.get("position").get("positionId"), Integer.valueOf(value));
//		}else if("empName".equals(columnName)) {
//			predicate = builder.like(root.get(columnName), "%" + value + "%");
//			
//		}else if("empState".equals(columnName)) {
//			predicate = builder.equal(root.get(columnName), Boolean.parseBoolean(value));
//		}
//		
//		return predicate;
//		
//	}

//	

//	//原版
//	@SuppressWarnings("unchecked")
//	public static List<Employee> getAllC(Map<String, String[]> map, Session session) {
//
//
//		Transaction tx = session.beginTransaction();
//		List<Employee> list = null;
//		try {
//			// 【●創建 CriteriaBuilder】
//			CriteriaBuilder builder = session.getCriteriaBuilder();
//			// 【●創建 CriteriaQuery】
//			CriteriaQuery<Employee> criteriaQuery = builder.createQuery(Employee.class);
//			// 【●創建 Root】
//			Root<Employee> root = criteriaQuery.from(Employee.class);
//
//			List<Predicate> predicateList = new ArrayList<Predicate>();
//			
//			Set<String> keys = map.keySet();
//			int count = 0;
//			for (String key : keys) {
//				String value = map.get(key)[0];
//				if (value != null && value.trim().length() != 0 && !"action".equals(key)) {
//					count++;
//					predicateList.add(get_aPredicate_For_AnyDB(builder, root, key, value.trim()));
//					System.out.println("有送出查詢資料的欄位數count = " + count);
//				}
//			}
//			System.out.println("predicateList.size()="+predicateList.size());
//			criteriaQuery.where(predicateList.toArray(new Predicate[predicateList.size()]));
//			criteriaQuery.orderBy(builder.asc(root.get("empno")));
//			// 【●最後完成創建 javax.persistence.Query●】
//			Query query = session.createQuery(criteriaQuery); //javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面
//			list = query.getResultList();
//
//			tx.commit();
//		} catch (RuntimeException ex) {
//			if (tx != null)
//				tx.rollback();
//			throw ex; // System.out.println(ex.getMessage());
//		} finally {
//			session.close();
//			// HibernateUtil.getSessionFactory().close();
//		}
//
//		return list;
//	}

}
