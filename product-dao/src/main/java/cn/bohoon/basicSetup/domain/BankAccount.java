package cn.bohoon.basicSetup.domain;
/**
 * 银行账户
 * @author HJ
 * 2018年1月12日,下午1:35:35
 */
public class BankAccount {

	private String id;
	private String bankName; // 银行名字
	private String openingBank;// 开户支行
	private String address;// 开户地址
	private String cardBank;// 银行卡号
	private Boolean display = true; // 显示状态， true 为显示 false 不显示
	private Integer province; //省份
	private Integer city;//市
	private Integer area;//地区

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getOpeningBank() {
		return openingBank;
	}

	public void setOpeningBank(String openingBank) {
		this.openingBank = openingBank;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCardBank() {
		return cardBank;
	}

	public void setCardBank(String cardBank) {
		this.cardBank = cardBank;
	}

	public Boolean getDisplay() {
		return display;
	}

	public void setDisplay(Boolean display) {
		this.display = display;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public Integer getArea() {
		return area;
	}

	public void setArea(Integer area) {
		this.area = area;
	}
}
