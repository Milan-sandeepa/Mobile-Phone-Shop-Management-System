package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginFormController {
    public JFXTextField txtUserName;
    public JFXPasswordField pwdPassword;
    public AnchorPane dashBoardContext;


    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        if (txtUserName.getText().equalsIgnoreCase("admin")&& pwdPassword.getText().equalsIgnoreCase("123")){
            dashBoardContext.getChildren().clear();
            Stage stage = (Stage) dashBoardContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/AdminDashboard.fxml"))));
            stage.setMinWidth(1180);
            stage.setMinHeight(796);
            stage.setResizable(false);
            stage.setMaximized(false);

        }else if(txtUserName.getText().equalsIgnoreCase("cashier") && pwdPassword.getText().equalsIgnoreCase("123")){
            dashBoardContext.getChildren().clear();
            Stage stage = (Stage) dashBoardContext.getScene().getWindow();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/CashierDashboard.fxml"))));
            stage.setMinWidth(1180);
            stage.setMinHeight(796);
            stage.setResizable(false);
            stage.setMaximized(false);
        }
    }
}
