package jp.co.example.ecommerce_b.form;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * 
 * @author honda yuki
 *
 */
public class UpdateOrderForm {
	/** 宛先氏名 */
	@NotBlank(message = "名前を入力してください")
	private String destinationName;
	/** 宛先Email */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message = "メールアドレスの形式が不正です")
	private String destinationEmail;
	/** 宛先郵便番号 */
	@NotBlank(message = "郵便番号を入力してください")
	@Pattern(regexp = "^[0-9]{3}-[0-9]{4}$", message = "郵便番号はXXX-XXXXの形式で入力してください")
	private String destinationZipcode;
	/** 宛先住所 */
	@NotBlank(message = "住所を入力してください")
	private String destinationAddress;
	/** 宛先電話番号 */
	@NotBlank(message = "電話番号を入力してください")
	@Pattern(regexp = "^[0-9]{2,4}-[0-9]{2,4}-[0-9]{2,4}$", message = "電話番号はXXXX-XXXX-XXXXの形式で入力してください")
	private String destinationTel;
	/** 配達日 */
	@NotBlank(message = "配達日時を入力してください")
	private String deliveryDate;
	/** 配達時間 */
	@NotEmpty(message = "配達日時を入力してください")
	private String deliveryTime;
	/** 支払い方法 */
	private String paymentMethod;
	
	/**
	 * 配達日をDate型で返す
	 * 
	 * @return 配達日
	 * @throws ParseException
	 */
	public Date getDateDelivaryDate() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.parse(deliveryDate);
	}
	
	/**
	 * 配達時間をInteger型で返す
	 * 
	 * @return 配達時間
	 */
	public Integer getIntDelivaryTime() {
		return Integer.parseInt(deliveryTime);
	}
	
	/**
	 * 支払い方法をInteger型で返す
	 * 
	 * @return 支払い方法
	 */
	public Integer getIntPaymentMethod() {
		return Integer.parseInt(paymentMethod);
	}
	
	public String getDestinationName() {
		return destinationName;
	}
	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}
	public String getDestinationEmail() {
		return destinationEmail;
	}
	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}
	public String getDestinationZipcode() {
		return destinationZipcode;
	}
	public void setDestinationZipcode(
			String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}
	public String getDestinationAddress() {
		return destinationAddress;
	}
	public void setDestinationAddress(
			String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}
	public String getDestinationTel() {
		return destinationTel;
	}
	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}
	public String getDeliveryDate() {
		return deliveryDate;
	}
	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
	public String getDeliveryTime() {
		return deliveryTime;
	}
	public void setDeliveryTime(String deliveryTime) {
		this.deliveryTime = deliveryTime;
	}
	public String getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
	@Override
	public String toString() {
		return "UpdateOrderForm [destinationName="
				+ destinationName + ", destinationEmail="
				+ destinationEmail + ", destinationZipcode="
				+ destinationZipcode
				+ ", destinationAddress="
				+ destinationAddress + ", destinationTel="
				+ destinationTel + ", delivaryDate="
				+ deliveryDate + ", delivaryTime="
				+ deliveryTime + ", paymentMethod="
				+ paymentMethod + "]";
	}
}
