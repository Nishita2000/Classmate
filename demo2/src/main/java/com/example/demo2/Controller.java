package com.example.demo2;

import com.lambdaworks.crypto.SCryptUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label loginMessage;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private ChoiceBox<String> myChoiceBox;
    private final String[] option = {"Teacher", "Student"};
    private Stage stage;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        myChoiceBox.getItems().addAll(option);
    }

    @FXML
    public void loginButtonAction(ActionEvent event) {
        if (!username.getText().isBlank() && !password.getText().isBlank()) {
            validateLogin(event);
        } else {
            loginMessage.setText("Please enter username and password");
        }
    }

    public void validateLogin(ActionEvent event) {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String Password = this.password.getText();

        String verifyLogin = "SELECT count(1),Password FROM useraccounts WHERE Username = '" + username.getText() + "' AND Role = '" + myChoiceBox.getValue() + "'";
        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);

            while (queryResult.next()) {
                if ((queryResult.getInt(1) == 1) && SCryptUtil.check(Password, queryResult.getString(2))) {
                    verifiedLogin(event);
                } else {
                    loginMessage.setText("Invalid login. Please try again!");
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    public void verifiedLogin(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HomepageController homePage = fxmlLoader.getController();
        homePage.setData(username.getText(),myChoiceBox.getValue());
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
    }

    @FXML
    public void signUpButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Register.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
    }

}