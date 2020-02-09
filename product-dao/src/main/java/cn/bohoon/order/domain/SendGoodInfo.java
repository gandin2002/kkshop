package cn.bohoon.order.domain;

/**
 * 发货信息
 * @author Administrator
 *
 */
public class SendGoodInfo {

	private Integer id;//发货订单itemID
	private Integer num; //发货数量

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

}
