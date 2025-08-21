package model;

import java.io.Serializable;

public class PorderItem implements Serializable{

	
	private int id;
	private Integer porderId;
	private Integer productId;
	private Integer unitPrice; //單價
	private Integer quantity;
	private Integer lineTotal; //單價*數量
	
	public PorderItem() {
		super();
	}

	public PorderItem(int id, Integer porderId, Integer productId, Integer unitPrice, Integer quantity,
			Integer lineTotal) {
		super();
		this.id = id;
		this.porderId = porderId;
		this.productId = productId;
		this.unitPrice = unitPrice;
		this.quantity = quantity;
		this.lineTotal = lineTotal;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getPorderId() {
		return porderId;
	}

	public void setPorderId(Integer porderId) {
		this.porderId = porderId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public Integer getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Integer unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getLineTotal() {
		return lineTotal;
	}

	public void setLineTotal(Integer lineTotal) {
		this.lineTotal = lineTotal;
	}
	
	
	
}
