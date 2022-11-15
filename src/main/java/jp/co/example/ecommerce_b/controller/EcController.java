package jp.co.example.ecommerce_b.controller;

import java.util.List;

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
public class EcController {

	@Autowired
	private Itemservice itemService;

	@Autowired
	private FavoriteService favoriteService;

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

	/**
	 * 商品一覧を全件表示します
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/showItems")
	public String showItems(Model model) {
		List<Item> listAllItems = itemService.findAllItems();
		Integer countAllItems = itemService.countAllItems();
		model.addAttribute("countAllItems", countAllItems);
		model.addAttribute("listAllItems", listAllItems);
		return "show_items";
	}

	/**
	 * 商品を検索します
	 * 
	 * @param itemName(商品名)
	 * @param model
	 * @return
	 */
	@RequestMapping("/searchItems")
	public String searchItems(String itemName, Model model) {
		List<Item> listAllItems = itemService.findAllItems();
		List<Item> searchItems = itemService.searchItems(itemName);
		Integer countAllItems = itemService.countAllItems();
		Integer countItems = itemService.countItems(itemName);
		// 空文字、空欄で検索ボタンを押した時
		if (itemName.isEmpty()) {
			model.addAttribute("listAllItems", listAllItems);
			model.addAttribute("itemName", "文字を入力してください");
			model.addAttribute("countItems", countAllItems);
		} else {
			if (searchItems.isEmpty()) {
				// 検索結果が存在しない時
				String noItemsText = "該当する商品がありません";
				String noItemsCount = "0";
				model.addAttribute("noItemsText", noItemsText);
				model.addAttribute("itemName", itemName);
				model.addAttribute("countItems", noItemsCount);
			} else if (!(itemName == null)) {
				// 検索結果が存在する時
				model.addAttribute("searchItems", searchItems);
				model.addAttribute("itemName", itemName);
				model.addAttribute("countItems", countItems);
			}
		}
		return "show_items";
	}

	@RequestMapping("/itemAlign")
	public String itemAlign(String listBox, Model model) {
		Integer AllItemCount = itemService.countAllItems();
		model.addAttribute("searchCount", AllItemCount);
		if (listBox.equals("low")) {
			List<Item> itemList = itemService.lowList();
			model.addAttribute("itemList", itemList);
		} else if (listBox.equals("high")) {
			List<Item> itemList = itemService.highList();
			model.addAttribute("itemList", itemList);
		} else {
			List<Item> itemList = itemService.findAllItems();
			model.addAttribute("itemList", itemList);
		}
		return "show_items";
	}

}
