package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.example.ecommerce_b.domain.Favorite;
import jp.co.example.ecommerce_b.repository.FavoriteRepository;

/**
 * 
 * @author hondayuki
 *
 */
@Service
public class FavoriteService {
	@Autowired
	private FavoriteRepository favoriteRepository;
	
	public void favorite(Integer userId, Integer itemId) {
		favoriteRepository.insert(userId, itemId);
	}
	
	public List<Favorite> showList(Integer userId) {
		return favoriteRepository.findByUserIdAndItemId(userId, null);
	}
	
	public List<Favorite> confirmFavorite(Integer userId, Integer itemId) {
		List<Favorite> favoriteList = favoriteRepository.findByUserIdAndItemId(userId, itemId);
		return favoriteList;
	}
	
	public void remove(Integer userId, Integer itemId) {
		favoriteRepository.delete(userId, itemId);
	}
}
