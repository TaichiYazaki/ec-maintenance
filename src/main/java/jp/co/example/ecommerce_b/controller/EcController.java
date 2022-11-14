package jp.co.example.ecommerce_b.controller;

import java.util.List;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import jp.co.example.ecommerce_b.domain.Favorite;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.LoginUser;
import jp.co.example.ecommerce_b.form.ItemsearchForm;

import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.form.IntoCartForm;
import jp.co.example.ecommerce_b.service.FavoriteService;
import jp.co.example.ecommerce_b.service.Itemservice;

@Controller
@RequestMapping("")
public class EcController {

	@Autowired
	private Itemservice itemService;

	@Autowired
	private FavoriteService favoriteService;

	@Autowired
	private HttpSession session;

	@ModelAttribute
	public ItemsearchForm setUpFormItemSearch() {
		return new ItemsearchForm();
	}

	@ModelAttribute
	public IntoCartForm setUpForm() {
		return new IntoCartForm();
	}

	@RequestMapping("")
	public String index() {
		return "login";
	}

	/**
	 * 送られてきたitemIDをもとにして商品を取得するメソッド トッピング全件取得のsqlも実行し、トッピングリストをitemオブジェクトに格納
	 * 
	 * @param itemId 商品一覧画面より遷移するときに送られてくる Id
	 * @param model
	 * @return 商品詳細画面へフォワード
	 */
	@RequestMapping("/toItemDetail")
	public String toItemDetail(Integer itemId, Model model, @AuthenticationPrincipal LoginUser loginUser) {
//		System.out.println("システム起動");
		Item item = itemService.findByItemId(itemId);
		// Item item = itemService.findByItemId(1);

		List<Topping> toppingList = itemService.toppingFindAll();

		item.setToppingList(toppingList);

		if (loginUser != null) {
			Integer userId = loginUser.Getusers().getId();
			List<Favorite> favoriteList = favoriteService.confirmFavorite(userId, itemId);
			model.addAttribute("favoriteList", favoriteList);
		}
		model.addAttribute("item", item);
//		System.out.println(item);
//		System.out.println(toppingList);
//		
		return "item_detail";
	}

	/*
	 * 商品全件の一覧
	 */

	@RequestMapping("/itemList")
	public String itemList(String code, Model model) {
		// 全件表示
		Integer AllItemCount = itemService.AllItemCount();
		if (code == null) {
			List<Item> itemList = itemService.findAllItemList();
			model.addAttribute("searchCount", AllItemCount);
			model.addAttribute("itemList", itemList);
		} else {
			List<Item> searchItem = itemService.search(code);
			Integer searchCount1 = itemService.searchCount(code);
			model.addAttribute("code", code);
			String noList = "null";
			// 検索結果がない場合
			if (searchItem.isEmpty()) {
				noList = "該当する商品がありません";
				model.addAttribute("noList", noList);
				List<Item> itemList = itemService.findAllItemList();
				String noItem = "0";
				model.addAttribute("searchCount", noItem);
				model.addAttribute("itemList", itemList);
				// 検索結果がある場合
			} else if (!(null == searchItem)) {
				model.addAttribute("searchItem", searchItem);
				model.addAttribute("searchCount", searchCount1);
			}
		}
		return "item_list_curry";
	}

	@RequestMapping("/itemAlign")
	public String itemAlign(String listBox, Model model) {
		Integer AllItemCount = itemService.AllItemCount();
		model.addAttribute("searchCount", AllItemCount);
		if (listBox.equals("low")) {
			List<Item> itemList = itemService.lowList();		
			model.addAttribute("itemList", itemList);
		} else if (listBox.equals("high")) {
			List<Item> itemList = itemService.highList();
			model.addAttribute("itemList", itemList);
		} else {
			List<Item> itemList = itemService.findAllItemList();
			model.addAttribute("itemList", itemList);
		}
		return "item_list_curry";
	}

}
