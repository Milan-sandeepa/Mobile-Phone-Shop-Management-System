package controller;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import model.Employee;
import model.Item;
import util.CrudUtil;
import util.ValidationUtil;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class EmployeFormController {
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtEmail;
    public JFXDatePicker txtJoinDate;
    public JFXComboBox<String> cmbGender;
    public JFXDatePicker txtBirthDay;
    public JFXTextField txtSalary;
    public JFXComboBox<String> cmbType;
    public TableView tblEmployee;
    public TableColumn colId;
    public TableColumn colType;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colGender;
    public TableColumn colBirthday;
    public TableColumn colJoinDate;
    public TableColumn colSalary;
    public TableColumn colEmail;
    public AnchorPane employeeContext;
    public JFXButton btnSaveButton;
    public JFXButton btnUpdateButton;
    public JFXButton btnDeleteButton;
    public Label txtStatus;
    public JFXTextField txtAddress;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colType.setCellValueFactory(new PropertyValueFactory("type"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colGender.setCellValueFactory(new PropertyValueFactory("gender"));
        colBirthday.setCellValueFactory(new PropertyValueFactory("birthDay"));
        colJoinDate.setCellValueFactory(new PropertyValueFactory("joinDate"));
        colSalary.setCellValueFactory(new PropertyValueFactory("salary"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));

        loadTypes();
        loadAllEmployees();
        ObservableList<String> list=FXCollections.observableArrayList("Male","Female");
        cmbGender.setItems(list);

        Pattern idPattern = Pattern.compile("^(E0)[0-9]{2,5}$");
        Pattern namePattern = Pattern.compile("^[A-z 0-9]{3,15}$");
        Pattern contactPattern = Pattern.compile("^07(7|6|8|1)[0-9]{7}$");
        Pattern salaryPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,30}$");

        map.put(txtId,idPattern);
        map.put(txtName,namePattern);
        map.put(txtContact,contactPattern);
        map.put(txtSalary,salaryPattern);
        map.put(txtAddress,addressPattern);

        btnSaveButton.setDisable(false);
        btnUpdateButton.setDisable(true);
        btnDeleteButton.setDisable(true);

    }

    private void loadAllEmployees() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Employe");

        ObservableList<Employee> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Employee(
                            result.getString("eId"),
                            result.getString("employeType"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("contact"),
                            result.getString("gender"),
                            result.getString("joinDate"),
                            result.getString("dob"),
                            result.getDouble("Salary"),
                            result.getString("Email")
                    )
            );
        }
        tblEmployee.setItems(obList);
    }

    private void loadTypes() {
        ObservableList typelist= FXCollections.observableArrayList();
        try {
            ResultSet resultSet=EmployeeCrudController.getType();

            while (resultSet.next()){
                typelist.add(resultSet.getString(1));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        cmbType.setItems(typelist);

    }

    public void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        ResultSet resultSet= CrudUtil.execute("SELECT * FROM Employe WHERE eId=?", txtId.getText());

        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else if (resultSet.next()){
            new Alert(Alert.AlertType.ERROR, "Product Id Already Exists.!").show();
        }else {
            Employee e = new Employee(
                    txtId.getText(),
                    cmbType.getValue(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContact.getText(),
                    cmbGender.getValue(),
                    String.valueOf(txtBirthDay.getValue()),
                    String.valueOf(txtJoinDate.getValue()),
                    Double.parseDouble(txtSalary.getText()),
                    txtEmail.getText()
            );
            EmployeeCrudController.saveEmployee(e);
            tablerefresh("EmployeForm");
        }
    }

    private void tablerefresh(String URI) throws IOException {
        employeeContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        employeeContext.getChildren().add(parent);
    }

    public void btnUpdateOnAction(ActionEvent event) throws IOException {
        Employee e=new Employee(
                txtId.getText(),
                cmbType.getValue(),
                txtName.getText(),
                txtAddress.getText(),
                txtContact.getText(),
                cmbGender.getValue(),
                String.valueOf(txtBirthDay.getValue()),
                String.valueOf(txtJoinDate.getValue()),
                Double.parseDouble(txtSalary.getText()),
                txtEmail.getText()
        );
        try {
            EmployeeCrudController.updateEmployee(e);
            tablerefresh("EmployeForm");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
        }

    }

    public void btnDeleteOnAction(ActionEvent event) throws IOException {
        try {
            EmployeeCrudController.deleteEmployee(txtId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tablerefresh("EmployeForm");
    }

    public void btnSearchOnAction(ActionEvent event) {
        try {
            ResultSet result=EmployeeCrudController.searchEmployee(txtId);
            if (result.next()) {
                txtName.setText(result.getString(3));
                txtContact.setText(result.getString(5));
                txtSalary.setText(result.getString(9));
                txtAddress.setText(result.getString(4));
                txtEmail.setText(result.getString(10));
                cmbGender.setValue(result.getString(6));
                cmbType.setValue(result.getString(2));
                txtJoinDate.setValue(LocalDate.parse(result.getString(8)));
                txtBirthDay.setValue(LocalDate.parse(result.getString(7)));
                btnUpdateButton.setDisable(false);
                btnDeleteButton.setDisable(false);

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map);;
            //if the response is a text field
            //that means there is a error
            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();// if there is a error just focus it
                btnSaveButton.setDisable(true);
            } else if (response instanceof Boolean) {
                btnSaveButton.setDisable(false);
            }
        }
    }
}
