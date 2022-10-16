package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Item;
import util.CrudUtil;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class ItemCrudController {
    public static void saveItem(Item i) throws SQLException, ClassNotFoundException {

            try {
                CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?,?,?,?)",i.getItemCode(),i.getName(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice(),i.getSellingPrice(),i.getWarranty());
                new Alert(Alert.AlertType.CONFIRMATION, "Product Saved..!").show();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }

    }

    public static ResultSet searchItem(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item WHERE itemCode=?", txtId.getText());
        return result;
    }

    public static void updateItem(Item i) throws SQLException, ClassNotFoundException {
        boolean isUpdated = CrudUtil.execute("UPDATE Item SET name=? ,description=?, qtyOnHand=? , unitPrice=?,sellingPrice=?,warranty=? WHERE itemCode=?",i.getName(),i.getDescription(),i.getQtyOnHand(),i.getUnitPrice(),i.getSellingPrice(),i.getWarranty(),i.getItemCode());
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }

    public static void deleteItem(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please check Details.!").show();

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Item ?", ButtonType.YES,ButtonType.NO);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)){
                CrudUtil.execute("DELETE FROM Item WHERE itemCode=?",txtId.getText());
                new Alert(Alert.AlertType.CONFIRMATION, "Item Deleted.!").show();
            }
        }
    }

    public static ArrayList<String> getItemCodes() throws SQLException, ClassNotFoundException {
        ResultSet result=CrudUtil.execute("SELECT itemCode FROM Item");

        ArrayList<String> codeList=new ArrayList<>();
        while (result.next()){
            codeList.add(result.getString(1));
        }
        return codeList;
    }

    public static Item getItem(String code) throws SQLException, ClassNotFoundException {
        ResultSet result =CrudUtil.execute("SELECT * FROM Item WHERE itemCode=?",code);
        if (result.next()){
            return new Item(
                    result.getString(1),
                    result.getString(2),
                    result.getString(3),
                    result.getInt(4),
                    result.getDouble(5),
                    result.getDouble(6),
                    result.getString(7)
            );
        }
        return null;
    }
}
