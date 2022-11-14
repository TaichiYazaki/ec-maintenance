package jp.co.example.ecommerce_b.domain;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class Order {

	private Integer id;

	private Integer userId;

	private String preId;

	private Integer status;

	private Integer totalPrice;

	private Date orderDate;

	private String destinationName;

	private String destinationEmail;

	private String destinationZipcode;

	private String destinationAddress;

	private String destinationTel;

	private Timestamp deliveryTime;

	private Integer paymentMethod;

	private Users user;

	private List<OrderItem> orderItemList;

	public int getTax() {
		return totalPrice / 10;
	}

	public int getCalcTotalPrice() {
		return totalPrice + getTax();
	}

}
