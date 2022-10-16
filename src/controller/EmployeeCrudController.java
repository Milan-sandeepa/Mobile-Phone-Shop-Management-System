package controller;

import com.jfoenix.controls.JFXTextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Employee;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class EmployeeCrudController {
    public static void saveEmployee(Employee em) {
        if (em.getId().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else {
            try {
                CrudUtil.execute("INSERT INTO Employe VALUES (?,?,?,?,?,?,?,?,?,?)",em.getId(),em.getType(),em.getName(),em.getAddress(),em.getContact(),em.getGender(),em.getBirthDay(),em.getJoinDate(),em.getSalary(),em.getEmail());
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Saved..!").show();

            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static ResultSet getType() throws SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM Employe_Type");
        return resultSet;
    }

    public static ResultSet searchEmployee(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Employe WHERE eId=?", txtId.getText());
        return result;
    }

    public static void updateEmployee(Employee e) throws SQLException, ClassNotFoundException {
        boolean isUpdated = CrudUtil.execute("UPDATE Employe SET employeType=? ,name=?, address=? , contact=?,gender=?,dob=?,joinDate=?,Salary=?,Email=? WHERE eId=?",e.getType(),e.getName(),e.getAddress(),e.getContact(),e.getGender(),e.getBirthDay(),e.getJoinDate(),e.getSalary(),e.getEmail(),e.getId());
        if (isUpdated){
            new Alert(Alert.AlertType.CONFIRMATION, "Updated!").show();
        }else{
            new Alert(Alert.AlertType.WARNING, "Try Again!").show();
        }
    }

    public static void deleteEmployee(JFXTextField txtId) throws SQLException, ClassNotFoundException {
        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please check Details.!").show();

        }else{
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure Delete Employee ?", ButtonType.YES,ButtonType.NO);

            Optional<ButtonType> buttonType = alert.showAndWait();

            if (buttonType.get().equals(ButtonType.YES)){
                CrudUtil.execute("DELETE FROM Employe WHERE eId=?",txtId.getText());
                new Alert(Alert.AlertType.CONFIRMATION, "Employee Deleted.!").show();
            }
        }
    }
}
