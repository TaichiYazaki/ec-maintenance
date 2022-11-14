package jp.co.example.ecommerce_b.domain;

import java.util.List;

import lombok.Data;

@Data
public class OrderItem {

	private Integer id;

	private Integer orderId;

	private Integer itemId;

	private Integer quantity;

	private Character size;

	private Item item;

	private List<OrderTopping> orderToppingList;

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
