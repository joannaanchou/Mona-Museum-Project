package model;

import java.io.Serializable;

public class TicketPeriod implements Serializable {

	private Integer id;
	private String ticketPeriodNo;
	private String name;
	
	public TicketPeriod() {
		super();
	}

	public TicketPeriod(Integer id, String ticketPeriodNo, String name) {
		super();
		this.id = id;
		this.ticketPeriodNo = ticketPeriodNo;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTicketPeriodNo() {
		return ticketPeriodNo;
	}

	public void setTicketPeriodNo(String ticketPeriodNo) {
		this.ticketPeriodNo = ticketPeriodNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
