package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Porder implements Serializable{

	private Integer id;
	private Integer memberId;
	private String orderNo;
	private int totalAmount;
	private LocalDateTime orderDate;
	private LocalDateTime createdAt;
	
	public Porder() {
		super();
	}

	public Porder(Integer id, Integer memberId ,String orderNo, int totalAmount,
			LocalDateTime orderDate, LocalDateTime createdAt) {
		super();
		this.id = id;
		this.orderNo = orderNo;
		this.totalAmount = totalAmount;
		this.orderDate = orderDate;
		this.createdAt=createdAt;
		this.memberId=memberId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}


	public int getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(int totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public Integer getMemberId() {
	    return memberId;
	}

	public void setMemberId(Integer memberId) {
	    this.memberId = memberId;
	}
	
	
	
	
	
	
	
}


