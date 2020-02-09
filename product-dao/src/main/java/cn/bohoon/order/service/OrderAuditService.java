package cn.bohoon.order.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.bohoon.company.entity.CompanyDepartment;
import cn.bohoon.company.service.CompanyDepartmentService;
import cn.bohoon.framework.service.BaseService;
import cn.bohoon.order.dao.OrderAuditStateDao;
import cn.bohoon.order.domain.OrderAuditState;
import cn.bohoon.order.entity.Order;
import cn.bohoon.order.entity.OrderAudit;
import cn.bohoon.userInfo.entity.UserInfo;
import cn.bohoon.userInfo.service.UserInfoService;

@Service
public class OrderAuditService extends BaseService<OrderAudit,Integer>{

	@Autowired
	OrderAuditStateDao orderAuditStateDao;
	@Autowired
	CompanyDepartmentService companyDepartmentService;
	@Autowired
	UserInfoService userInfoService;
	
	@Autowired
	OrderAuditService(OrderAuditStateDao orderAuditStateDao){
        super(orderAuditStateDao);
    }
	
	
	// 通过订单获取和部门获取--》 该部门的审核状态订单
	public void getOrderAndDept(Order order,Integer departmentId) throws Exception{
		
		
		
		// 保存当前部门的完成免审
		OrderAudit orderAu = this.select("from OrderAudit where orderId=? and departmentId=?",order.getId(),departmentId );
		orderAu.setOrderAuditState(OrderAuditState.FINISH_AUDIT);
		this.save(orderAu);
		
		UserInfo order_user = userInfoService.get(order.getUserId());
		CompanyDepartment departments = companyDepartmentService.get(departmentId);
		
		companyDepartmentService.getDeptOrOrderState(departments.getPid(), order_user, order);
		
		
	}
}
