package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import dao.PorderDao;
import model.Porder;
import util.DbConnection;

public class PorderDaoImpl implements PorderDao{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	private static Connection conn=DbConnection.getDb();
	
	@Override
	public int insert(Porder p, Connection conn) {
		String sql = "INSERT INTO porder(member_id, order_no, created_at, order_date, total_amount) " +"VALUES(?,?,?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
			ps.setInt(1, p.getMemberId());
			 
			ps.setString(2, p.getOrderNo());
            
            
            ps.setTimestamp(3, Timestamp.valueOf(p.getCreatedAt())); // 下單時間
            if (p.getOrderDate() != null) {
                ps.setTimestamp(4, Timestamp.valueOf(p.getOrderDate())); // 啟用日(可 null)
            } else {
                ps.setNull(4, Types.TIMESTAMP);
            }
            ps.setInt(5, p.getTotalAmount()); // 先 0，最後再 updateTotal
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new RuntimeException("Insert porder: no generated key");
        } catch (SQLException e) {
            throw new RuntimeException("Insert porder failed", e);
        }
    }
		
	

	

	@Override
	public List<Object[]> selectAllOrderDetails() {
	    String sql = 
	    		 "SELECT " +
	    				  	"    pi.id AS porder_item_id, " +   // <<-新增 porder_item 的 id
	    				 	"    p.created_at, " +            // 下單時間 (第1欄)
	    			        "    p.order_no, " +              // 訂單序號 (第2欄)
	    			        "    p.member_id, " +             // 會員編號 (第3欄)
	    			        "    p.order_date, " +            // 票券啟用日 (第4欄)
	    			        "    pr.ticket_type_id, " +       // 票種 (第5欄)
	    			        "    pr.ticket_period_id, " +     // 票期 (第6欄)
	    			        "    pi.quantity, " +             // 數量 (第7欄)
	    			        "    pi.unit_price " +            // 金額 (第8欄)
	    			        "FROM porder p " +
	    			        "JOIN porder_item pi ON p.id = pi.porder_id " +
	    			        "JOIN product pr ON pi.product_id = pr.id " +
	    			        "ORDER BY p.created_at DESC";


	    List<Object[]> list = new ArrayList<>();

	    try (PreparedStatement ps = conn.prepareStatement(sql);
	         ResultSet rs = ps.executeQuery()) {

	        while (rs.next()) {
	        	Object[] row = new Object[9];
	        	row[0] = rs.getInt("porder_item_id");
	            row[1] = rs.getTimestamp("created_at");    // 下單時間
	            row[2] = rs.getString("order_no");         // 訂單序號
	            row[3] = rs.getInt("member_id");           // 會員編號
	            row[4] = rs.getTimestamp("order_date");    // 票券啟用日
	            row[5] = rs.getInt("ticket_type_id");      // 票種
	            row[6] = rs.getInt("ticket_period_id");    // 票期
	            row[7] = rs.getInt("quantity");            // 數量
	            row[8] = rs.getInt("unit_price");          // 金額

	            list.add(row);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	@Override
	public void updateTotal(int porderId, int total, Connection conn) {
		String sql = "UPDATE porder SET total_amount=? WHERE id=?";
        try (
        	PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, total);
            ps.setInt(2, porderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Update total failed", e);
        }
    }

	@Override
	public List<Porder> getOrdersByMemberId(int memberId) {
	    List<Porder> list = new ArrayList<>();
	    String sql = "SELECT * FROM porder WHERE member_id = ? ORDER BY created_at DESC";

	    try (PreparedStatement ps = conn.prepareStatement(sql)) {
	        ps.setInt(1, memberId);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Porder p = new Porder();
	            p.setId(rs.getInt("id"));
	            p.setMemberId(rs.getInt("member_id"));
	            p.setOrderNo(rs.getString("order_no"));
	            p.setOrderDate(rs.getTimestamp("order_date") != null ? 
	                rs.getTimestamp("order_date").toLocalDateTime() : null);
	            p.setCreatedAt(rs.getTimestamp("created_at") != null ? 
	                rs.getTimestamp("created_at").toLocalDateTime() : null);
	            p.setTotalAmount(rs.getInt("total_amount"));
	            list.add(p);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}

		
}

	


