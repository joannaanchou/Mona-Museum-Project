package model;

import java.io.Serializable;

public class TicketType implements Serializable {
	
	private Integer id;
	private String ticketTypeNo;
	private String name;
	
	public TicketType() {
		super();
	}
	
	public TicketType(Integer id, String ticketTypeNo, String name) {
		super();
		this.id = id;
		this.ticketTypeNo = ticketTypeNo;
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getTicketTypeNo() {
		return ticketTypeNo;
	}
	public void setTicketTypeNo(String ticketTypeNo) {
		this.ticketTypeNo = ticketTypeNo;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	

}
