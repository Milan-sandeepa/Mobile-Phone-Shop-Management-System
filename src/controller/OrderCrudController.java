package controller;

import model.Order;
import model.OrderDetails;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderCrudController {
    public boolean saveOrder(Order order) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("INSERT INTO Orders VALUES(?,?,?,?)",
                order.getId(),
                order.getCustomerId(),
                order.getDate(),
                order.getTime()
        );
    }

    public boolean saveOrderDetails(ArrayList<OrderDetails>details) throws SQLException, ClassNotFoundException {
        for (OrderDetails temp:details
             ) {
            boolean isDetailsSaved=CrudUtil.execute("INSERT INTO Order_Details VALUES(?,?,?,?)",
                    temp.getOrderId(),
                    temp.getItemCode(),
                    temp.getQty(),
                    temp.getUnitPrice()
            );
            if (isDetailsSaved){
                if (!updateQty(temp.getItemCode(),temp.getQty())){
                 return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    private boolean updateQty(String itemCode, int qty) throws SQLException, ClassNotFoundException {
        return CrudUtil.execute("UPDATE Item SET qtyOnHand=qtyOnHand-? WHERE itemCode=?",qty,itemCode);
    }

    public String getOrderId() throws SQLException, ClassNotFoundException {
        ResultSet result=CrudUtil.execute("SELECT OrderId FROM Orders ORDER BY OrderId DESC LIMIT 1");
       Integer x=0;
        if (result.next()){
            x= Integer.parseInt(result.getString(1));
            ++x;
            return String.valueOf(x);
        }else {
            return "101";
        }
    }
}
