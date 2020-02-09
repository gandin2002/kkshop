package cn.bohoon.quotation.domain;

/**
 * 订单的状态
 */
public enum QuotationState {
	/*
	WATI_QUOTATION
	会员提交报价，待后台确认报价 。
	
	BE_ON_QUOTATION
	后台确认并修改报价，待后台审核，对应【审核中】。

	PASS_QUOTATION
	报价后台审核通过，对应【审核通过】。
	
	DENY_QUOTATION
	报价审核未通过，对应【审核拒绝】。
	*/
	
	WATI_QUOTATION {
        public String getName() {
            return "未议价";
        }
        public String getCheckState() {
            return "待议价";
        }
    },
	BE_ON_QUOTATION {
        public String getName() {
            return "已议价";
        }
        public String getCheckState() {
            return "审核中";
        }
    },
	PASS_QUOTATION {
        public String getName() {
            return "已通过";
        }
        public String getCheckState() {
            return "审核通过";
        }
    },
	DENY_QUOTATION {
        public String getName() {
            return "未通过";
        }
        public String getCheckState() {
            return "审核拒绝";
        }
    };

    public abstract String getName();
    public abstract String getCheckState();
}
