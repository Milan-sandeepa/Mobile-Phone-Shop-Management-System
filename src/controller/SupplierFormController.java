package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import model.Customer;
import model.Supplier;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class SupplierFormController {
    public TableView<Supplier> tblSuppliers;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colAddress;
    public TableColumn colContact;
    public TableColumn colItems;
    public TableColumn colEmail;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtContact;
    public JFXTextField txtItem;
    public JFXTextField txtEmail;
    public AnchorPane supplierContext;
    public JFXTextField txtAddress;
    public JFXButton btnSaveButton;
    public JFXButton btnDeleteButton;
    public JFXButton btnUpdateButton;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory("address"));
        colContact.setCellValueFactory(new PropertyValueFactory("contact"));
        colEmail.setCellValueFactory(new PropertyValueFactory("email"));
        colItems.setCellValueFactory(new PropertyValueFactory("status"));

        loadAllSuppliers();

        Pattern idPattern = Pattern.compile("^(S0)[0-9]{2,5}$");
        Pattern namePattern = Pattern.compile("^[A-z 0-9]{3,15}$");
        Pattern itemPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");
        Pattern contactPattern = Pattern.compile("^07(7|6|8|1)[0-9]{7}$");
        Pattern addressPattern = Pattern.compile("^[A-z0-9 ,/]{4,20}$");

        map.put(txtId,idPattern);
        map.put(txtName,namePattern);
        map.put(txtItem,itemPattern);
        map.put(txtContact,contactPattern);
        map.put(txtAddress,addressPattern);

        btnSaveButton.setDisable(false);
        btnUpdateButton.setDisable(true);
        btnDeleteButton.setDisable(true);
    }

    private void loadAllSuppliers() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Supplier");
        ObservableList<Supplier> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Supplier(
                            result.getString("sId"),
                            result.getString("name"),
                            result.getString("address"),
                            result.getString("contact"),
                            result.getString("email"),
                            result.getString("status")

                    )
            );
        }
        tblSuppliers.setItems(obList);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result=SupplierCrudController.searchSupplier(txtId);
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtAddress.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtEmail.setText(result.getString(5));
                txtItem.setText(result.getString(6));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM Supplier WHERE sId=?", txtId.getText());
        if (resultSet.next()){
            new Alert(Alert.AlertType.ERROR, "Supplier Already Exists.!").show();
        }else {
            Supplier s = new Supplier(
                    txtId.getText(),
                    txtName.getText(),
                    txtAddress.getText(),
                    txtContact.getText(),
                    txtEmail.getText(),
                    txtItem.getText()
            );
            SupplierCrudController.saveSupplier(s);
            try {
                tablerefresh("SupplierForm");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void tablerefresh(String URI) throws IOException {
        supplierContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        supplierContext.getChildren().add(parent);
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws IOException {
        try {

            SupplierCrudController.deleteSupplier(txtId);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        tablerefresh("SupplierForm");
    }

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        Supplier s =new Supplier(
                txtId.getText(),
                txtName.getText(),
                txtAddress.getText(),
                txtContact.getText(),
                txtEmail.getText(),
                txtItem.getText()
        );
        try {
            SupplierCrudController.updateSupplier(s);
            tablerefresh("SupplierForm");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result=SupplierCrudController.searchSupplier(txtId);
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtAddress.setText(result.getString(3));
                txtContact.setText(result.getString(4));
                txtEmail.setText(result.getString(5));
                txtItem.setText(result.getString(6));
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
