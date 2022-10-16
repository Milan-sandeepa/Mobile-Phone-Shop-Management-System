package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class CustomerCrudController {
    public static void saveCustomer(Customer c){

        if (c.getId().isEmpty() || c.getName().isEmpty() || c.getContact().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else {
            try {
                CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?,?,?)", c.getId(), c.getName(), c.getAddress(), c.getContact(), c.getEmail());
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Saved..!").show();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet searchCustomer(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer WHERE cId=?", txtId.getText());
        return result;
    }

    public static void updateCustomer(Customer c) throws SQLException, ClassNotFoundException {

        boolean isUpdated = CrudUtil.execute("UPDATE Customer SET name=? ,address=?, contact=? , email=? WHERE cId=?",c.getName(),c.getAddress(),c.getContact(),c.getEmail(),c.getId());
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }


    public static void deleteCustomer(JFXTextField txtId) throws SQLException, ClassNotFoundException {

        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please check Details.!").show();

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Customer ?",ButtonType.YES,ButtonType.NO);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)){
                CrudUtil.execute("DELETE FROM Customer WHERE cId=?",txtId.getText());
                new Alert(Alert.AlertType.CONFIRMATION, "Customer Deleted.!").show();
            }
        }
    }

    public static ArrayList<String> getCustomerIds() throws SQLException, ClassNotFoundException {
        ResultSet result=CrudUtil.execute("SELECT cId FROM Customer");
        ArrayList<String>ids=new ArrayList<>();
        while (result.next()){
            ids.add(result.getString(1));
        }
        return ids;
    }

    public static Customer getCustomer(String id) throws SQLException, ClassNotFoundException {
        ResultSet result=CrudUtil.execute("SELECT * FROM Customer WHERE cId=?",id);
        if (result.next()){
            return new Customer(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getString(4),
                    result.getString(5)
            );
        }
        return null;
    }
}
