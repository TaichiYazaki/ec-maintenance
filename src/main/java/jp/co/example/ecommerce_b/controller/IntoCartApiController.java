package jp.co.example.ecommerce_b.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import jp.co.example.ecommerce_b.domain.LoginUser;
import jp.co.example.ecommerce_b.domain.Order;
import jp.co.example.ecommerce_b.domain.OrderItem;
import jp.co.example.ecommerce_b.domain.OrderTopping;
import jp.co.example.ecommerce_b.service.OrderService;

@RestController
@RequestMapping("/intoCart")
@SessionAttributes(value="toppingId")
public class IntoCartApiController {

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private HttpSession session;
	
	
	@RequestMapping(value="/insert",method=RequestMethod.POST)
	public void insert(Integer priceM,Integer priceL,Integer itemId,char size,String toppingId,Integer quantity,@AuthenticationPrincipal LoginUser loginUser) {
		List<String> toppingIds= Arrays.asList(toppingId.split(","));
		List<Integer> toIntToppingIds= new ArrayList<>();
		if(!(toppingId.equals(""))){
		for(String ids:toppingIds) {
			toIntToppingIds.add(Integer.parseInt(ids));
		}
		}
	
//		
//		//動作確認用
//		session.setAttribute("userId", 2);
		
		//仮登録
//		itemId=1;
		//必要なオブジェクト生成
		Order order = new Order();
		OrderItem item = new OrderItem();
		
		List<OrderTopping> toppingList = new ArrayList<>();

		//orderId初期化
		int orderId=0;
		
		//小計金額計算(カートの状態、ログイン状態は関係なしの共通処理)
		int total =0;
		if(size =='M') {
			total += priceM;
			//トッピングが選択されている場合の処理
			if(!(toIntToppingIds.isEmpty())) {
			total += toIntToppingIds.size()*200;
			}
			
		}else if(size =='L') {
			total += priceL;
			if(!(toIntToppingIds.isEmpty())) {
			total += toIntToppingIds.size()*300;
			}
		
		}
		item.setSize(size);
		total *= quantity;
		//orderオブジェクトに小計金額とステータスをセット
		order.setTotalPrice(total);
		order.setStatus(0);
		//ログインしているか否か判定
		if(loginUser != null) {
			//ログインしている場合の処理
			Integer userId = loginUser.Getusers().getId();
			//userIdをorderオブジェクトにセット
			order.setUserId(userId);
			//カート内に商品があるか否かの判定のためuserIdでordersテーブルを検索
			 orderId=orderService.getOrderId(userId);
			if(orderId ==0) {
				//カートが空の状態時の処理
				//orderテーブルへのinsert処理(カートへの新規追加)
				orderService.intoCart(order);
				//直前でinsertしたorderのIDを取得
				orderId=orderService.getOrderId(userId);
			}else {
				//カートが空じゃない場合の処理
				//既にカートに入っている合計金額を取得
				Integer preTotal= orderService.getTotalPrice(userId);
				//今カートに追加した商品の金額をカートリスト合計と合わせる
				total += preTotal;
				order.setTotalPrice(total);
				orderService.updateOrder(order);
			}
			//orderIdをセット
			item.setOrderId(orderId);
			
		}else {
			//ログインしていない状態の処理
			//userIdを0でセット(userIdはNot Null設定のため)
			order.setUserId(0);
			String preId = (String) session.getAttribute("preId");
			//preIdが発行されていない＝カートに商品がない状態
			if(orderService.getNotLoginCartList(preId) ==null) {
				//カートが空の状態時の処理
				//新規にpreIdを作成オブジェクトにセット
				
				UUID uuID= UUID.randomUUID();
				String uuid = uuID.toString();
				
				session.setAttribute("preId", uuid);
				order.setPreId(uuid);
				//orderテーブルへのinsert処理(カートへの新規追加)
				orderService.intoCart(order);
				//直前でinsertしたorderのIDを取得
				 orderId=orderService.getOrderId2(session.getAttribute("preId").toString());
			}else {
				//カートが空じゃない場合の処理
				//*注意　動作確認時、一度preId発行した後にDBでデータ削除すると正常な動作できなくなる
				//発行済みのpreIdをもとにorderIdを取得
				 orderId=orderService.getOrderId2(session.getAttribute("preId").toString());
				//過去にカート追加した際発行されたpreIdをorderオブジェクトにセット
				order.setPreId(session.getAttribute("preId").toString());
				//既にカートに入っている合計金額を取得
				Integer preTotal= orderService.getNotLoginTotalPrice((String)session.getAttribute("preId"));
				//今カートに追加した商品の金額をカートリスト合計と合わせる
				total += preTotal;
				order.setTotalPrice(total);
				orderService.updateOrder(order);
				//合計金額の更新
				orderService.updateOrder(order);
			}
			//orderIdをセット
			item.setOrderId(orderId);
		
		}
		
//	　　　itemIdをセット
		item.setItemId(itemId);
		
		
		//個数をセット
		item.setQuantity(quantity);
		//orderテーブルへのinsert処理
		orderService.insertItem(item);
		if(!(toIntToppingIds.isEmpty())) {
		//formのトッピングリストから情報を一件ずつ取得し、新たなリストに格納
		
		int itemIdd = orderService.getItemId(orderId);
		
		for(Integer id:toIntToppingIds) {
			OrderTopping topping = new OrderTopping();
			topping.setToppingId(id);
			topping.setOrderItemId(itemIdd);
			toppingList.add(topping);
			}
		//トッピングが選択されていた場合にorderToppingテーブルにinsert処理
			
		
		orderService.insertTopping(toppingList);
		}
		
		
	}
	
	

}
