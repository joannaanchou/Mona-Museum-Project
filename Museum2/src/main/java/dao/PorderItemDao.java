package dao;

import java.sql.Connection;
import java.util.List;

import model.PorderItem;

public interface PorderItemDao {


	void batchInsert(List<PorderItem> items, Connection conn); // 同一交易內批次新增訂單明細


	List<PorderItem> selectByPorderId(int porderId); //依 id 查詢明細
	Integer findPorderIdByItemId(int itemId);
    int sumLineTotalByPorderId(int porderId);
	
    void update(PorderItem item);

	void deleteByPorderId(int porderId, Connection conn);
	void deleteById(int id);
}
