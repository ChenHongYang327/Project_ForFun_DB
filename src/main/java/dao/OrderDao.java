package dao;

import member.bean.Order;
import java.util.List;

public interface OrderDao {
    int insert(Order order);
    
	int selectTenantByID(int OrderId);
	
	int selectPublishByID(int orderId);
	
	boolean changeOrderStatus (int orderID, int status);
	
	Order selectByID(int OrderId);
	
	Order selectByPublishID(int PublishId);
	
	int insertEvaluation(Order evaluation, int orderId);
	
	List<Order> selectAllBySatus (int orderStatus, int tenantId);
	

}
