package service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.PorderDao;
import dao.PorderItemDao;
import dao.impl.PorderDaoImpl;
import dao.impl.PorderItemDaoImpl;
import model.PorderItem;
import service.PorderItemService;
import util.DbConnection;

public class PorderItemServiceImpl implements PorderItemService {

    private static PorderItemDao dao = new PorderItemDaoImpl();
    private PorderDao  porderDao = new PorderDaoImpl();
    private static Connection conn = DbConnection.getDb();

   
    
    @Override
    public void batchInsert(List<PorderItem> items, Connection conn) {
        if (items == null || items.isEmpty()) return;
        dao.batchInsert(items, conn);
    }

    @Override
    public List<PorderItem> getItemsByPorderId(int porderId) {
        return dao.selectByPorderId(porderId);
    }

    

	
		@Override
	    public void update(PorderItem item) {
	        if (item == null) throw new IllegalArgumentException("item 為 null");
	        int id = item.getId();
	        if (id <= 0) throw new IllegalArgumentException("非法的 item.id=" + id);

	        // 1) 先找主檔 id
	        Integer porderId = dao.findPorderIdByItemId(id);
	        if (porderId == null) {
	            throw new RuntimeException("找不到 porder_id，itemId=" + id);
	        }

	        // 2) 更新明細
	        dao.update(item);

	        // 3) 重算總額並回寫主檔
	        int total = dao.sumLineTotalByPorderId(porderId);

	        try (Connection conn = DbConnection.getDb()) {  //這邊要改
	            porderDao.updateTotal(porderId, total, conn);
	        } catch (SQLException e) {
	            throw new RuntimeException("更新 porder.total_amount 失敗", e);
	        }
		
	}

	@Override
	public void deleteById(int id) {
		// 1) 先找主檔 id
        Integer porderId = dao.findPorderIdByItemId(id);
        if (porderId == null) {
            throw new RuntimeException("找不到 porder_id，itemId=" + id);
        }

        // 2) 刪除明細
        dao.deleteById(id);
        
        // 3) 重算總額並回寫主檔
        int total = dao.sumLineTotalByPorderId(porderId);

        try (Connection conn = DbConnection.getDb()) {
            porderDao.updateTotal(porderId, total, conn);
        } catch (SQLException e) {
            throw new RuntimeException("更新 porder.total_amount 失敗", e);
        }
  
		
	}
}
