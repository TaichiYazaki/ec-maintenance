package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import jp.co.example.ecommerce_b.domain.Item;

@SpringBootTest
class ItemrepositoryTest {

	@Autowired
	private Itemrepository itemRepository;

	@Autowired
	NamedParameterJdbcTemplate template;


	@Test
	public void FindAllItemList_18items() {
		System.out.println("全件検索するテスト開始");
		List<Item> itemList = itemRepository.findAllItemList();
		assertEquals(18, itemList.size(), "件数が一致しません");
		assertEquals("カツカレー", itemList.get(0).getName(), "安い順に並んでいません");
		assertEquals("えびナスカレー", itemList.get(17).getName(), "安い順に並んでいません");
		System.out.println("全件検索するテスト終了");
	}
}
