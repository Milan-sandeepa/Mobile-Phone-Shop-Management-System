package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Return;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ReturnCrudController {
    public static ResultSet searchOrder(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Order_details WHERE OrderId=?", txtId.getText());
        return result;
    }

    public static ResultSet searchName(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Orders WHERE OrderId=?", txtId.getText());
        String id;
        if (result.next()){
            id=result.getString(2);
            ResultSet resultcId = CrudUtil.execute("SELECT * FROM Customer WHERE cId=?", id);
            return resultcId;
        }
        return result;
    }

    public static ResultSet searchDate(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Orders WHERE OrderId=?", txtId.getText());
        return result;
    }

    public static void saveReturn(Return r) {
        if (r.getCustomerName().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else {
            try {
                CrudUtil.execute("INSERT INTO Returns VALUES (?,?,?,?,?)", r.getId(), r.getOrderId(), r.getCustomerName(),r.getNote(),r.getAmount());
                new Alert(Alert.AlertType.CONFIRMATION, "Order Returned").show();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void returnOrder(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        CrudUtil.execute("DELETE FROM Orders WHERE orderId=?",txtId.getText());
    }

    public static ResultSet searchReturn(JFXTextField txtRid) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM returns WHERE rId=?", txtRid.getText());
        return result;
    }

    public static void deleteReturn(JFXTextField txtRid) throws SQLException, ClassNotFoundException {
        if (txtRid.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please check Details.!").show();

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Return ?", ButtonType.YES,ButtonType.NO);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)){
                CrudUtil.execute("DELETE FROM Returns WHERE rId=?",txtRid.getText());
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Deleted.!").show();
            }
        }
    }

    public String getReturnId() throws SQLException, ClassNotFoundException {
        ResultSet result=CrudUtil.execute("SELECT rId FROM Returns ORDER BY rId DESC LIMIT 1");
        Integer x=0;
        if (result.next()){
            x= Integer.parseInt(result.getString(1));
            ++x;
            return String.valueOf(x);
        }else {
            return "10001";
        }
    }
}
