package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Customer;
import model.Item;
import model.Order;
import model.OrderDetails;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.CrudUtil;
import view.tm.CartTM;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class SaleFormController {
    public JFXComboBox<String> cmbCustomerId;
    public JFXTextField txtName;
    public JFXTextField txtAddress;
    public JFXTextField txtContact;
    public JFXComboBox<String> cmbItemCode;
    public JFXTextField txtItemName;
    public JFXTextField txtQtyOnHand;
    public JFXTextField txtPrice;
    public JFXTextField txtQty;
    public JFXTextArea txtDescription;
    public TableView tblCart;
    public TableColumn colCode;
    public TableColumn colDescription;
    public TableColumn colUnitPrice;
    public TableColumn colQty;
    public TableColumn colTotal;
    public TableColumn colButton;
    public AnchorPane saleContext;
    public Label lblTotalPrice;
    public Label lblDate;
    public Label lblTime;

    public String CurrentId;

    public void initialize(){

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUnitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colButton.setCellValueFactory(new PropertyValueFactory<>("btn"));

        setCustomerId();
        setItemCodes();
        loadDateAndTime();
        setOrderId();

        //--------------------
        cmbCustomerId.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) ->{
             setCustomerDetails(newValue);
        });

        cmbItemCode.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
              setItemDetails(newValue);
        });
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

    private void setItemDetails(String selectedItemCode) {
        try {
            Item i=ItemCrudController.getItem(selectedItemCode);

            if (i!=null){
                txtItemName.setText(i.getName());
                txtQtyOnHand.setText(String.valueOf(i.getQtyOnHand()));
                txtPrice.setText(String.valueOf(i.getSellingPrice()));
                txtDescription.setText(i.getDescription());
            }else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setItemCodes() {
        try {
            cmbItemCode.setItems(FXCollections.observableArrayList(ItemCrudController.getItemCodes()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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

    private void setCustomerId(){

        try {
            cmbCustomerId.setItems(FXCollections.observableArrayList(CustomerCrudController.getCustomerIds()));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    ObservableList<CartTM> tmList=FXCollections.observableArrayList();

    public void addToCartOnAction(ActionEvent actionEvent) {
        if (txtQty.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();
        }else{

            double unitPrice = Double.parseDouble(txtPrice.getText());
            int qty = Integer.parseInt(txtQty.getText());
            double totalPrice = unitPrice * qty;

            CartTM isExists = isExists(cmbItemCode.getValue());

            if (isExists != null) {
                for (CartTM temp : tmList
                ) {
                    if (temp.equals(isExists)) {
                        temp.setQty((temp.getQty() + qty));
                        temp.setTotal(temp.getTotal() + totalPrice);
                    }
                }
            } else {
                Button btn = new Button("Delete");

                CartTM tm = new CartTM(
                        cmbItemCode.getValue(),
                        txtDescription.getText(),
                        unitPrice,
                        qty,
                        totalPrice,
                        btn
                );

                btn.setOnAction(event -> {

                    tmList.remove(tm);

                });

                tmList.add(tm);
                tblCart.setItems(tmList);
            }
        }
        tblCart.refresh();
        calculateTotal();
    }

    public void openNewCustomerOnAction(ActionEvent actionEvent) throws IOException {
        saleContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/CustomerForm.fxml"));
        saleContext.getChildren().add(parent);
    }

    private CartTM isExists(String itemCode){
        for (CartTM tm:tmList
             ) {
            if (tm.getCode().equals(itemCode)){
                return tm;
            }
        }
        return null;
    }

    private void calculateTotal(){
        double total=0;
        for (CartTM tm:tmList
             ) {
            total+=tm.getTotal();
        }
        lblTotalPrice.setText(String.valueOf(total));
    }

    public void placeOrderOnAction(ActionEvent event) throws SQLException {
        Order order=new Order(
                CurrentId,
                cmbCustomerId.getValue(),
                lblDate.getText(),
                lblTime.getText()

        );
        ArrayList<OrderDetails> details=new ArrayList<>();
        for (CartTM tm:tmList
             ) {
            details.add(new OrderDetails(
                    CurrentId,
                    tm.getCode(),
                    tm.getQty(),
                    tm.getTotal()
            ));
        }

        Connection connection=null;

        try {

            connection= DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);

            boolean isOrderSaved=new OrderCrudController().saveOrder(order);

            if (isOrderSaved){
                boolean isDetailsSaved=new OrderCrudController().saveOrderDetails(details);

                if (isDetailsSaved){

                    connection.commit();
                    new Alert(Alert.AlertType.CONFIRMATION,"Saved.!").show();

                }else{

                    connection.rollback();
                    new Alert(Alert.AlertType.ERROR,"Error.!").show();
                }
            }else {
                connection.rollback();
                new Alert(Alert.AlertType.ERROR,"Error.!").show();
            }
        }catch (SQLException|ClassNotFoundException e){

        }finally {
            connection.setAutoCommit(true);
        }
        tblCart.refresh();
    }

    private void setOrderId() {
        try {
            String x = new OrderCrudController().getOrderId();
            CurrentId=(x);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void posEvent(MouseEvent event) {
        String customerID=cmbCustomerId.getValue();
        String customerName=txtName.getText();
        String customerAddress=txtAddress.getText();
        String customerContact=txtContact.getText();
        double total= Double.parseDouble(lblTotalPrice.getText());

        HashMap paramMap=new HashMap();
        paramMap.put("oId",CurrentId);
        paramMap.put("id",customerID);
        paramMap.put("name",customerName);
        paramMap.put("address",customerAddress);
        paramMap.put("contact",customerContact);
        paramMap.put("total",total);

        try {
            //Catch The Compiled Report
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/Posreport.jasper"));

            //Fill the information which report needed
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JRBeanArrayDataSource(tblCart.getItems().toArray()));

            //Then the report is ready.. let's view it
            JasperViewer.viewReport(jasperPrint, false);
        } catch (JRException e) {
            e.printStackTrace();
        }
    }
}
