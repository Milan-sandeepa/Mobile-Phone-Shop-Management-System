package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Customer;
import model.Item;
import model.Repair;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class RepairFormController {
    public Label lblDate;
    public Label lblTime;
    public TableView<Repair> tblRepairs;
    public TableColumn colrId;
    public TableColumn colId;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colCharge;
    public TableColumn colStatus;
    public TableColumn colOption;
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public AnchorPane repairContext;
    public JFXComboBox<String> txtStatus;
    public JFXTextField txtId;
    public JFXTextField txtCost;
    public JFXTextField txtDescription;
    public TableColumn colDesc;
    public JFXButton btnSaveButton;
    public JFXButton btnUpdateButton;
    public JFXButton btnDeleteButton;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize() throws SQLException, ClassNotFoundException {
        colrId.setCellValueFactory(new PropertyValueFactory("id"));
        colId.setCellValueFactory(new PropertyValueFactory("customerId"));
        colDate.setCellValueFactory(new PropertyValueFactory("date"));
        colTime.setCellValueFactory(new PropertyValueFactory("time"));
        colCharge.setCellValueFactory(new PropertyValueFactory("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory("status"));
        colDesc.setCellValueFactory(new PropertyValueFactory("description"));

        loadDateAndTime();
        setCustomerId();
        loadAllRepairs();

        Pattern idPattern = Pattern.compile("^(R0)[0-9]{2,5}$");
        Pattern chargePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{2})?$");
        Pattern descPattern = Pattern.compile("^[A-z0-9 ,/]{4,50}$");

        map.put(txtId,idPattern);
        map.put(txtCost,chargePattern);
        map.put(txtDescription,descPattern);

        btnSaveButton.setDisable(false);
        btnUpdateButton.setDisable(true);
        btnDeleteButton.setDisable(true);

        ObservableList<String> statlist=FXCollections.observableArrayList("No","Process","Completed","Denied");
        txtStatus.setItems(statlist);
        //----------------------------
        cmbCustomerId.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->{
                    setCustomerDetails((String) newValue);
                });
    }

    private void loadAllRepairs() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Repair ORDER BY rId DESC");

        ObservableList<Repair> repairList = FXCollections.observableArrayList();

        while (result.next()){
            repairList.add(
                    new Repair(
                            result.getString("rId"),
                            result.getString("cId"),
                            result.getString("description"),
                            result.getString("repairDate"),
                            result.getString("repairTime"),
                            result.getDouble("cost"),
                            result.getString("status")
                    )
            );
        }
        tblRepairs.setItems(repairList);
    }

    private void setCustomerDetails(String selectedCustomerId) {
        try {
            Customer c=CustomerCrudController.getCustomer(selectedCustomerId);

            if (c!=null){

                txtName.setText(c.getName());
                txtAddress.setText(c.getAddress());
                txtContact.setText(c.getContact());

            }else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setCustomerId() {
        try {
            cmbCustomerId.setItems(FXCollections.observableArrayList(CustomerCrudController.getCustomerIds()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void loadDateAndTime() {
        //set Date
        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        //set time
        Timeline clock=new Timeline(new KeyFrame(Duration.ZERO, e->{
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour()+":"+currentTime.getMinute()+":"+currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }

    public void openNewCustomerOnAction(ActionEvent event) throws IOException {
        repairContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"));
        repairContext.getChildren().add(parent);
    }

    public void btnSaveOnAction(ActionEvent event) throws SQLException, ClassNotFoundException, IOException {
        ResultSet resultSet= CrudUtil.execute("SELECT * FROM Repair WHERE rId=?", txtId.getText());

        if (txtId.getText().isEmpty()){

            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();

        }else if (resultSet.next()){

            new Alert(Alert.AlertType.ERROR, "Repair Id Already Exists.!").show();

        } else {

            Repair r = new Repair(
                    txtId.getText(),
                    (String) cmbCustomerId.getValue(),
                    txtDescription.getText(),
                    lblDate.getText(),
                    lblTime.getText(),
                    Double.parseDouble(txtCost.getText()),
                    (String) txtStatus.getValue()
            );
            RepairCrudController.saveItem(r);
            tablerefresh("RepairForm");
        }
    }

    private void tablerefresh(String URI) throws IOException {
        repairContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        repairContext.getChildren().add(parent);
    }

    public void btnUpdateOnAction(ActionEvent event) {
        Repair r = new Repair(
                txtId.getText(),
                (String) cmbCustomerId.getValue(),
                txtDescription.getText(),
                lblDate.getText(),
                lblTime.getText(),
                Double.parseDouble(txtCost.getText()),
                txtStatus.getValue()
        );

        try {
            RepairCrudController.updateRepair(r);
            tablerefresh("RepairForm");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtSearchOnAction(ActionEvent event) throws SQLException {

        try {
            ResultSet  result = RepairCrudController.searchRepair(txtId);
            if (result.next()) {
                cmbCustomerId.setValue(result.getString(2));
                txtDescription.setText(result.getString(3));
                txtCost.setText(result.getString(6));
                txtStatus.setValue(result.getString(7));

            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void btnSearchOnAction(ActionEvent event) {
        try {
            ResultSet  result = RepairCrudController.searchRepair(txtId);
            if (result.next()) {
                cmbCustomerId.setValue(result.getString(2));
                txtDescription.setText(result.getString(3));
                txtCost.setText(result.getString(6));
                txtStatus.setValue(result.getString(7));
                btnUpdateButton.setDisable(false);
                btnDeleteButton.setDisable(false);
            } else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnDeleteOnAction(ActionEvent event) throws IOException {
        try {
            RepairCrudController.deleteRepair(txtId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        tablerefresh("RepairForm");
    }

    public void posEvent(MouseEvent mouseEvent) {
        String customerID=cmbCustomerId.getValue();
        String customerName=txtName.getText();
        String customerAddress=txtAddress.getText();
        String customerContact=txtContact.getText();
        String repairID=txtId.getText();
        double total= Double.parseDouble(txtCost.getText());
        String repairDesc=txtDescription.getText();

        HashMap paramMap=new HashMap();

        paramMap.put("id",customerID);
        paramMap.put("name",customerName);
        paramMap.put("address",customerAddress);
        paramMap.put("contact",customerContact);
        paramMap.put("rId",repairID);
        paramMap.put("total",total);
        paramMap.put("desc",repairDesc);

        try {
            //Catch The Compiled Report
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/RepairReport.jasper"));

            //Fill the information which report needed
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));

            //Then the report is ready.. let's view it
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
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



























