package model;

import java.io.Serializable;

public class Product implements Serializable{
	
	private Integer id;
	private Integer ticketTypeId;
	private Integer ticketPeriodId;
	private int price;
	
	public Product() {
		super();
		
	}
	
	public Product(Integer id, Integer ticketTypeId, Integer ticketPeriodId, int price) {
		super();
		this.id = id;
		this.ticketTypeId = ticketTypeId;
		this.ticketPeriodId = ticketPeriodId;
		this.price = price;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getTicketTypeId() {
		return ticketTypeId;
	}
	public void setTicketTypeId(Integer ticketTypeId) {
		this.ticketTypeId = ticketTypeId;
	}
	public Integer getTicketPeriodId() {
		return ticketPeriodId;
	}
	public void setTicketPeriodId(Integer ticketPeriodId) {
		this.ticketPeriodId = ticketPeriodId;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	
	

}
