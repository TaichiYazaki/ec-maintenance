package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.domain.Item;

/**
 * 
 * @author hondayuki
 *
 */
@Repository
public class FavoriteRepository {
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Favorite> FAVORITE_ROW_MAPPER = (rs, i) -> {
		Favorite favorite = new Favorite();
		favorite.setId(rs.getInt("id"));
		favorite.setUserId(rs.getInt("user_id"));
		favorite.setItemId(rs.getInt("item_id"));
		Item item = new Item();
		item.setId(rs.getInt("item_id"));
		item.setImagePath(rs.getString("image_path"));
		item.setName(rs.getString("name"));
		favorite.setItem(item);
		
		return favorite;
	};
	
	/**
	 * ユーザーIDとアイテムID から検索
	 * 
	 * @param userId
	 * @param itemId
	 * @return
	 */
	public List<Favorite> findByUserIdAndItemId(Integer userId, Integer itemId) {
		String sql = "SELECT f.id,item_id,user_id,name,image_path"
				+ " FROM favorites as f"
				+ " JOIN items as i"
				+ " ON f.item_id=i.id"
				+ " WHERE user_id=:userId";
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		if (itemId != null) {
			sql += " AND item_id=:itemId";
			param.addValue("itemId", itemId);
		}
		sql += " ORDER BY f.id DESC";
		List<Favorite> favoriteList = template.query(sql, param, FAVORITE_ROW_MAPPER);
		
		return favoriteList;
	}
	
	/**
	 * favoritesテーブルに登録
	 * 
	 * @param userId
	 * @param itemId
	 */
	public void insert(Integer userId, Integer itemId) {
		String sql = "INSERT INTO favorites"
				+ "        (user_id, item_id)"
				+ " VALUES (:userId, :itemId)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("itemId", itemId);
		
		template.update(sql, param);
	}
	
	/**
	 * ユーザーIDとアイテムIDをもとに削除
	 * 
	 * @param userId
	 * @param itemId
	 */
	public void delete(Integer userId, Integer itemId) {
		String sql = "DELETE FROM favorites WHERE user_id=:userId AND item_id=:itemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("itemId", itemId);
		
		template.update(sql, param);
	}
}
