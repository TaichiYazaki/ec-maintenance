package jp.co.example.ecommerce_b.service;

import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;

import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.repository.OrderRepository;

/**
 * 
 * @author honda yuki
 *
 */
@Service
public class OrderService {
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private MailSender sender;
	/**
	 * 
	 * @param userId ユーザーID
	 * @param status 注文状態
	 * @return カート情報が入ったOrderオブジェクト
	 */
	public Order showCart(Integer userId, Integer status, String preId) {
		return orderRepository.findByUserIdAndStatus(userId, status, preId);
	}
	
	/**
	 * アップデート後にメールを送信する
	 * 
	 * @param order 宛先が格納された注文情報
	 */
	public void orderUpdate(Order order) {
		orderRepository.update(order);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 hh時");
		String stringPaymentMethod = null;
		if (order.getPaymentMethod() == 1) {
			stringPaymentMethod = "代金引換";
		} else if(order.getPaymentMethod() == 2) {
			stringPaymentMethod = "クレジットカード";
		}
		String text = "ご注文金額:" + String.format("%,d", order.getTotalPrice()) + "円\n"
				+ "お支払い方法:" + stringPaymentMethod + "\n"
				+ "配達日時:" + sdf.format(order.getDeliveryTime()) + "\n"
				+ "ご注文内容:\n";
		for (OrderItem orderItem : order.getOrderItemList()) {
			text += orderItem.getItem().getName() + " " + orderItem.getQuantity() + "個\n";
		}
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("pupi.yh@gmail.com");
		//message.setTo("@gmail.com");
		message.setTo(order.getDestinationEmail());
		message.setSubject("ご注文を承りました。");
		message.setText(text);
		try {
			sender.send(message);
		} catch (MailException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		System.out.println("メールを送信しました");
	}
	
	/**
	 * 注文履歴を取得
	 * 
	 * @param userId
	 * @return
	 */
	public List<Order> getHistory(Integer userId) {
		return orderRepository.findByOrderd(userId);
	}
	
	/**
	 * @param order カート追加orderオブジェクト
	 * ordersテーブルへのinsert
	 */
	public void intoCart(Order order) {
		orderRepository.intoCart(order);
	}
	
	public int getOrderId(Integer userId) {
		return orderRepository.getOrderId(userId);
	}
	public int getOrderId2(String preId) {
		return orderRepository.getOrderId2(preId);
	}
	
	public void insertItem(OrderItem item) {
		orderRepository.insertItem(item);
	}
	
	public void insertTopping(List<OrderTopping> list) {
		orderRepository.insertTopping(list);
	}
	
	public void updateOrder(Order order) {
		orderRepository.updateOrder(order);
	}
	
	public int getItemId(Integer orderId) {
		return orderRepository.getItemId(orderId);
		
	}
	
	/**
	 * @param userId
	 * @return カートに追加された商品情報
	 * ログインユーザー用メソッド
	 */
	public Order getCartList(Integer userId){
		return orderRepository.getCartList(userId);
	}
	public Order getNotLoginCartList(String preId){
		return orderRepository.getNotLoginCartList(preId);
	}
	
	public int getTotalPrice(Integer userId) {
		return orderRepository.getTotalPrice(userId);
	}
	public int getNotLoginTotalPrice(String preId) {
		return orderRepository.getNotLoginTotalPrice(preId);
	}
	
	public void deleteCart(Integer id) {
		orderRepository.deleteCart(id);
		
	}
//	
//	public OrderItem getOrderItem(Integer orderId) {
//		return orderRepository.getOrderItem(orderId);
//	}
}
