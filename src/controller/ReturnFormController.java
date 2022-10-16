package controller;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Customer;
import model.Return;
import util.CrudUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class ReturnFormController {
    public Label lblDate;
    public Label lblTime;
    public JFXTextField txtId;
    public JFXTextField txtName;
    public JFXTextArea txtNote;
    public JFXTextField txtPrice;
    public JFXTextField txtQty;
    public JFXTextField txtTotal;
    public JFXTextField txtWar;
    public JFXTextField txtDate;
    public JFXTextField txtTime;
    public String CurrentId;
    public AnchorPane returnContext;
    public TableView<Return> tblReturns;
    public TableColumn colId;
    public TableColumn colOrderId;
    public TableColumn colName;
    public TableColumn colNote;
    public TableColumn colTotal;
    public JFXTextField txtRid;

    public void initialize() throws SQLException, ClassNotFoundException {
        colId.setCellValueFactory(new PropertyValueFactory("id"));
        colOrderId.setCellValueFactory(new PropertyValueFactory("orderId"));
        colName.setCellValueFactory(new PropertyValueFactory("customerName"));
        colNote.setCellValueFactory(new PropertyValueFactory("note"));
        colTotal.setCellValueFactory(new PropertyValueFactory("amount"));

        loadDateAndTime();
        setOrderId();
        loadAllReturns();
    }

    private void loadAllReturns() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM Returns ORDER BY rId DESC");

        ObservableList<Return> rList = FXCollections.observableArrayList();

        while (result.next()){
            rList.add(
                    new Return(
                            result.getString("rId"),
                            result.getString("orderId"),
                            result.getString("CustomerName"),
                            result.getString("note"),
                            result.getDouble("total")
                    )
            );
        }
        tblReturns.setItems(rList);
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

    public void txtSearchOnAction(ActionEvent event) {
        try {
            ResultSet result=ReturnCrudController.searchOrder(txtId);
            double tot=0;
            while (result.next()){
                tot+=result.getDouble(4);
            }
            txtTotal.setText(String.valueOf(tot));

            ResultSet resultName=ReturnCrudController.searchName(txtId);
            if (resultName.next()){
                txtName.setText(resultName.getString(2));
            }

           ResultSet resultDate=ReturnCrudController.searchDate(txtId);
            if (resultDate.next()){
                txtDate.setText(resultDate.getString(3));
                txtTime.setText(resultDate.getString(4));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnReturnOnAction(ActionEvent event) throws IOException, SQLException, ClassNotFoundException {
        if (txtId.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();
        }else {
            Return r=new Return(
                    CurrentId,
                    txtId.getText(),
                    txtName.getText(),
                    txtNote.getText(),
                    Double.parseDouble(txtTotal.getText())
            );
            ReturnCrudController.saveReturn(r);
            ReturnCrudController.returnOrder(txtId);
            tablerefresh("ReturnForm");
        }
    }

    private void tablerefresh(String URI) throws IOException {
        returnContext.getChildren().clear();
        Parent parent = FXMLLoader.load(getClass().getResource("../view/"+URI+".fxml"));
        returnContext.getChildren().add(parent);
    }

    private void setOrderId() {
        try {
            String x = new ReturnCrudController().getReturnId();
            CurrentId=(x);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent event) {
        if (txtId.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR, "Please Enter the Correct Details.!").show();
        }else {
            try {
                ResultSet result=ReturnCrudController.searchOrder(txtId);
                double tot=0;
                while (result.next()){
                    tot+=result.getDouble(4);
                }
                txtTotal.setText(String.valueOf(tot));

                ResultSet resultName=ReturnCrudController.searchName(txtId);
                if (resultName.next()){
                    txtName.setText(resultName.getString(2));
                }

                ResultSet resultDate=ReturnCrudController.searchDate(txtId);
                if (resultDate.next()){
                    txtDate.setText(resultDate.getString(3));
                    txtTime.setText(resultDate.getString(4));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnDeleteOnAction(ActionEvent event) {
        try {
            ReturnCrudController.deleteReturn(txtRid);
            tablerefresh("ReturnForm");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void txtReturnSearchOnAction(ActionEvent event) {
        try {
            ResultSet result=ReturnCrudController.searchReturn(txtRid);
            if (result.next()){
                txtRid.setText(result.getString(1));
                new Alert(Alert.AlertType.WARNING, "Return Found").show();
            }else {
                new Alert(Alert.AlertType.WARNING, "Empty Result").show();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
