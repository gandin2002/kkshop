package cn.bohoon.userInfo.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.annotation.JSONField;
import com.alipay.api.domain.Data;

import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.company.service.CompanyService;
import cn.bohoon.framework.SpringContextHolder;
import cn.bohoon.userInfo.domain.AuthState;
import cn.bohoon.userInfo.domain.MemberSource;
import cn.bohoon.userInfo.domain.UserSex;

/**
 * 用户信息表
 * 
 * @author hj 2017年11月9日,上午10:24:48
 */
@Entity
@Table(name = "t_user_info")
public class UserInfo {

	/**
	 * @Id @GenericGenerator( name="id_gen",
	 *     strategy="enhanced-table",parameters={
	 * @Parameter(name = "table_name", value =
	 *                 "t_id_generator"), @Parameter(name="initial_value",value=
	 *                 "10001"), @Parameter(name="increment_size",value="1")
	 *                 }) @GeneratedValue(generator="id_gen")
	 * 
	 * 
	 *                 //SQL：ALTER TABLE `t_user_info` auto_increment=10001
	 *                 如部署服务器 请修改 自增ID起始值 从10001开始
	 */

	@Id
	@Column(length=64)
	String id;
	
	Integer score = 0; // 积分
	Integer commendFriendId; // 推荐人
	String qualityRate="100";//质量率
	Boolean concern; // 关注
	Integer exp = 0; // 经验值
	String userLevel; // 会员等级
	String realname = ""; // 真实姓名
	String nickname = ""; // 昵称
	@DateTimeFormat(pattern="yyyy-MM-dd")
	Date birthday; // 生日
	String idcard; // 身份证号
	Data idcardExpire;// 身份证有效期
	String site; // 居住地址
	String phone; // 手机号
	String email;// 邮箱
	UserSex sex; // 性别
	@Enumerated(EnumType.STRING)
	MemberSource memberSource = MemberSource.PC;// 用户来源
	BigDecimal creaditAmount = new BigDecimal(0); // 信用额度
	BigDecimal remainingCredit = new BigDecimal(0); // 剩余信用额度
	BigDecimal expenditure = new BigDecimal(0); //消费额
	Date createTime = new Date();// 创建时间
	Date lastLoginTime; // 最后登录时间
	String wechatOpenid; // 微信小程序Openid：
	String wechatMpOpenid;//微信公众号Openid;
	String wechatUnioid; // 微信Unioid：
	String alipayid; // 支付宝alipayid
	String qqOpenid; // QQ-Openid
	String qq;//qq号
	String wechatId;//微信号
	@Column(columnDefinition="varchar(128) default '' ")   // 企业id默认值必须为空字符串，后期查询中不会有问题
	String companyId; // 企业ID
	@Column(columnDefinition="tinyint(2)")
	Boolean companyState = true;// 公司在岗状态 true 开启 false 禁用
	@Enumerated(EnumType.STRING)
	AuthState idCardAuthState = AuthState.NO_AUTHED ; // 是否实名
	String idCardAuthDesc = "" ;
	String idCardImg = ""; // 身份证图片
	String idCardImgReverse = "";//身份证反面
	@Enumerated(EnumType.STRING)
	AuthState companyAuthState = AuthState.NO_AUTHED; // 未认证 1申请中 ，2 已认证通过 ，3 认证拒绝
	
	Date bindingDate;// 绑定时间
	Integer departmentId = 0;// 部门ID
	String deptCode; // 部门编码
	
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	Date entryTime;// 加入部门时间
	String imageUrl = "/assets/images/defaultMan.png"; // 用户头像
	String transPass = "111111"; // 交易密码，新增用户默认为 六个1
	@Column(columnDefinition="tinyint(2)")
	Boolean status = true; // 启用(true)或禁用(false)
	@Column(columnDefinition="tinyint(2)")
	Boolean personInCharge = false ; // 部门负责人 (true)或禁用(false)
	@Column(columnDefinition="tinyint(2)")
	Boolean reviewStatus = false ; // 免审状态 (true)或禁用(false)
	String username;// 用户帐号 
	@Temporal(TemporalType.TIMESTAMP)
	Date applicationTime;  // 申请时间
	Integer rowNo = 0;//公司人员
	
	String defaultCompanyId = "1000000";
	
	
	
	public Date getApplicationTime() {
		return applicationTime;
	}

	public void setApplicationTime(Date applicationTime) {
		this.applicationTime = applicationTime;
	}

	@JSONField(serialize=false)
	@Transient
	public String getStartPhone() {
		String star = "";
		if (null != phone && !"".equals(phone)) {
			star = phone.substring(0, 3).concat("****").concat(phone.substring(7, 11));
		}
		return star;
	}

	@Transient
	@JSONField(serialize = false)
	public CompanyDepartment getDept() {
		if (null != departmentId) {
			CompanyDepartmentService service = SpringContextHolder.getBean(CompanyDepartmentService.class);
			CompanyDepartment dept = service.getParent(departmentId);
			if(!StringUtils.isEmpty(dept) &&  !StringUtils.isEmpty(dept.getTitle()) ) {
				String title = dept.getTitle() ;
				if(title.endsWith(">")) {
					title = title.substring(0, title.length()-1) ;
					dept.setTitle(title);
				}
				return dept ;
			}
		}
		return null;
	}
	
