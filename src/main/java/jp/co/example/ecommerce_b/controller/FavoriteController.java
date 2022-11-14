package jp.co.example.ecommerce_b.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.domain.LoginUser;
import jp.co.example.ecommerce_b.service.FavoriteService;

/**
 * 
 * @author hondayuki
 *
 */
@Controller
@RequestMapping("/favorite")
public class FavoriteController {
	@Autowired
	private FavoriteService favoriteService;
	
	
	/**
	 * 商品をお気に入りに追加します
	 * 
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping("/add")
	public String favorite(Integer itemId, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		if (loginUser == null) {
			return "redirect:/";
		}
		Integer userId = loginUser.Getusers().getId();
		if (favoriteService.confirmFavorite(userId, itemId).isEmpty()) {
			favoriteService.favorite(userId, itemId);
		}
		//favoriteService.favorite(1, 1);
		
		return "redirect:/toItemDetail?itemId=" + itemId;
	}
	
	/**
	 * お気に入り一覧を表示
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/list")
	public String showList(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		if (loginUser == null) {
			return "redirect:/";
		}
		Integer userId = loginUser.Getusers().getId();
		
		List<Favorite> favoriteList = favoriteService.showList(userId);
		model.addAttribute("favoriteList", favoriteList);
		
		return "favorite_list";
	}
	
	/**
	 * お気に入りを解除
	 * 
	 * @param itemId
	 * @return
	 */
	@RequestMapping("/remove")
	public String remove(Integer itemId, @AuthenticationPrincipal LoginUser loginUser) {
		Integer userId = loginUser.Getusers().getId();
		favoriteService.remove(userId, itemId);
		
		return "redirect:/favorite/list";
	}
}
