package jp.co.example.ecommerce_b.domain;

import lombok.Data;

@Data
public class OrderTopping {

	private Integer id;

	private Integer toppingId;

	private Integer orderItemId;

	private Topping topping;

}
