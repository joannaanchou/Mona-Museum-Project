package service.impl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import dao.impl.PorderDaoImpl;
import model.Porder;
import model.PorderItem;
import service.PorderService;
import util.DbConnection;
import util.OrderNo;
import dao.PorderItemDao;
import dao.impl.PorderItemDaoImpl;
//import service.PorderItemService;



public class PorderServiceImpl implements PorderService{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	
	private static PorderDaoImpl pdi=new PorderDaoImpl();
	private static PorderItemDao itemDao = new PorderItemDaoImpl();
	
	private static Connection conn=DbConnection.getDb();
	


	@Override
	public long placeOrder(Porder order, List<PorderItem> items) {
	    if (order == null) {
	        throw new IllegalArgumentException("order is null");
	    }
	    if (items == null || items.isEmpty()) {
	        throw new IllegalArgumentException("items is empty");
	    }
	    
	    // 一定要有 memberId 
	    if (order.getMemberId() == null) {
	        throw new IllegalArgumentException("order.memberId is null");
	    }
	    
	    if (order.getOrderDate() == null) {
	        throw new IllegalArgumentException("order.orderDate is null");
	    }

	    // 補必要欄位（下單時間 / 訂單編號）
	    if (order.getCreatedAt() == null) {
	        order.setCreatedAt(LocalDateTime.now());
	    }
	    
	    if (order.getOrderNo() == null || order.getOrderNo().trim().isEmpty()) {
	        order.setOrderNo(OrderNo.genOrderNo());
	    }
	    
	    try {
            conn.setAutoCommit(false);

            // 取得自動編號
            long porderId = pdi.insert(order, conn);

            // 準備並寫入明細
            int total = 0;
            int pid = (int) porderId; 

            // 先把每筆的 porderId 與 lineTotal 補上
            for (PorderItem it : items) {
                it.setPorderId(pid);
                if (it.getLineTotal() == null) {
                    it.setLineTotal(calcLineTotal(it.getUnitPrice(), it.getQuantity()));
                }
                total += (it.getLineTotal() != null ? it.getLineTotal() : 0);
            }

            // 以 DAO 批次新增
            itemDao.batchInsert(items, conn);

            // 回寫主檔總額
            pdi.updateTotal(pid, total, conn);
            order.setTotalAmount(total); 

            // 提交交易
            conn.commit();
            return porderId;

        } catch (Exception ex) {
            try { conn.rollback(); } catch (SQLException ignore) {}
            throw new RuntimeException("placeOrder failed", ex);
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException ignore) {}
        }
    }

	

	@Override
	public List<Porder> getOrdersByMemberId(int memberId) {
	    return pdi.getOrdersByMemberId(memberId);
	}
	
	@Override
	public List<Object[]> getAllOrderDetails() {
	    return pdi.selectAllOrderDetails();
	}

	// 工具方法
		private static int calcLineTotal(Integer unitPrice, Integer qty) {
		    int p = (unitPrice == null ? 0 : unitPrice);
		    int q = (qty == null ? 0 : qty);
		    return p * q;
		}




		
	



}
