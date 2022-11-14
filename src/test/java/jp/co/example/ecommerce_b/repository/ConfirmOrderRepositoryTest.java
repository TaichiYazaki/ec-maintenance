package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

import jp.co.example.ecommerce_b.domain.Order;
/**
 * 
 * @author hondayuki
 *
 */
@SpringBootTest
class ConfirmOrderRepositoryTest {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;

	@Test
	void test() {
		fail("まだ実装されていません");
	}
	
	@BeforeEach
	void firstInsert() {
		System.out.println("テスト用のデータを挿入");
		MapSqlParameterSource param = new MapSqlParameterSource()
				.addValue("userId", 1).addValue("status", 0).addValue("totalPrice", 4760);
		String orderSql = "insert into orders"
				+ " (user_id, status, total_price) values"
				+ " (:userId, :status, :totalPrice)";
		template.update(orderSql, param);
		
		Integer maxId = template.queryForObject("select max(id) from orders", param, Integer.class);
		param.addValue("maxId", maxId);
		String orderItemSql = "insert into order_items"
				+ " (item_id, order_id, quantity, size) values"
				+ " (1, :maxId, 1, 'M'),"
				+ " (2, :maxId, 1, 'L')";
		template.update(orderItemSql, param);
		
		Integer maxOIId = template.queryForObject("select max(id) from order_items", param, Integer.class);
		Integer premaxOIId = template.queryForObject("select max(id)-1 from order_items", param, Integer.class);
		param.addValue("maxOIId", maxOIId).addValue("premaxOIId", premaxOIId);
		String orderToppingSql = "insert into order_toppings"
				+ " (topping_id, order_item_id) values"
				+ " (1, :maxOIId),"
				+ " (2, :maxOIId),"
				+ " (1, :premaxOIId),"
				+ " (2, :premaxOIId)";
		template.update(orderToppingSql, param);
		
		System.out.println("テスト用データ挿入完了");
		
	}
	
	@Test
	void load() {
		System.out.println("ユーザーIdとステータスで検索するテスト開始");
		
		Order order = orderRepository.findByUserIdAndStatus(1, 0, "1");
		assertNotNull(order, "注文情報がありません");
		assertEquals(4760, order.getTotalPrice(), "金額が不一致");
		assertFalse(order.getOrderItemList().isEmpty(), "商品が追加されていません");
		
		
		System.out.println("ユーザーIdとステータスで検索するテスト終了");
	}
	
	@AfterEach
	void tearDownAfterClass() {
		MapSqlParameterSource param = new MapSqlParameterSource();
		//ordersテーブルを削除してIDを取得
		Integer maxId = 
				template.queryForObject("delete from orders where id in (select max(id) from orders) returning id", param, Integer.class);
		//order_itemsテーブルの最後から2つのデータのIDを取得
		Integer maxOIId = template.queryForObject("select max(id) from order_items", param, Integer.class);
		Integer preMaxOIId = template.queryForObject("select max(id)-1 from order_items", param, Integer.class);
		//orderIdからorder_itemsのレコードを削除
		param.addValue("maxId", maxId);
		template.update("delete from order_items where order_id=:maxId", param);
		//orderItemIdからorder_toppingのレコードを削除
		SqlParameterSource param2 = new MapSqlParameterSource().addValue("maxOIId", maxOIId).addValue("preMaxOIId", preMaxOIId);
		template.update("delete from order_toppings where order_item_id=:maxOIId OR order_item_id=:preMaxOIId", param2);
		System.out.println("テスト用データを削除しました");
	}

}
