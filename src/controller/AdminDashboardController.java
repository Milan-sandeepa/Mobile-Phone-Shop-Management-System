package controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Item;
import model.OrderDetails;
import util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Optional;

public class AdminDashboardController {

    public AnchorPane context;
    public AnchorPane loginFormContext;
    public  Label lblDate;
    public  Label lblTime;
    public TableView<OrderDetails> tblOrders;
    public TableColumn colOrderId;
    public TableColumn colItemId;
    public TableColumn colQty;
    public TableColumn colPrice;
    public Label lblCustomerTotal;
    public Label lblProductTotal;
    public Label lblSupplierTotal;
    public Label lblSaleTotal;

    public void initialize() throws SQLException, ClassNotFoundException {
        colOrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        colItemId.setCellValueFactory(new PropertyValueFactory("itemCode"));
        colQty.setCellValueFactory(new PropertyValueFactory("qty"));
        colPrice.setCellValueFactory(new PropertyValueFactory("unitPrice"));

        loadOrders();
        loadDateAndTime();
        loadCustomerTotal();
        loadProductTotal();
        loadSupplierTotal();
        loadSaleTotal();
    }

    private void loadSaleTotal() throws SQLException, ClassNotFoundException {
        double saletotal=0;
        double reptotal=0;
        ResultSet result=CrudUtil.execute("SELECT SUM(price) FROM Order_Details");
        ResultSet represult=CrudUtil.execute("SELECT SUM(cost) FROM repair");

        while (result.next()){
            saletotal+=result.getInt(1);
        }
        while (represult.next()){
            reptotal+=represult.getDouble(1);
        }

        lblSaleTotal.setText(String.valueOf(saletotal+reptotal));
    }

    private void loadSupplierTotal() throws SQLException, ClassNotFoundException {
        String suptotal="0";
        ResultSet result=CrudUtil.execute("SELECT COUNT(*) FROM Supplier");

        while (result.next()){
            suptotal+=result.getString(1);

        }
        lblSupplierTotal.setText(suptotal);
    }

    private void loadProductTotal() throws SQLException, ClassNotFoundException {
        String ptotal="0";
        ResultSet result=CrudUtil.execute("SELECT COUNT(*) FROM Item");

        while (result.next()){
            ptotal+=result.getString(1);

        }
        lblProductTotal.setText(ptotal);
    }

    private void loadCustomerTotal() throws SQLException, ClassNotFoundException {
        String ctotal="0";
        ResultSet result=CrudUtil.execute("SELECT COUNT(*) FROM Customer");

        while (result.next()){
            ctotal+=result.getString(1);

        }
        lblCustomerTotal.setText(ctotal);
    }

    private void loadOrders() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Order_Details ORDER BY OrderId DESC");
        ObservableList<OrderDetails> orderList = FXCollections.observableArrayList();

        while (result.next()){
            orderList.add(new OrderDetails(
                    result.getString("orderId"),
                    result.getString("itemCode"),
                    result.getInt("qty"),
                    result.getDouble("price")
            ));
        }
        tblOrders.setItems(orderList);
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

    public void signOutOnAction(ActionEvent actionEvent) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are You Sure LogOut?", ButtonType.YES,ButtonType.NO);

        Optional<ButtonType> buttonType = alert.showAndWait();

        if (buttonType.get().equals(ButtonType.YES)){
            Stage stage = (Stage) loginFormContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/LoginForm.fxml"))));
            stage.setMinWidth(1180);
            stage.setMinHeight(796);
            stage.setResizable(false);

        }
    }

    public void openDashboardOnAction(ActionEvent actionEvent) throws IOException {
        context.getChildren().clear();
        Stage stage = (Stage) context.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashboard.fxml"))));
        stage.setResizable(false);
        stage.setMaximized(false);
    }

    private void setUi(String URI) throws IOException {
        context.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        context.getChildren().add(parent);
    }

    public void openSaleOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SaleForm");
    }

    public void openCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUi("CustomerForm");
    }

    public void openAccessoriesOnAction(ActionEvent actionEvent) throws IOException {
        setUi("AccessoriesForm");
    }

    public void openRepairsOnAction(ActionEvent actionEvent) throws IOException {
        setUi("RepairForm");
    }

    public void openReturnOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ReturnForm");
    }

    public void openSupplierOnAction(ActionEvent actionEvent) throws IOException {
        setUi("SupplierForm");
    }

    /*
    public void openReportOnAction(ActionEvent actionEvent) throws IOException {
        setUi("ReportForm");
    }

    public void openPayrollOnAction(ActionEvent actionEvent) throws IOException {
        setUi("PayrollForm");
    }*/

    public void openEmployeOnAction(ActionEvent actionEvent) throws IOException {
        setUi("EmployeForm");
    }

    public void posSaleOnAction(ActionEvent event) throws IOException {
        setUi("SaleForm");
    }
}
