package dao;

import java.sql.Connection;
import java.util.List;

import model.Porder;


public interface PorderDao {

	//create 
	int insert(Porder p, Connection conn); //同交易內新增主檔並回傳自動編號
	
	//read 
	List<Porder> getOrdersByMemberId(int memberId);
	List<Object[]> selectAllOrderDetails(); //findallporder 使用
	
	//update
	void updateTotal(int porderId, int total, Connection conn);
}
