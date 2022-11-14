package jp.co.example.ecommerce_b.domain;

import lombok.Data;

@Data
public class Favorite {
	/** ID */
	private Integer id;
	/** ユーザーID */
	private Integer userId;
	/** アイテムID */
	private Integer itemId;
	/** 商品 */
	private Item item;

}
