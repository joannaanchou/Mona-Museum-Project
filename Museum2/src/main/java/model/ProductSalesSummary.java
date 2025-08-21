package model;

public class ProductSalesSummary {

	private Integer productId;
    private int totalAmount;

    public ProductSalesSummary(int productId, int totalAmount) {
        this.productId = productId;
        this.totalAmount = totalAmount;
    }

    public Integer getProductId() {
        return productId;
    }

    public int getTotalAmount() {
        return totalAmount;
    }
}
