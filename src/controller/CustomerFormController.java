package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
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
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class CustomerFormController {


    public JFXTextField txtId;
    public JFXTextField txtName;
    //public JFXTextArea txtAddress;
    public JFXTextField txtContact;
    public JFXTextField txtEmail;
    public TableView<Customer> tblCustomers;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colMail;
    public TableColumn colStatus;
    public AnchorPane customerContext;
    public JFXButton btnSaveButton;
    public JFXButton btnDeleteButton;
    public JFXButton btnUpdateButton;
    public JFXTextField txtAddress;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();


    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colMail.setCellValueFactory(new PropertyValueFactory("email"));

        loadAllCustomers();

        Pattern idPattern = Pattern.compile("^(C0)[0-9]{2,5}$");
        Pattern namePattern = Pattern.compile("^[A-z 0-9]{3,15}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern contactPattern = Pattern.compile("^07(7|6|8|1)[0-9]{7}$");

        map.put(txtId,idPattern);
        map.put(txtName,namePattern);
        map.put(txtAddress,addressPattern);
        map.put(txtContact,contactPattern);

        btnSaveButton.setDisable(false);
        btnUpdateButton.setDisable(true);
        btnDeleteButton.setDisable(true);

    }


    private void loadAllCustomers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Customer");

        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Customer(
                            result.getString("cId"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("contact"),
                            result.getString("email")
                    )
            );
        }
        tblCustomers.setItems(obList);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {
        ResultSet resultSet=CrudUtil.execute("SELECT * FROM Customer WHERE cId=?", txtId.getText());
        if (resultSet.next()){
            new Alert(Alert.AlertType.ERROR, "Customer Already Exists.!").show();
        }else {
            Customer c = new Customer(
                    txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContact.getText(),
                    txtEmail.getText()
            );
            CustomerCrudController.saveCustomer(c);
            tablerefresh("CustomerForm");
        }
    }

    private void tablerefresh(String URI) throws IOException {
        customerContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        customerContext.getChildren().add(parent);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result=CustomerCrudController.searchCustomer(txtId);
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtAddress.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtEmail.setText(result.getString(5));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws IOException {
        try {

                CustomerCrudController.deleteCustomer(txtId);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        tablerefresh("CustomerForm");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        Customer c =new Customer(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtContact.getText(),
                txtEmail.getText()
        );
        try {
            CustomerCrudController.updateCustomer(c);
            tablerefresh("CustomerForm");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result=CustomerCrudController.searchCustomer(txtId);
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtAddress.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtEmail.setText(result.getString(5));

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






















































