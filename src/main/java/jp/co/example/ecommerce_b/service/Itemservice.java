package jp.co.example.ecommerce_b.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;
import jp.co.example.ecommerce_b.repository.Itemrepository;

@Service
@Transactional
public class Itemservice {
	
	@Autowired
	private Itemrepository itemRepository;
	/**
	 * 全件検索 全商品一覧の表示
	 */
	public List<Item> findAllItemList(){
		return itemRepository.findAllItemList();
	}
	
	/**
	 * 全件検索　安い順
	 */
	public List<Item> lowList(){
		return itemRepository.findAllLow();
	}
	
	/**
	 * 全件検索　高い順
	 */
	public List<Item> highList(){
		return itemRepository.findAllHigh();
	}
	
	/**
	 * 商品検索
	 */
	public List<Item> search(String name) {
		return itemRepository.search(name);
	}
	
	/**　送られてきたitemIdを条件としてItemテーブルを検索する
	 * @param itemId
	 * @return　itemRepositoryのfindByItemIdメソッド実行する
	 */
	public Item findByItemId(Integer itemId) {
		Item item=itemRepository.findByItemId(itemId);
		return item;
	}
	
	/**
	 * @return　全トッピングリスト
	 * 
	 */
	public List<Topping> toppingFindAll(){
		return itemRepository.findAll();
		
	}
	/**
	 * 商品結果カウント
	 * 
	 */
	public Integer searchCount(String name) {
		return itemRepository.searchCount(name);
		}
	/**
	 * 全商品カウント
	 * 
	 */
	public Integer AllItemCount() {
		return itemRepository.AllItemCount();
	}
}