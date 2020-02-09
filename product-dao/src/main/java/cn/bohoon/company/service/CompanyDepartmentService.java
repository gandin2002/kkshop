package cn.bohoon.company.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import cn.bohoon.basicSetup.entity.SysParam;
import cn.bohoon.basicSetup.service.SysParamService;
import cn.bohoon.company.dao.CompanyDepartmentDao;
import cn.bohoon.company.entity.Company;
import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.company.entity.CompanyDeptNoExamine;
import cn.bohoon.company.helper.CreditFlowHelper;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.domain.OrderAuditState;
import cn.bohoon.order.domain.OrderState;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderAudit;
import cn.bohoon.order.service.OrderAuditService;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;
import cn.bohoon.util.ConvertUtils;

@Service
@Transactional
public class CompanyDepartmentService extends BaseService<CompanyDepartment, Integer> {

	@Autowired
	CompanyDepartmentDao companyDepartmentDao;

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CompanyDepartmentService(CompanyDepartmentDao companyDepartmentDao) {
		super(companyDepartmentDao);
	}
	
	@Autowired
	CompanyDeptNoExamineService companyDeptNoExamineService;
	
	@Autowired
	OrderAuditService orderAuditService;
	
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	SysParamService sysParamService;
	
	
	
	/**
	 * 订单审核的免审状态
	 * @throws ParseException 
	 * @throws NumberFormatException 
	 */
	public void getDeptOrOrderState(Integer departmentId,UserInfo userInfo ,Order order) throws Exception{
		
		if (StringUtils.isEmpty(departmentId) || departmentId == 0){
			 OrderAudit oas = new OrderAudit();
			 oas.setOrderId(order.getId());
			 oas.setDepartmentId(0);
			 if (userInfo.getReviewStatus()){
				 
				 Company company = companyService.get(userInfo.getCompanyId());
				 
				// 判断企业免审状态
					if (company.isAudit()){
						
						// 为免审状态
						
						
						// 预计收货时间
						SysParam sysParam = sysParamService.get(0);
						String requirementDate = order.getRequirementDate();
						
						
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						Date newDate = CreditFlowHelper.getAddOrReduce(sdf.parse(requirementDate), new Integer(sysParam.getValue()));
						
						order.setOrderState(OrderState.WAIT_BUYER_PAY);
						order.setConsignmentDate(sdf.format(newDate));
						
					}else{
						
						order.setOrderState(OrderState.INIT_STATE);
					}
				 
				 
			 }
			 orderAuditService.save(oas);
			 
		}else{
		
		
		
		// 先判断用户的部门，是否为部门负责人
		CompanyDepartment cdept = this.select("from CompanyDepartment where id=?", departmentId);
		OrderAudit orderAudit ;
		
		// 获取用户对应的免审状态
		CompanyDeptNoExamine noExamine = companyDeptNoExamineService.select("from CompanyDeptNoExamine where departmentId=? and userInfoId=?",departmentId,userInfo.getId() );
		
		orderAudit = new  OrderAudit(); 
		orderAudit.setDepartmentId(departmentId);
		orderAudit.setOrderId(order.getId());
		order.setOrderState(OrderState.WAIT_CONFIRM_SALES);
		
		// 该会员是该部门的负责人  -->  或者有免审状态  --> 亦或者该部门没有负责人
		if (userInfo.getId().equals( cdept.getResponsibleId() ) || null == cdept.getResponsibleId() || null != noExamine ){
			
			 orderAudit.setOrderAuditState(OrderAuditState.FINISH_AUDIT);
			 
			 
			 // 需要递归到上级部门
			
				 
				 getDeptOrOrderState(cdept.getPid(),userInfo,order);
			 
			
		}
		
		orderAuditService.save(orderAudit);
		
		}
	}
	

	/**
	 * 通过级别获取
	 * @param level
	 * @param pid
	 * @return
	 */
	public List<CompanyDepartment> getListParentName(Integer level, Integer pid) {
		List<CompanyDepartment> list = new ArrayList<CompanyDepartment>();
		for (int i = level; i > 0; i--) {
			CompanyDepartment CompanyDepartment = this.select(" from CompanyDepartment where level = ? and id = ? ", i,
					pid);
			list.add(CompanyDepartment);
			pid = CompanyDepartment.getPid();
		}
		Collections.reverse(list);
		return list;
	}

	// 递归排序
	public List<CompanyDepartment> recursionDepartment(CompanyDepartment dept) {
		String hql = " from CompanyDepartment where pid = ?  and companyId=?" ;
		List<CompanyDepartment> childNodes = list(hql, dept.getId(),dept.getCompanyId());
		List<CompanyDepartment> departmetList = new ArrayList<CompanyDepartment>();
		departmetList.add(dept);
		for (CompanyDepartment child : childNodes) {
			List<CompanyDepartment> lists = recursionDepartment(child);
			if (!lists.isEmpty())
				departmetList.addAll(lists);
		}
		return departmetList;
	}
	
	
	/**
	 * 通过节点获取上级节点   -->  递归
	 */
	public List<CompanyDepartment> getNodesDepartment(Integer id,String companyId){
		
		String hql = " from CompanyDepartment where id = ?  and companyId=?" ;
		List<CompanyDepartment> deptList = new ArrayList<CompanyDepartment>();
		CompanyDepartment dept = this.select(hql, id,companyId);
		
		if (dept != null){
			
			deptList.add(dept);
			List<CompanyDepartment> nodesDepartment = getNodesDepartment(dept.getPid(),companyId);
			deptList.addAll(nodesDepartment);
		}
		
		
		return deptList;
	}

