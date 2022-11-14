package jp.co.example.ecommerce_b.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.service.Itemservice;

/**
 * 
 * @author honda yuki
 *
 */
@RestController
@RequestMapping("/item")
public class ItemRestController {
	
	@Autowired
	private Itemservice itemservice;
	
	@RequestMapping(value = "/autocomplete")
	public Map<String, String[]> autocomlete() {
		Map<String, String[]> map = new HashMap<>();
		List<Item> itemList = itemservice.findAllItemList();
		String[] items = new String[itemList.size()];
		for (int i = 0; i < itemList.size(); i++) {
			items[i] = itemList.get(i).getName();
		}
		map.put("items", items);
		return map;
	}
}