	@Transient
	@JSONField(serialize = false)
	public Company getCompany() {
		if (null != companyId) {
			CompanyService service = SpringContextHolder.getBean(CompanyService.class);
			return service.get(companyId) ;
		}
		return null;
	}

	@JSONField(serialize=false)
	@Transient
	public String getStarMail() {
		String star = "";
		if (null != email && !"".equals(email)) {
			int len = email.length();
			int ofx = email.indexOf("@");
			int start = ofx - 2;
			int end = ofx + 2 + 1;
			star = email.substring(0, start).concat("**@**").concat(email.substring(end, len));
		}
		return star;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Integer getCommendFriendId() {
		return commendFriendId;
	}

	public void setCommendFriendId(Integer commendFriendId) {
		this.commendFriendId = commendFriendId;
	}

	public Boolean getConcern() {
		return concern;
	}

	public void setConcern(Boolean concern) {
		this.concern = concern;
	}

	public Integer getExp() {
		return exp;
	}

	public void setExp(Integer exp) {
		this.exp = exp;
	}

	public String getUserLevel() {
		return userLevel;
	}

	public void setUserLevel(String userLevel) {
		this.userLevel = userLevel;
	}

	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	
	public String getWechatMpOpenid() {
		return wechatMpOpenid;
	}

	public void setWechatMpOpenid(String wechatMpOpenid) {
		this.wechatMpOpenid = wechatMpOpenid;
	}

	@JSONField(format="yyyy-MM-dd")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UserSex getSex() {
		return sex;
	}

	public void setSex(UserSex sex) {
		this.sex = sex;
	}

	public MemberSource getMemberSource() {
		return memberSource;
	}

	public void setMemberSource(MemberSource memberSource) {
		this.memberSource = memberSource;
	}

	public BigDecimal getCreaditAmount() {
		return creaditAmount;
	}

	public void setCreaditAmount(BigDecimal creaditAmount) {
		this.creaditAmount = creaditAmount;
	}

	public BigDecimal getRemainingCredit() {
		return remainingCredit;
	}

	public void setRemainingCredit(BigDecimal remainingCredit) {
		this.remainingCredit = remainingCredit;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getWechatOpenid() {
		return wechatOpenid;
	}

	public void setWechatOpenid(String wechatOpenid) {
		this.wechatOpenid = wechatOpenid;
	}

	public String getWechatUnioid() {
		return wechatUnioid;
	}

	public void setWechatUnioid(String wechatUnioid) {
		this.wechatUnioid = wechatUnioid;
	}

	public String getAlipayid() {
		return alipayid;
	}

	public void setAlipayid(String alipayid) {
		this.alipayid = alipayid;
	}

	public String getQqOpenid() {
		return qqOpenid;
	}

	
	public void setQqOpenid(String qqOpenid) {
		this.qqOpenid = qqOpenid;
	}
	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	public Date getBindingDate() {
		return bindingDate;
	}

	public void setBindingDate(Date bindingDate) {
		this.bindingDate = bindingDate;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
	public Date getEntryTime() {
		return entryTime;
	}

	public void setEntryTime(Date entryTime) {
		this.entryTime = entryTime;
	}

	public Boolean getCompanyState() {
		return companyState;
	}

	public void setCompanyState(Boolean companyState) {
		this.companyState = companyState;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Data getIdcardExpire() {
		return idcardExpire;
	}

	public void setIdcardExpire(Data idcardExpire) {
		this.idcardExpire = idcardExpire;
	}

	public String getTransPass() {
		return transPass;
	}

	public void setTransPass(String transPass) {
		this.transPass = transPass;
	}

	public AuthState getIdCardAuthState() {
		return idCardAuthState;
	}

	public void setIdCardAuthState(AuthState idCardAuthState) {
		this.idCardAuthState = idCardAuthState;
	}

	public String getIdCardImg() {
		return idCardImg;
	}

	public void setIdCardImg(String idCardImg) {
		this.idCardImg = idCardImg;
	}
	
	public String getIdCardAuthDesc() {
		return idCardAuthDesc;
	}

	public void setIdCardAuthDesc(String idCardAuthDesc) {
		this.idCardAuthDesc = idCardAuthDesc;
	}

	public AuthState getCompanyAuthState() {
		return companyAuthState;
	}

	public void setCompanyAuthState(AuthState companyAuthState) {
		this.companyAuthState = companyAuthState;
	}

	public Boolean getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(Boolean personInCharge) {
		this.personInCharge = personInCharge;
	}

	public Boolean getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(Boolean reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public BigDecimal getExpenditure() {
		return expenditure;
	}

	public void setExpenditure(BigDecimal expenditure) {
		this.expenditure = expenditure;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	public String getIdCardImgReverse() {
		return idCardImgReverse;
	}

	public void setIdCardImgReverse(String idCardImgReverse) {
		this.idCardImgReverse = idCardImgReverse;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public Integer getRowNo() {
		return rowNo;
	}

	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}

	public String getQualityRate() {
		return qualityRate;
	}

	public void setQualityRate(String qualityRate) {
		this.qualityRate = qualityRate;
	}

	public String getDefaultCompanyId() {
		return defaultCompanyId;
	}

	public void setDefaultCompanyId(String defaultCompanyId) {
		this.defaultCompanyId = defaultCompanyId;
	}
	
}
