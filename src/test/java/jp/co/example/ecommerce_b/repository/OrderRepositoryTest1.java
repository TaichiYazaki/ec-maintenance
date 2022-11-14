package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;

class OrderRepositoryTest1 {
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	

	@BeforeEach
	void setUp()  {
		System.out.println("DB登録初期処理");
		Order order = new Order();
		OrderItem item = new OrderItem();
		OrderTopping topping = new OrderTopping();
		order.setUserId(0);
		order.setPreId("aaa");
		order.setStatus(0);
		order.setTotalPrice(1000);
		System.out.println(order);
		orderRepository.intoCart(order);
		System.out.println("ordersテーブル登録完了");
		Integer orderId=template.queryForObject("select max(id) from orders", new MapSqlParameterSource(), Integer.class);
		item.setItemId(1);
		item.setOrderId(orderId);
		item.setQuantity(1);
		item.setSize('M');
		
		orderRepository.insertItem(item);
		List<OrderTopping>toppingList=new ArrayList<>();
		Integer orderItemId=template.queryForObject("select max(id) from order_items", new MapSqlParameterSource(), Integer.class);
		topping.setOrderItemId(orderItemId);
		topping.setToppingId(1);
		toppingList.add(topping);
		System.out.println("insert完了");
		
		
	}

	@AfterEach
	void tearDown() throws Exception {
		Integer orderItemId=template.queryForObject("select max(id) from order_items", new MapSqlParameterSource(), Integer.class);
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
		template.update("delete from order_toppings where order_item_id=:orderItemId", param);
		
		Integer orderId=template.queryForObject("select max(id) from orders", new MapSqlParameterSource(), Integer.class);
		MapSqlParameterSource param2 = new MapSqlParameterSource().addValue("orderId", orderId);
		template.update("delete from order_items where order_id=:orderId", param2);
		
		MapSqlParameterSource param3 = new MapSqlParameterSource().addValue("userId", 0);	
		template.update("delete from orders where user_id=:userId", param3);
		System.out.println("入れたデータを削除しました。");
	}

	@Test
	public void testCheckOrderId() {
	System.out.println("userIdからorderIdを検索するテスト");
	Integer orderId=orderRepository.getOrderId(0);
	assertEquals(orderId,template.queryForObject("select max(id) from orders", new MapSqlParameterSource(), Integer.class),"orderIdが取得できていません");
	System.out.println("orderId検索テスト完了");
	}
	
	@Test
	public void testCheckOrderIdpre() {
		System.out.println("preIdからorderIdを検索するテスト");
		Integer orderId=orderRepository.getOrderId2("aaa");
		assertEquals(orderId,template.queryForObject("select max(id) from orders", new MapSqlParameterSource(), Integer.class),"orderIdが取得できていません");
		System.out.println("orderId検索テスト完了");
	}
	@Test
	public void testGetTotalPrice() {
		System.out.println("userIdから合計金額取得テスト");
		int totalPrice=orderRepository.getTotalPrice(0);
		assertEquals(totalPrice,1000,"合計金額が取得できません");
		System.out.println("合計金額取得テスト完了");
	}
	
	@Test
	public void testInsert() {
		System.out.println("insertテスト");
		Order order = new Order();
		order.setUserId(100);
		order.setStatus(0);
		order.setTotalPrice(5000);
		orderRepository.intoCart(order);
		int total = template.queryForObject("select total_price from orders where user_id=100;" , new MapSqlParameterSource(), Integer.class);
		assertEquals(total,5000,"insertができていません");
		Integer orderId=template.queryForObject("select max(id) from orders", new MapSqlParameterSource(), Integer.class);
		OrderItem orderItem = new OrderItem();
		orderItem.setItemId(2);
		orderItem.setOrderId(orderId);
		orderItem.setSize('L');
		orderItem.setQuantity(3);
		orderRepository.insertItem(orderItem);
		char orderSize=template.queryForObject("select size from order_items where order_id=:orderId;", new MapSqlParameterSource().addValue("orderId", orderId), char.class);
		Integer orderItemId=template.queryForObject("select item_id from order_items where order_id=:orderId;", new MapSqlParameterSource().addValue("orderId", orderId), Integer.class);
		Integer orderItemQuantity=template.queryForObject("select quantity from order_items where order_id=:orderId;", new MapSqlParameterSource().addValue("orderId", orderId), Integer.class);
		assertEquals(orderSize,'L',"サイズ登録ができていません");
		assertEquals(orderItemId,2,"itemId登録ができていません");
		assertEquals(orderItemQuantity,3,"数量登録ができていません");
		
		
		
	}
	
	

}
