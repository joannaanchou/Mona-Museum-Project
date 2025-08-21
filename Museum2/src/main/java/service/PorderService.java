package service;

import java.util.List;

import model.Porder;
import model.PorderItem;


public interface PorderService {

	
	//create
	long placeOrder(Porder order, List<PorderItem> items); //一次下單(多筆明細 + 回傳訂單) -->（AddPorder 使用）
	
	
	
	//read
	List<Porder> getOrdersByMemberId(int memberId); //（FindMyPorder 使用）
	
	List<Object[]> getAllOrderDetails(); //（FindAllPorder、ReportCharts 使用）
	
	

}
