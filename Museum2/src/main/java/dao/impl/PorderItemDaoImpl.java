package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dao.PorderItemDao;
import model.PorderItem;
import util.DbConnection;

public class PorderItemDaoImpl implements PorderItemDao{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static Connection conn=DbConnection.getDb();
	
	
	
	@Override
	public void batchInsert(List<PorderItem> items, Connection conn) {
		String sql = "INSERT INTO porder_item(porder_id, product_id, quantity, unit_price, line_total) " +
                "VALUES(?,?,?,?,?)";
		try (PreparedStatement ps = conn.prepareStatement(sql)) {
			for (PorderItem it : items) {
				ps.setInt(1, it.getPorderId());
				ps.setInt(2, it.getProductId());
				ps.setInt(3, it.getQuantity());
				ps.setInt(4, it.getUnitPrice());
				ps.setInt(5, it.getLineTotal());
				ps.addBatch();
			}
       ps.executeBatch();
		} catch (SQLException e) {
			throw new RuntimeException("batchInsert porder_item failed", e);
		}
}

	@Override
	public List<PorderItem> selectByPorderId(int porderId) {
        String sql = "SELECT porder_id, product_id, quantity, unit_price, line_total " +
                "FROM porder_item WHERE porder_id=?";
   List<PorderItem> list = new ArrayList<>();
   try (
        PreparedStatement ps = conn.prepareStatement(sql)) {
       ps.setLong(1, porderId);
       try (ResultSet rs = ps.executeQuery()) {
           while (rs.next()) {
               PorderItem it = new PorderItem();
               it.setPorderId(rs.getInt("porder_id"));
               it.setProductId(rs.getInt("product_id"));
               it.setQuantity(rs.getInt("quantity"));
               it.setUnitPrice(rs.getInt("unit_price"));
               it.setLineTotal(rs.getInt("line_total"));
               list.add(it);
           }
       }
       return list;
   } catch (SQLException e) {
       throw new RuntimeException("selectByPorderId failed", e);
   }
}


	@Override
    public Integer findPorderIdByItemId(int itemId) {
        String sql = "SELECT porder_id FROM porder_item WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("findPorderIdByItemId failed", e);
        }
    }

	@Override
    public int sumLineTotalByPorderId(int porderId) {
        String sql = "SELECT COALESCE(SUM(line_total), 0) FROM porder_item WHERE porder_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, porderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
                return 0;
            }
        } catch (SQLException e) {
            throw new RuntimeException("sumLineTotalByPorderId failed", e);
        }
    }

	
	@Override
    public void update(PorderItem item) {
		if (item == null) {
	        throw new IllegalArgumentException("porder_item.update: item 為 null");
	    }
	    int id = item.getId(); // primitive int
	    if (id <= 0) {
	        throw new IllegalArgumentException("porder_item.update: 非法的 item.id=" + id);
	    }

        // 若呼叫端沒算 lineTotal，這裡幫忙補
        Integer unit = item.getUnitPrice() == null ? 0 : item.getUnitPrice();
        Integer qty  = item.getQuantity()  == null ? 0 : item.getQuantity();
        Integer line = (item.getLineTotal() != null) ? item.getLineTotal() : unit * qty;

        String sql = "UPDATE porder_item " +
                     "SET product_id=?, quantity=?, unit_price=?, line_total=? " +
                     "WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, item.getProductId());
            ps.setInt(2, qty);
            ps.setInt(3, unit);
            ps.setInt(4, line);
            ps.setInt(5, item.getId());

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("porder_item.update: 沒有更新到任何資料, id=" + item.getId());
            }
        } catch (SQLException e) {
            throw new RuntimeException("porder_item.update failed", e);
        }
    }
	
	@Override
	public void deleteByPorderId(int porderId, Connection conn) {
		String sql = "DELETE FROM porder_item WHERE porder_id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, porderId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("deleteByPorderId failed", e);
        }
    }
	
	@Override
    public void deleteById(int id) {
        String sql = "DELETE FROM porder_item WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new RuntimeException("porder_item.deleteById: 沒有刪到任何資料, id=" + id);
            }
        } catch (SQLException e) {
            throw new RuntimeException("porder_item.deleteById failed", e);
        }
    }

}


