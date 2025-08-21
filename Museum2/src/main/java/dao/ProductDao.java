package dao;



import model.Product;


public interface ProductDao {

		//read
		Product selectById(int id);
		Product selectByTypeAndPeriod(int ticketTypeId, int ticketPeriodId);
		
		
		
}
