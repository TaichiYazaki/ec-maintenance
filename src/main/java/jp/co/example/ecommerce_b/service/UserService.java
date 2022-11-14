package jp.co.example.ecommerce_b.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jp.co.example.ecommerce_b.domain.Users;
import jp.co.example.ecommerce_b.repository.UserRepository;

/**
 * ユーザー情報の操作処理の設定
 * 
 * @author ishida fuya
 *
 */
@Service
@Transactional
public class UserService {

	@Autowired
	private UserRepository userrepository;
	
	/**
	 * メールアドレス重複チェック
	 * 
	 * @author ishida fuya
	 *
	 */
	public List<Users> emailCheck(String email) {
		return userrepository.emailCheck(email);
	}

    @Autowired
    PasswordEncoder passwordEncoder;

	/**
	 * ユーザー情報の追加処理
	 * 
	 * 
	 *
	 */
	public Users resgisterUser(Users user) {
		
		//パスワードのハッシュ化を行う
		System.out.println(user.getPassword());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		System.out.println(user.getPassword());
		
		return userrepository.registerUser(user);
	}
	
	/**
	 * ログイン処理
	 * メールアドレス、パスワードからユーザー情報を検索
	 * 
	 * @param email,password
	 * @return ユーザー情報
	 *
	 */
	public Users Login(String email,String password) {
		
		return userrepository.Login(email,password);
	}
	
	/**
	 * 
	 * メールアドレスからパスワード情報を検索
	 * 
	 * @param email
	 * @return パスワード情報
	 *
	 */
	//public List<Users> searchPassword(String email) {
		
	//	return userrepository.searchPassword(email);
	//}


}
