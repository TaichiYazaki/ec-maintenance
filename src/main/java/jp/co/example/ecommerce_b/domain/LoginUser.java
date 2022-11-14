package jp.co.example.ecommerce_b.domain;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

/**
 * ユーザーのログイン情報を表すドメイン
 * 
 * @author ishida fuya
 *
 */
public class LoginUser extends User{
	
	private static final long serialVersionUID = 1L;
	/** 管理者情報 */
	private final Users user;
	
	/**
	 * 通常の管理者情報に加え、認可用ロールを設定する。
	 * 
	 * @param users ユーザー情報
	 * @param authorityList 権限情報が入ったリスト
	 */
	public LoginUser(Users user, Collection<GrantedAuthority> authorityList) {
		super(user.getEmail(),user.getPassword(),authorityList);
		this.user = user;
	}

	/**
	 * ユーザー情報を返します.
	 * 
	 * @return ユーザー情報
	 */
	public Users Getusers() {
		return user;
	}

}
