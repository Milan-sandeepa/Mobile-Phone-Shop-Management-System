package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Repair;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RepairCrudController {

    public static void saveItem(Repair r) {
        try {
            CrudUtil.execute("INSERT INTO Repair VALUES (?,?,?,?,?,?,?)", r.getId(),r.getCustomerId(),r.getDescription(),r.getDate(),r.getTime(),r.getPrice(),r.getStatus());
            new Alert(Alert.AlertType.CONFIRMATION, "Repair Saved..!").show();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateRepair(Repair r) throws SQLException, ClassNotFoundException {
        boolean isUpdated = CrudUtil.execute("UPDATE Repair SET cId=? ,description=?,cost=?,status=? WHERE rId=?",r.getCustomerId(),r.getDescription(),r.getPrice(),r.getStatus(),r.getId());
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }

    public static ResultSet searchRepair(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Repair WHERE rId=?", txtId.getText());
        return result;
    }

    public static void deleteRepair(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please check Details.!").show();

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete ?", ButtonType.YES,ButtonType.NO);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)){
                CrudUtil.execute("DELETE FROM Repair WHERE rId=?",txtId.getText());
                new Alert(Alert.AlertType.CONFIRMATION, "Item Deleted.!").show();
            }
        }
    }
}
