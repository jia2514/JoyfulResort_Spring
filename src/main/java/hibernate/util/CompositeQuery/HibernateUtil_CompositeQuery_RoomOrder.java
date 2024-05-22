/*
 *  1. 萬用複合查詢-可由客戶端隨意增減任何想查詢的欄位
 *  2. 為了避免影響效能:
 *        所以動態產生萬用SQL的部份,本範例無意採用MetaData的方式,也只針對個別的Table自行視需要而個別製作之
 * */

package hibernate.util.CompositeQuery;

import java.sql.Date;
//import hibernate.util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Query; //Hibernate 5 開始 取代原 org.hibernate.Query 介面
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery; //Hibernate 5.2 開始 取代原 org.hibernate.Criteria 介面
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.yu.roomtype.model.RoomType;

public class HibernateUtil_CompositeQuery_RoomOrder {

	@SuppressWarnings("unchecked")
	public static List<RoomOrder> getAllC(Map<String, String> map, Session session) {

//		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		List<RoomOrder> list = null;
		try {
			// 【●創建 CriteriaBuilder】
			CriteriaBuilder builder = session.getCriteriaBuilder();
			// 【●創建 CriteriaQuery】
			CriteriaQuery<RoomOrder> criteriaQuery = builder.createQuery(RoomOrder.class);
			// 【●創建 Root】
			Root<RoomOrder> root = criteriaQuery.from(RoomOrder.class);

			List<Predicate> predicates = new ArrayList<Predicate>();

			Path<Date> checkInDate = root.get("checkInDate");
			if (map.containsKey("startcheckindate") && map.containsKey("endcheckindate"))
				predicates.add(builder.between(checkInDate, Date.valueOf(map.get("startcheckindate")),
						Date.valueOf(map.get("endcheckindate"))));

			
			Path<Date> checkOutDate = root.get("checkOutDate");
			if (map.containsKey("startcheckoutdate") && map.containsKey("endcheckoutdate"))
				predicates.add(builder.between(checkOutDate, Date.valueOf(map.get("startcheckoutdate")),
						Date.valueOf(map.get("endcheckoutdate"))));

			Path<Date> orderDate = root.get("orderDate");
			if (map.containsKey("startorderdate") && map.containsKey("endorderdate"))
				predicates.add(builder.between(orderDate, Date.valueOf(map.get("startorderdate")),
						Date.valueOf(map.get("endorderdate"))));

			for (Map.Entry<String, String> row : map.entrySet()) {
				Path<String> memberName = root.get("member").get("memberName");
				if ("membername".equals(row.getKey())) {
					predicates.add(builder.like(memberName, "%" + row.getValue() + "%"));
				}

				if ("memberid".equals(row.getKey())) {
					predicates.add(builder.equal(root.get("member").get("memberId"), row.getValue()));
				}
				
				if ("roomorderstate".equals(row.getKey())) {
					predicates.add(builder.equal(root.get("roomOrderState"), row.getValue()));
					System.out.println("roomorderstate" + row.getValue());
				}
				
				if ("refundstate".equals(row.getKey())) {
					predicates.add(builder.equal(root.get("refundState"), row.getValue()));
					System.out.println("refundstate" + row.getValue());
				}
				

				if ("startcheckindate".equals(row.getKey())) {
					if (!map.containsKey("endcheckindate"))
						predicates.add(builder.greaterThanOrEqualTo(checkInDate, Date.valueOf(row.getValue())));
				}

				System.out.println("114" + predicates);

				if ("endcheckindate".equals(row.getKey())) {
					if (!map.containsKey("startcheckindate"))
						predicates.add(builder.lessThanOrEqualTo(checkInDate, Date.valueOf(row.getValue())));
				}

				if ("startcheckoutdate".equals(row.getKey())) {
					if (!map.containsKey("endcheckoutdate"))
						predicates.add(builder.greaterThanOrEqualTo(checkOutDate, Date.valueOf(row.getValue())));
				}

				if ("endcheckoutdate".equals(row.getKey())) {
					if (!map.containsKey("startcheckoutdate"))
						predicates.add(builder.lessThanOrEqualTo(checkOutDate, Date.valueOf(row.getValue())));
				}

				if ("startorderdate".equals(row.getKey())) {
					if (!map.containsKey("endorderdate"))
						predicates.add(builder.greaterThanOrEqualTo(orderDate, Date.valueOf(row.getValue())));
				}

				if ("endorderdate".equals(row.getKey())) {
					if (!map.containsKey("startorderdate"))
						predicates.add(builder.lessThanOrEqualTo(orderDate, Date.valueOf(row.getValue())));
				}


			}

			System.out.println("predicates" + predicates);
			root.fetch("member", JoinType.LEFT); // 使用 LEFT JOIN 來加載 member

			criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
			criteriaQuery.orderBy(builder.asc(root.get("roomOrderId")));
			// 【●最後完成創建 javax.persistence.Query●】
			TypedQuery<RoomOrder> query = session.createQuery(criteriaQuery); // javax.persistence.Query; //Hibernate 5
																				// 開始 取代原
			// org.hibernate.Query 介面
			list = query.getResultList();
			for(RoomOrder roomOrder:list) {
				System.out.println("125" + roomOrder.getMember());
				System.out.println("126" + roomOrder.getRoomOrderItems());
			}
			
			tx.commit();
		} catch (RuntimeException ex) {
			if (tx != null)
				tx.rollback();
			throw ex; // System.out.println(ex.getMessage());
		} finally {
			session.close();
			// HibernateUtil.getSessionFactory().close();
		}

		return list;
	}
}