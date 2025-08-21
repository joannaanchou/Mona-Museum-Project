package service;

import model.Product;

public interface ProductService {

	

	
	//read
	Product getProductById(int id); //（FindMyPorder、Finish 使用）
	Product getProductByTypeAndPeriod(int ticketTypeId, int ticketPeriodId); //取得對應商品 （AddPorder／FindAllPorder 使用）
	Integer getPriceByTypeAndPeriod(int ticketTypeId, int ticketPeriodId); //取得對應價格 （AddPorder 使用）

	

}
