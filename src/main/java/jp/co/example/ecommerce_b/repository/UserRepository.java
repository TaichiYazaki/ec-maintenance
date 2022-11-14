package jp.co.example.ecommerce_b.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import jp.co.example.ecommerce_b.domain.Users;

/**
 * ユーザー情報の操作処理の設定
 * 
 * @author ishida fuya
 *
 */
@Repository
public class UserRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private static final RowMapper<Users>USER_ROW_MAPPER = (rs,i) ->{
		Users user = new Users();
		user.setId(rs.getInt("id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		user.setPassword(rs.getString("password"));
		
		return user;
	};
	
	/**
	 * メールアドレスからユーザー情報の検索処理
	 * @param email
	 * @return 
	 *
	 */
	public List<Users> emailCheck(String email) {
		String sql = "SELECT id,name,email,zipcode,address,telephone,password FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource ().addValue("email", email);
		
		List<Users>userList = template.query(sql, param, USER_ROW_MAPPER);
		return userList;
	}
	
	
	
	/**
	 * ユーザー情報の追加処理
	 * 
	 * @author ishida fuya
	 *
	 */
	public Users registerUser(Users user) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(user);
		String insertsql = "INSERT INTO users(name,email,password,zipcode,address,telephone) "
				+ "VALUES(:name,:email,:password,:zipcode,:address,:telephone)";
		
		template.update(insertsql, param);
		return user;
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
		//idだけ取ることは可能？？
		String sql = "SELECT id,name,email,zipcode,address,telephone,password FROM users WHERE email = :email AND password = :password";
		SqlParameterSource param = new MapSqlParameterSource ().addValue("email", email).addValue("password", password);
		
		List<Users>userList = template.query(sql, param, USER_ROW_MAPPER);
		return userList.get(0);
	}
	
	/**
	 * パスワードのハッシュ化
	 * テーブルに登録された、ハッシュ化されたパスワードを取得する。
	 * 
	 * @param email
	 * @return password
	 *
	 */
	public Users searchPassword(String email) {
		//idだけ取ることは可能？？
		String sql = "SELECT id,name,email,zipcode,address,telephone,password FROM users WHERE email = :email";
		SqlParameterSource param = new MapSqlParameterSource ().addValue("email", email);
		
		List<Users>userList = template.query(sql, param, USER_ROW_MAPPER);
		return userList.get(0);
	}
	
	

}
