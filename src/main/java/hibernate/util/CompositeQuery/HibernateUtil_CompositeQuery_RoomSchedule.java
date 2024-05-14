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
import org.hibernate.type.DateType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;

import com.joyfulresort.jia.roomorder.model.RoomOrder;
import com.joyfulresort.jia.roomorderitem.model.RoomOrderItem;
import com.joyfulresort.jia.roomschedule.model.RoomSchedule;
import com.joyfulresort.yu.roomtype.model.RoomType;

public class HibernateUtil_CompositeQuery_RoomSchedule {

	@SuppressWarnings("unchecked")
	public static List<Object[]> getAllByCurDate(Session session) {
		Transaction tx = session.beginTransaction();
		List<Object[]> list = null;
		try {
			String sql = "WITH RECURSIVE dates (v_date) AS " + " ( SELECT CURDATE() " + "   UNION ALL "
					+ "   SELECT v_date + INTERVAL 1 DAY " + "   FROM dates "
					+ "   WHERE v_date + INTERVAL 1 DAY <= ADDDATE(CURDATE(), INTERVAL 2 MONTH) - INTERVAL 1 DAY ) "
					+ " SELECT rt.room_type_id, " + " d.v_date, " + " COUNT(rs.room_schedule_id) as roomtotal, "
					+ " (SELECT COUNT(*) FROM room WHERE room_type_id = rt.room_type_id and room_sale_state = 1 ) - "
					+ " COUNT(rs.room_schedule_id) as remptyroomtotal " + " FROM dates d " + " CROSS JOIN room_type rt "
					+ " LEFT JOIN room_schedule rs on (d.v_date = rs.room_schedule_date AND rt.room_type_id = rs.room_type_id) "
					+ " GROUP BY rt.room_type_id, d.v_date " + " ORDER BY d.v_date, rt.room_type_id; ";

			list = session.createSQLQuery(sql).addScalar("room_type_id", IntegerType.INSTANCE)
					.addScalar("v_date", DateType.INSTANCE).addScalar("roomtotal", LongType.INSTANCE)
					.addScalar("remptyroomtotal", LongType.INSTANCE).list();
			System.err.println("57+"+list);
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

	@SuppressWarnings("unchecked")
	public static List<Object[]> getByCompositeQuery(Map<String, String> map, Session session) {
		Transaction tx = session.beginTransaction();
		List<Object[]> list = null;
		try {
			StringBuilder sql = new StringBuilder();
			sql.append("WITH RECURSIVE dates (v_date) AS ");
			sql.append("( SELECT ? UNION ALL ");
			sql.append("SELECT v_date + INTERVAL 1 DAY FROM dates ");
			sql.append("WHERE v_date + INTERVAL 1 DAY <= ADDDATE(?, INTERVAL 2 MONTH) - INTERVAL 1 DAY ) ");
			sql.append("SELECT rt.room_type_id, d.v_date, COUNT(rs.room_schedule_id) as roomtotal, ");
			sql.append("(SELECT COUNT(*) FROM room WHERE room_type_id = rt.room_type_id and room_sale_state = 1 ) - ");
			sql.append("COUNT(rs.room_schedule_id) as remptyroomtotal FROM dates d ");
			sql.append("CROSS JOIN room_type rt ");
			sql.append(
					"LEFT JOIN room_schedule rs on (d.v_date = rs.room_schedule_date AND rt.room_type_id = rs.room_type_id) ");
//		sql.append("WHERE d.v_date >= ? AND d.v_date <= ? ");
//		sql.append("GROUP BY rt.room_type_id, d.v_date; ");

			Date startQueryDate = null;
			Date endQueryDate = null;
			Integer roomTypeId = 0;

			if (map.containsKey("roomTypeId")) {
				if (map.containsKey("startquerydate") && map.containsKey("endquerydate")) {
					startQueryDate = Date.valueOf(map.get("startquerydate"));
					endQueryDate = Date.valueOf(map.get("endquerydate"));
					roomTypeId = Integer.valueOf(map.get("roomTypeId"));
					sql.append(
							"WHERE d.v_date >= ? AND d.v_date <= ? AND rt.room_type_id = ?  AND d.v_date <= DATE_ADD(CURDATE(), INTERVAL 2 MONTH)");
					sql.append("GROUP BY rt.room_type_id, d.v_date ");
					sql.append("ORDER BY d.v_date, rt.room_type_id; ");
					list = session.createSQLQuery(sql.toString()).addScalar("room_type_id", IntegerType.INSTANCE)
							.addScalar("v_date", DateType.INSTANCE).addScalar("roomtotal", LongType.INSTANCE)
							.addScalar("remptyroomtotal", LongType.INSTANCE).setParameter(1, startQueryDate)
							.setParameter(2, startQueryDate).setParameter(3, startQueryDate)
							.setParameter(4, endQueryDate).setParameter(5, roomTypeId).list();

					return list;
				} else if (map.containsKey("startquerydate") && !map.containsKey("endquerydate")) {
					startQueryDate = Date.valueOf(map.get("startquerydate"));
					roomTypeId = Integer.valueOf(map.get("roomTypeId"));
					sql.append("WHERE rt.room_type_id = ? AND d.v_date <= DATE_ADD(CURDATE(), INTERVAL 2 MONTH)");
					sql.append("GROUP BY rt.room_type_id, d.v_date ");
					sql.append("ORDER BY d.v_date, rt.room_type_id; ");
					list = session.createSQLQuery(sql.toString()).addScalar("room_type_id", IntegerType.INSTANCE)
							.addScalar("v_date", DateType.INSTANCE).addScalar("roomtotal", LongType.INSTANCE)
							.addScalar("remptyroomtotal", LongType.INSTANCE).setParameter(1, startQueryDate)
							.setParameter(2, startQueryDate).setParameter(3, roomTypeId).list();
					System.out.println("104+" + list);
					return list;
				}

			} else {
				if (map.containsKey("startquerydate") && map.containsKey("endquerydate")) {
					startQueryDate = Date.valueOf(map.get("startquerydate"));
					endQueryDate = Date.valueOf(map.get("endquerydate"));
					sql.append(
							"WHERE d.v_date >= ? AND d.v_date <= ? AND d.v_date <= DATE_ADD(CURDATE(), INTERVAL 2 MONTH) ");
					sql.append("GROUP BY rt.room_type_id, d.v_date ");
					sql.append("ORDER BY d.v_date, rt.room_type_id; ");
					list = session.createSQLQuery(sql.toString()).addScalar("room_type_id", IntegerType.INSTANCE)
							.addScalar("v_date", DateType.INSTANCE).addScalar("roomtotal", LongType.INSTANCE)
							.addScalar("remptyroomtotal", LongType.INSTANCE).setParameter(1, startQueryDate)
							.setParameter(2, startQueryDate).setParameter(3, startQueryDate)
							.setParameter(4, endQueryDate).list();

					return list;
				} else if (map.containsKey("startquerydate") && !map.containsKey("endquerydate")) {
					startQueryDate = Date.valueOf(map.get("startquerydate"));
					sql.append("WHERE d.v_date <= DATE_ADD(CURDATE(), INTERVAL 2 MONTH) ");
					sql.append("GROUP BY rt.room_type_id, d.v_date ");
					sql.append("ORDER BY d.v_date, rt.room_type_id; ");
					list = session.createSQLQuery(sql.toString()).addScalar("room_type_id", IntegerType.INSTANCE)
							.addScalar("v_date", DateType.INSTANCE).addScalar("roomtotal", LongType.INSTANCE)
							.addScalar("remptyroomtotal", LongType.INSTANCE).setParameter(1, startQueryDate)
							.setParameter(2, startQueryDate).list();

					return list;
				}
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

	@SuppressWarnings("unchecked")
	public static List<Object[]> getOneByCurDate(Map<String, String> map, Session session) {
		Transaction tx = session.beginTransaction();
		List<Object[]> list = null;
		try {
			String sql = "WITH RECURSIVE dates (v_date) AS " + " ( SELECT CURDATE() " + "   UNION ALL "
					+ "   SELECT v_date + INTERVAL 1 DAY " + "   FROM dates "
					+ "   WHERE v_date + INTERVAL 1 DAY <= ADDDATE(CURDATE(), INTERVAL 2 MONTH) - INTERVAL 1 DAY ) "
					+ " SELECT rt.room_type_id, " + " d.v_date, " + " COUNT(rs.room_schedule_id) as roomtotal, "
					+ " (SELECT COUNT(*) FROM room WHERE room_type_id = rt.room_type_id and room_sale_state = 1 ) - "
					+ " COUNT(rs.room_schedule_id) as remptyroomtotal " + " FROM dates d " + " CROSS JOIN room_type rt "
					+ " LEFT JOIN room_schedule rs on (d.v_date = rs.room_schedule_date AND rt.room_type_id = rs.room_type_id) "
					+ " WHERE rt.room_type_id = ? " + " GROUP BY rt.room_type_id, d.v_date "
					+ " ORDER BY d.v_date, rt.room_type_id; ";

			Integer roomTypeId = Integer.valueOf(map.get("roomTypeId"));
			list = session.createSQLQuery(sql).addScalar("room_type_id", IntegerType.INSTANCE)
					.addScalar("v_date", DateType.INSTANCE).addScalar("roomtotal", LongType.INSTANCE)
					.addScalar("remptyroomtotal", LongType.INSTANCE).setParameter(1, roomTypeId).list();

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