package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import model.Item;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class AccessoriesFormController {
    public TableView<Item> tblItems;
    public TableColumn colId;
    public TableColumn colName;
    public TableColumn colDesc;
    public TableColumn colQty;
    public TableColumn colUnitPrice;
    public TableColumn colSellingPrice;
    public TableColumn colWarranty;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQty;
    public JFXComboBox<String> txtWarranty;
    public JFXTextField txtSellingPrice;
    public AnchorPane accessContext;
    public JFXButton btnSaveButton;
    public JFXButton btnUpdateButton;
    public JFXButton btnDeleteButton;
    public JFXTextField txtDesc;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory("itemCode"));
        colName.setCellValueFactory(new PropertyValueFactory("name"));
        colDesc.setCellValueFactory(new PropertyValueFactory("description"));
        colQty.setCellValueFactory(new PropertyValueFactory("qtyOnHand"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));
        colSellingPrice.setCellValueFactory(new PropertyValueFactory("sellingPrice"));
        colWarranty.setCellValueFactory(new PropertyValueFactory("warranty"));

        ObservableList<String> list=FXCollections.observableArrayList("No","3 Months","6 Months","12 Months","24 Months");
        txtWarranty.setItems(list);

        loadAllItems();

        Pattern idPattern = Pattern.compile("^(P0)[0-9]{2,5}$");
        Pattern namePattern = Pattern.compile("^[A-z 0-9]{3,15}$");
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        Pattern unitPricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        Pattern descPattern = Pattern.compile("^[A-z0-9 ,/]{4,50}$");
        Pattern sellingPricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");

        map.put(txtId,idPattern);
        map.put(txtName,namePattern);
        map.put(txtQty,qtyPattern);
        map.put(txtUnitPrice,unitPricePattern);
        map.put(txtDesc,descPattern);
        map.put(txtSellingPrice,sellingPricePattern);

        btnSaveButton.setDisable(false);
        btnUpdateButton.setDisable(true);
        btnDeleteButton.setDisable(true);
    }

    private void loadAllItems() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Item");

        ObservableList<Item> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Item(
                            result.getString("itemCode"),
                            result.getString("name"),
                            result.getString("description"),
                            result.getInt("qtyOnHand"),
                            result.getDouble("unitPrice"),
                            result.getDouble("sellingPrice"),
                            result.getString("warranty")
                    )
            );
        }
        tblItems.setItems(obList);
    }

    public void btnSaveOnAction(ActionEvent actionEvent) throws IOException, SQLException, ClassNotFoundException {

        ResultSet resultSet=CrudUtil.execute("SELECT * FROM Item WHERE itemCode=?", txtId.getText());

        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else if (resultSet.next()){
            new Alert(Alert.AlertType.ERROR, "Product Id Already Exists.!").show();
        }
        else {
            Item i = new Item(
                    txtId.getText(),
                    txtName.getText(),
                    txtDesc.getText(),
                    Integer.parseInt(txtQty.getText()),
                    Double.parseDouble(txtUnitPrice.getText()),
                    Double.parseDouble(txtSellingPrice.getText()),
                    txtWarranty.getValue()
            );
            ItemCrudController.saveItem(i);
            tablerefresh("AccessoriesForm");
        }
    }

    private void tablerefresh(String URI) throws IOException {
        accessContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        accessContext.getChildren().add(parent);
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result=ItemCrudController.searchItem(txtId);
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtDesc.setText(result.getString(3));
                txtQty.setText(result.getString(4));
                txtUnitPrice.setText(result.getString(5));
                txtSellingPrice.setText(result.getString(6));
                txtWarranty.setValue(result.getString(7));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            ResultSet result=ItemCrudController.searchItem(txtId);
            if (result.next()) {
                txtName.setText(result.getString(2));
                txtDesc.setText(result.getString(3));
                txtQty.setText(result.getString(4));
                txtUnitPrice.setText(result.getString(5));
                txtSellingPrice.setText(result.getString(6));
                txtWarranty.setValue(result.getString(7));
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

    public void btnUpdateOnAction(ActionEvent actionEvent) throws IOException {
        Item i=new Item(
                txtId.getText(),
                txtName.getText(),
                txtDesc.getText(),
                Integer.parseInt(txtQty.getText()),
                Double.parseDouble(txtUnitPrice.getText()),
                Double.parseDouble(txtSellingPrice.getText()),
                txtWarranty.getValue()
        );
        try {
            ItemCrudController.updateItem(i);
            tablerefresh("AccessoriesForm");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent actionEvent) throws IOException {
        try {
            ItemCrudController.deleteItem(txtId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tablerefresh("AccessoriesForm");
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



































































