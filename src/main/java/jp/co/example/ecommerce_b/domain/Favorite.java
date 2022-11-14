package jp.co.example.ecommerce_b.domain;

/**
 * 
 * @author honda yuki
 *
 */
public class Favorite {
	/** ID */
	private Integer id;
	/** ユーザーID */
	private Integer userId;
	/** アイテムID */
	private Integer itemId;
	/** 商品 */
	private Item item;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public Integer getItemId() {
		return itemId;
	}
	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}
	public Item getItem() {
		return item;
	}
	public void setItem(Item item) {
		this.item = item;
	}
	@Override
	public String toString() {
		return "Favorite [id=" + id + ", userId=" + userId
				+ ", itemId=" + itemId + ", item=" + item
				+ "]";
	}
	
}
