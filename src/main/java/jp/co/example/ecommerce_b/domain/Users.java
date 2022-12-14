package jp.co.example.ecommerce_b.domain;

import lombok.Data;

@Data
public class Users {

	/** ID */
	private Integer id;

	/** 名前 */
	private String name;

	/** メールアドレス */
	private String email;

	/** パスワード */
	private String password;

	/** 郵便番号 */
	private String zipcode;

	/** 住所 */
	private String address;

	/** 電話番号 */
	private String telephone;

}
