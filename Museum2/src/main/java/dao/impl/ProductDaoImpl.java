package dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import dao.ProductDao;
import model.Product;
import util.DbConnection;

public class ProductDaoImpl implements ProductDao{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private static Connection conn=DbConnection.getDb();
	
	

	@Override
	public Product selectById(int id) {
		String sql="select*from product where id=?";
		Product product=null;
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1,id);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				product=new Product();
				product.setId(rs.getInt("id"));
				product.setTicketTypeId(rs.getInt("ticket_type_id"));
				product.setTicketPeriodId(rs.getInt("ticket_period_id"));
				product.setPrice(rs.getInt("price"));
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}

	
	
	@Override
	public Product selectByTypeAndPeriod(int ticketTypeId, int ticketPeriodId) {
		String sql="SELECT id, ticket_type_id AS ticketTypeId, ticket_period_id AS ticketPeriodId, price FROM product WHERE ticket_type_id=? AND ticket_period_id=? LIMIT 1";
		Product product=null;
		
		try {
			PreparedStatement ps=conn.prepareStatement(sql);
			ps.setInt(1, ticketTypeId);
			ps.setInt(2, ticketPeriodId);
			ResultSet rs=ps.executeQuery();
			
			if(rs.next())
			{
				product=new Product();
				product.setId(rs.getInt("id"));
				product.setTicketTypeId(rs.getInt("ticketTypeId"));
				product.setTicketPeriodId(rs.getInt("ticketPeriodId"));
				product.setPrice(rs.getInt("price"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return product;
	}
	

}