	/**
	 * 遍历部门信息
	 * 
	 * @param id
	 * @return
	 */
	public List<CompanyDepartment> departmentSorting(String id) {
		List<CompanyDepartment> parentNodes = list(" from CompanyDepartment where level = 1 and companyId = ? ", id);
		List<CompanyDepartment> departmetList = new ArrayList<CompanyDepartment>();
		for (CompanyDepartment department : parentNodes) {
			List<CompanyDepartment> list1 = recursionDepartment(department);
			if (!list1.isEmpty()) {
				departmetList.addAll(list1);
			}
		}
		return departmetList;
	}

	public CompanyDepartment getParent(Integer id) {
		String hql = "SELECT id ,pid , level,paths as title FROM (";
		hql += "select id ,pid , level  , @pathnodes:= IF( pid =0,concat(title,'>'), CONCAT_WS(concat(title,'>'),";
		hql += "IF( LOCATE( CONCAT('|',pid,':'),@pathall) > 0  ,  SUBSTRING_INDEX( SUBSTRING_INDEX(@pathall,CONCAT('|',pid,':'),-1),'|',1),@pathnodes ) ,''  ) )paths";
		hql += ",@pathall:=CONCAT(@pathall,'|',id,':', @pathnodes ,'|') pathall  from t_company_department , ";
		hql += "(SELECT @le:=0,@pathlevel:='', @pathall:='',@pathnodes:='') vv ORDER BY  pid,id ) src ";
		hql += " where id=" + id + " ORDER BY id";
		List<Map<String, Object>> list = jdbcTemplate.queryForList(hql);
		CompanyDepartment comDept = new CompanyDepartment();
		if (null != list && list.size() > 0) {
			Map<String, Object> result = list.get(0) ;
			comDept.setId(ConvertUtils.parseInteger(result.get("id") + ""));
			comDept.setPid(ConvertUtils.parseInteger(result.get("pid") + ""));
			comDept.setLevel(ConvertUtils.parseInteger(result.get("level") + ""));
			comDept.setTitle(result.get("title") + "");
		}
		return comDept;
	}

	public String findMaxCode(Integer pid,String companyId) {
		String hql = "SELECT MAX(deptCode) from CompanyDepartment where pid ="+pid +" and companyId=?";
	    String vCode = select(hql, String.class,companyId) ;
		return vCode;
	}
	
	
	
	/**
	 * 通过用户获取所对应的部门负责人
	 */
	public List<CompanyDepartment> getuserInfoIdToDept(String userInfoId){
		
		List<CompanyDepartment> list = this.list("from CompanyDepartment where responsibleId=?",userInfoId);
		
		return list;
	}
	
	/**
	 * 是否管理人员
	 * @return
	 */
	public Boolean isManager(Integer departmentId){
		CompanyDepartment cdept = this.select("from CompanyDepartment where id=?",departmentId);
		return cdept == null ? false: true;
	}
	
	
	

	/**
	 * 修改用户关联的部门负责人信息
	 */
	public void updateUserInfoAndDept(UserInfo userInfo,String dept){
		
		// 1.先删除关联的部门负责人信息
		this.execute("update  CompanyDepartment set responsibleId=? where responsibleId=?", null,userInfo.getId());
		
		if (StringUtils.isEmpty(dept)){
			
			userInfo.setPersonInCharge(false);
			
			return;
		}
		
		// 新增管理关联关系
		String[] deptId = dept.split(",");
		
		userInfo.setPersonInCharge(false);
		for (String de : deptId){
			
			Integer id = Integer.parseInt(de);
			CompanyDepartment cd = this.get(id);
			
			if (userInfo.getDepartmentId().equals(id)){
				
				
				// 则说明是该员工是该部门的负责人
				userInfo.setPersonInCharge(true);
			}
			
			// 删除对应id的状态
			if (!StringUtils.isEmpty( cd.getResponsibleId() )){
				
				UserInfo deptUser = userInfoService.get( cd.getResponsibleId() );
			
			if (deptUser.getDepartmentId().equals( cd.getResponsibleId() )){
				
				deptUser.setPersonInCharge(false);
				
				userInfoService.update(deptUser);
			}
			}
			
			cd.setResponsibleId(userInfo.getId());
			
			this.update(cd);
			
			
		}
		
		
	}
	
	
	/**
	 * 修改部门免审状态信息
	 */
	   public void updateUsernoExamine(UserInfo userInfo,String noExamine){
		
		// 1.先删除关联的部门负责人信息
		   String hql = "delete from CompanyDeptNoExamine where userInfoId=?";
		   companyDeptNoExamineService.execute(hql, userInfo.getId());
		
		
		   if (StringUtils.isEmpty(noExamine)){
			   userInfo.setReviewStatus(false);
				return;
			}
		   
		
			String[] noExamines = noExamine.split(",");
			
			userInfo.setReviewStatus(false);
			for (String de : noExamines){
				
				int noEx = Integer.parseInt(de);
				
				if (noEx == 0){
					userInfo.setReviewStatus(true);
				}else{
					
					
					CompanyDeptNoExamine deptNoExamin = new CompanyDeptNoExamine();
					deptNoExamin.setUserInfoId(userInfo.getId());
					deptNoExamin.setDepartmentId(noEx);
					
					companyDeptNoExamineService.save(deptNoExamin);
				}
				
			}
	}
	
	   /**
	    * 获取部门所有员工
	    * @param departments
	    * @return
	    */
	public List<UserInfo> departmentUserInfo(List<CompanyDepartment>  departments){
		List<UserInfo> list = new ArrayList<UserInfo>();
		for (CompanyDepartment companyDepartment : departments) {
			 List<UserInfo> ulist = userInfoService.list(" from UserInfo where departmentId = ? ", companyDepartment.getId());
			 if(!ulist.isEmpty()){
				 list.addAll(ulist);
			 }
		}
		return list;
	}
}
