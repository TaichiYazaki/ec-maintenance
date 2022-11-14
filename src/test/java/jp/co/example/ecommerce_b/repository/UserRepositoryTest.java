package jp.co.example.ecommerce_b.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.repository.UserRepository;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;


@SpringBootTest
class UserRepositoryTest {

	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	
	
	@BeforeEach
	//ユーザー登録処理のテスト
	public void testregisterUsert() {
		
	//テストデータを用意
	System.out.println("ユーザー登録処理開始。");
	Users user = new Users();
	user.setName("単体テスト太郎4");
	user.setEmail("testtarou4@sample.com");
	user.setZipcode("184-0000");
	user.setTelephone("0120-0000-0004");
	user.setAddress("東京都");
	user.setPassword("testtest4");
	userRepository.registerUser(user);
	
	System.out.println("ユーザー登録完了。");

	
	}
	
	@Test
	//メールアドレスの重複確認処理のテスト(ユーザー登録後なので重複ありの結果になる)
	public void testemailCheck1() {
		System.out.println("メールアドレス重複確認処理1開始");
		
		List<Users> resultEmailCheck = userRepository.emailCheck("testtarou4@sample.com");
		
		assertEquals(true,resultEmailCheck.isEmpty() , "メールアドレス重複あり");
		
		System.out.println("メールアドレス重複確認処理1テスト終了");
		
	}
	
	@Test
	//メールアドレスの重複確認処理のテスト(重複しないメールアドレス)
	public void testemailCheck2() {
		System.out.println("メールアドレス重複確認処理2開始");
		
		System.out.println("確認するメールアドレスのレコードを削除");
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("email", "testtarou5@sample.com");
		template.update("delete from users where email = :email", param);
				
		//DBに存在しないメールアドレスを入力
		System.out.println("該当するメールアドレスのレコードを検索");
		List<Users> resultEmailCheck = userRepository.emailCheck("testtarou5@sample.com");
		
		assertEquals(true,resultEmailCheck.isEmpty() , "メールアドレス重複あり");
		
		System.out.println("メールアドレス重複確認処理2テスト終了");
		
	}
	
	@Test
	public void testLogin1() {
		//ログイン処理のテスト（ログイン成功）
		System.out.println("ログイン確認処理1開始");
	
		System.out.println("メールアドレス、パスワードを元に検索処理実行");
		Users resultlogin = userRepository.Login("testtarou4@sample.com","testtest4");
		
	
		assertEquals("単体テスト太郎4",resultlogin.getName() , "名前が登録されていません");
		assertEquals("testtarou4@sample.com",resultlogin.getEmail() , "メールアドレスが登録されていません");
		assertEquals("184-0000",resultlogin.getZipcode() , "郵便番号が登録されていません");
		assertEquals("0120-0000-0004",resultlogin.getTelephone(),"電話番号が登録されていません");
		assertEquals("東京都",resultlogin.getAddress(),"住所が登録されていません");
		assertEquals("testtest4",resultlogin.getPassword(),"パスワードが登録されていません");
		
		System.out.println("ログイン確認処理1テスト終了");
	}
	
	@Test
	public void testLogin2() {
		//ログイン処理のテスト（ログイン失敗）
		System.out.println("ログイン確認処理2開始");
		
		
		System.out.println("ログインの確認をするレコードを削除");
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("email", "testtarou5@sample.com");
		template.update("delete from users where email = :email", param);
		
		
		//メールアドレス、パスワードを元に検索処理実行（ユーザー情報が存在しないためログインできない）
		System.out.println("メールアドレス、パスワードを元に検索処理実行");
		Users resultlogin = userRepository.Login("testtarou5@sample.com","testtest5");
	
		assertEquals("単体テスト太郎5",resultlogin.getName() , "名前が登録されていません");
		assertEquals("testtarou5@sample.com",resultlogin.getEmail() , "メールアドレスが登録されていません");
		assertEquals("184-0000",resultlogin.getZipcode() , "郵便番号が登録されていません");
		assertEquals("0120-0000-0005",resultlogin.getTelephone(),"電話番号が登録されていません");
		assertEquals("東京都",resultlogin.getAddress(),"住所が登録されていません");
		assertEquals("testtest5",resultlogin.getPassword(),"パスワードが登録されていません");
		
		System.out.println("ログイン確認処理2終了");
	}
	
	@AfterEach
	public void tearDownAfterClass() throws Exception {
		MapSqlParameterSource param = new MapSqlParameterSource().addValue("email", "testtarou4@sample.com");
		template.update("delete from users where email = :email", param);
		System.out.println("入れたデータを削除。");
	}

	
	
	

}
