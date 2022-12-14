package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Item;
import jp.co.example.ecommerce_b.domain.Topping;



@Repository
public class Itemrepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	private static final RowMapper<Item> ITEM_ROW_MAPPER=new BeanPropertyRowMapper<>(Item.class);
	private static final RowMapper<Topping> TOPPING_ROW_MAPPER=new BeanPropertyRowMapper<>(Topping.class);
	/**
	 * 全件検索 全商品一覧の表示
	 */

	public List<Item> findAllItems() {
		String sql = "SELECT id,name,description,price_m,price_l,image_path FROM items ORDER BY price_m ASC ";
		List<Item> findAllItems=template.query(sql, ITEM_ROW_MAPPER);
		return findAllItems;
	}
	
	/**
	 * 全件検索　安い順
	 */
	public List<Item> findAllLow(){
		String sql = "SELECT id, name, description, price_m, price_l, image_path FROM items ORDER BY price_m ASC";
		List<Item> lowList=template.query(sql, ITEM_ROW_MAPPER);
		return lowList;
		
	}
	/**
	 * 全件検索　高い順
	 */
	public List<Item> findAllHigh(){
		String sql = "SELECT id, name, description, price_m, price_l, image_path FROM items ORDER BY price_m DESC";
		List<Item> highList=template.query(sql, ITEM_ROW_MAPPER);
		return highList;
		
	}
	
	
	
	/**itemIdを渡してそれをもとにitemsテーブルを検索
	 * @param itemId
	 * @return Idをもとに検索した結果の商品情報
	 */
	public Item findByItemId(Integer itemId) {
		
		String sql = "SELECT id,name,description,price_m,price_l,image_path FROM items WHERE id=:itemId;";
		SqlParameterSource param = new MapSqlParameterSource().addValue("itemId", itemId);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
		
	}
	
	/**トッピング全件を取得し、リストにしてreturnするメソッド
	 * @return　全トッピングリスト
	 */
	public List<Topping> findAll(){
		String sql = "SELECT id,name,price_m,price_l FROM toppings ; ";
		List<Topping> list=template.query(sql, TOPPING_ROW_MAPPER);
		return list;
	}
	
	
	
	/**
	 * 商品検索
	 */
	public List<Item> searchItems(String name) {
		String sql="SELECT id,name,description,price_m,price_l,image_path FROM items WHERE name LIKE :name  ORDER BY price_m ASC";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
		List <Item> searchItems =template.query(sql, param, ITEM_ROW_MAPPER);
		return searchItems;
	}
	
	/**
	 * 検索した商品の集約(ヒット数)
	 */
	public Integer countItems(String name) {
		String sql="SELECT count(name) FROM items WHERE name LIKE :name";
		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%"+name+"%");
	     Integer countItems=template.queryForObject(sql, param, Integer.class);
		return countItems;
	}
	/**
	 * 全商品の集約(ヒット数)
	 */
	public Integer countAllItems() {
		String sql="SELECT count(*) FROM items";
		SqlParameterSource param = new MapSqlParameterSource();
		Integer countAllItems=template.queryForObject(sql, param, Integer.class);
		 return countAllItems;
	}
	}

