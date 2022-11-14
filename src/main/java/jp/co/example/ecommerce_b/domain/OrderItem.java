package jp.co.example.ecommerce_b.domain;

import java.util.List;

public class OrderItem {

	private Integer id;
	private Integer orderId;
	private Integer itemId;
	private Integer quantity;
	private Character size;
	private Item item;
	private List<OrderTopping> orderToppingList;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public Character getSize() {
		return size;
	}
	public void setSize(Character size) {
		this.size = size;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}
	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}
	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", orderId=" + orderId + ", itemId=" + itemId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}
	
	public int getSubTotal() {
		int subTotalPrice = 0;
		int orderToppingPrice = 0;
		for (OrderTopping orderTopping : orderToppingList) {
			if (size == 'M') {
				orderToppingPrice += orderTopping.getTopping().getPriceM(); 
			} else if (size == 'L') {
				orderToppingPrice += orderTopping.getTopping().getPriceL();
			}
			
		}
		if (size == 'M') {
			subTotalPrice = item.getPriceM() + orderToppingPrice;
		} else if (size == 'L') {
			subTotalPrice = item.getPriceL() + orderToppingPrice;
		}
		subTotalPrice *= quantity;
		return subTotalPrice;
	}
}
