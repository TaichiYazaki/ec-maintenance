package jp.co.example.ecommerce_b.controller;

import static org.junit.jupiter.api.Assertions.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

// MockMvcを利用するために必要
@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	/**
	 * ユーザー登録画面が正しく返されるか検証 ①レスポンスのHTTPステータスコードは正しいか　
	 * ②指定のviewを返すか
	 */
	@Test
	void toRegisterUser処理でregister_userが渡される() throws Exception {
		this.mockMvc.perform(get("/user/toRegisterUser")).andExpect(status().isOk())
		.andExpect(view().name("register_user"));
	}

}
