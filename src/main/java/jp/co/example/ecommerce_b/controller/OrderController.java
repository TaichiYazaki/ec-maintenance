package jp.co.example.ecommerce_b.controller;



import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.LoginUser;
import jp.co.example.ecommerce_b.domain.Order;

import jp.co.example.ecommerce_b.form.UpdateOrderForm;
import jp.co.example.ecommerce_b.service.OrderService;

/**
 * 
 * @author honda yuki
 *
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpSession session;
	
	@ModelAttribute
	public UpdateOrderForm setUpUpdateOrderForm() {
		return new UpdateOrderForm();
	}
	/**
	 * 
	 * @param model Orderオブジェクトを格納するモデル
	 * @return ログインしてなければログイン画面、注文情報がなければ商品一覧画面、あれば注文確認画面へ遷移
	 */
	@RequestMapping("/confirm")
	public String showOrderConfirm(UpdateOrderForm form, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		if (loginUser == null) {
			return "redirect:/login";
		}
		Integer userId = loginUser.Getusers().getId();
		String preId = (String) session.getAttribute("preId");
		
		Order order = null;
		if (orderService.showCart(0, 0, preId) != null) {
			order = orderService.showCart(0, 0, preId);
		} else {
			order = orderService.showCart(userId, 0, null);
		}
		if (order == null) {
			return "item_list_curry";
		}
		//配達日時をマップに格納
		Map<String, Integer> deliveryTimeMap = new LinkedHashMap<>();
		for (int i = 10; i <= 18; i++) {
			deliveryTimeMap.put(i + "時", i);
		}
		//formの初期値を設定
		if (form.getDeliveryTime() == null) {
			form.setDeliveryTime("10");
		}
		if (form.getPaymentMethod() == null) {
			form.setPaymentMethod("1");
		}
		
		model.addAttribute("deliveryTimeMap", deliveryTimeMap);
		model.addAttribute("order", order);
		
		return "order_confirm";
	}
	
	/**
	 * 入力値から注文テーブルを更新する
	 * 
	 * @param form 入力値
	 * @param result エラー文格納オブジェクト
	 * @param model
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping("/post")
	public String order(
			@Validated UpdateOrderForm form,
			BindingResult result,
			Model model,
			@AuthenticationPrincipal LoginUser loginUser) throws ParseException {
		//送信時の日付をlong型で取得
		long today = new Date().getTime();
		
		//入力された日付と時刻を文字列として結合したあとにlong型でフォーマット
		long delivaryDateTimeLong = 0;
		if (!form.getDeliveryDate().isBlank()) {
			String delivaryDateTime = form.getDeliveryDate() + " " + form.getDeliveryTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh");
			delivaryDateTimeLong = sdf.parse(delivaryDateTime).getTime();
			
			//時刻を比較して時間差を抽出
			long diff = delivaryDateTimeLong - today;
			TimeUnit time = TimeUnit.HOURS;
			long difference = time.convert(diff, TimeUnit.MILLISECONDS);
			
			//時間差が3時間より小さけれればエラー文を格納
			if (difference < 3) {
				result.rejectValue("deliveryDate", null, "今から3時間後の日時をご入力ください");
			}
		}
		//1つでもエラーがあれば注文完了画面へ
		if (result.hasErrors()) {
			return showOrderConfirm(form, model, loginUser);
		}
		//java.sql.Dateへ現時点の日付を変換
		java.sql.Date sqlToday = new java.sql.Date(today);
		Timestamp todayTimestamp = new Timestamp(delivaryDateTimeLong);
		
		//orderオブジェクトにformの値をコピー
		String preId = (String) session.getAttribute("preId");
		Order order = null;
		if (preId != null) {
			order = orderService.showCart(0, 0, preId);
		} else {
			order = orderService.showCart(loginUser.Getusers().getId(), 0, null);
		}
		if (order == null) {
			return "redirect:/item/showList";
		}
		BeanUtils.copyProperties(form, order);
		
		//コピーできなかった値を手動でコピー
		order.setId(order.getId());
		order.setUserId(loginUser.Getusers().getId());
		order.setOrderDate(sqlToday);
		order.setDeliveryTime(todayTimestamp);
		order.setPaymentMethod(form.getIntPaymentMethod());
		if (order.getPaymentMethod() == 1) {
			order.setStatus(1);
		} else if (order.getPaymentMethod() == 2) {
			order.setStatus(2);
		}
		
		orderService.orderUpdate(order);
		return "redirect:/order/completion";
	}
	
	/**
	 * 注文完了画面を表示する
	 * 
	 * @return
	 */
	@RequestMapping("/completion")
	public String complete() {
		return "order_finished";
	}
	
	/**
	 * 注文履歴を表示する
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/history")
	public String showHistrory(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		//ログインチェック
		if (loginUser == null) {
			return "redirect:/";
		}
		Integer userId = loginUser.Getusers().getId();
		List<Order> orderList = orderService.getHistory(userId);
		System.out.println(orderList.size());
		model.addAttribute("orderList", orderList);
		return "order_history";
	}
	

	@RequestMapping("toCartList")
	
	public String toCartList(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		
		Order order = new Order();
		//ログイン状態の条件分岐
		if(loginUser !=null) {
			//ログインしている
			order=orderService.getCartList(loginUser.Getusers().getId());
		}else {
			//非ログイン
			order=orderService.getNotLoginCartList((String)session.getAttribute("preId"));
		}
		
		//orderListをrequestスコープに格納(orderList)
		if(order == null || order.getOrderItemList() == null) {
			model.addAttribute("cartNullMessage","カートに商品がありません");
		} else if(order.getOrderItemList().isEmpty()) {
			model.addAttribute("cartNullMessage","カートに商品がありません");
		} else {
			model.addAttribute("cart",order);
		}
		return "cart_list";
	}
	
	@RequestMapping("/deleteCart")
	public String deleteCart(Integer orderId,Integer subTotalPrice,Model model, @AuthenticationPrincipal LoginUser loginUser) {
		
		Order order = new Order();
		
//		cartItem=orderService.getOrderItem(orderId);
	
		int total=0;
		//ログイン状態の分岐
		if(loginUser != null) {
			//ログインユーザーの処理
			Integer userId = loginUser.Getusers().getId();
			total=orderService.getTotalPrice(userId);
			order.setUserId(userId);
			
		}else {
			total=orderService.getNotLoginTotalPrice((String)session.getAttribute("preId"));
			order.setPreId((String)session.getAttribute("preId"));
		}
		total -= subTotalPrice;
		order.setTotalPrice(total);
		orderService.updateOrder(order);
		orderService.deleteCart(orderId);
		return toCartList(model, loginUser);
	}
}
