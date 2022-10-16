package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Supplier;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class SupplierCrudController {
    public static void saveSupplier(Supplier s) {

        if (s.getId().isEmpty() || s.getName().isEmpty() || s.getContact().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else {
            try {
                CrudUtil.execute("INSERT INTO Supplier VALUES (?,?,?,?,?,?)", s.getId(), s.getName(), s.getAddress(), s.getContact(), s.getEmail(),s.getStatus());
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier Saved..!").show();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public static ResultSet searchSupplier(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier WHERE sId=?", txtId.getText());
        return result;
    }

    public static void deleteSupplier(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please check Details.!").show();

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Supplier ?", ButtonType.YES,ButtonType.NO);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)){
                CrudUtil.execute("DELETE FROM Supplier WHERE sId=?",txtId.getText());
                new Alert(Alert.AlertType.CONFIRMATION, "Supplier Deleted.!").show();
            }
        }
    }

    public static void updateSupplier(Supplier s) throws SQLException, ClassNotFoundException {
        boolean isUpdated = CrudUtil.execute("UPDATE Supplier SET name=? ,address=?, contact=? , email=?,status=? WHERE sId=?",s.getName(),s.getAddress(),s.getContact(),s.getEmail(),s.getStatus(),s.getId());
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }
}
