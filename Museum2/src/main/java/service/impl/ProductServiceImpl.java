package service.impl;


import dao.impl.ProductDaoImpl;
import model.Product;
import service.ProductService;

public class ProductServiceImpl implements ProductService{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private static ProductDaoImpl pdi=new ProductDaoImpl();


	@Override
	public Product getProductById(int id) {
		// TODO Auto-generated method stub
		return pdi.selectById(id);
	}
	
	
	@Override
	public Product getProductByTypeAndPeriod(int ticketTypeId, int ticketPeriodId) {
		if(ticketTypeId<=0||ticketPeriodId<=0)return null; //若查無則回傳null
		return pdi.selectByTypeAndPeriod(ticketTypeId, ticketPeriodId);
	}

	@Override
	public Integer getPriceByTypeAndPeriod(int ticketTypeId, int ticketPeriodId) {
		Product p=getProductByTypeAndPeriod(ticketTypeId, ticketPeriodId);
		if(p!=null)
		{
			return p.getPrice();
		}else {
			return null;
		}
		
	}

}
