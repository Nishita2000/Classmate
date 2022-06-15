package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.lambdaworks.crypto.SCryptUtil;

public class SignUpController implements Initializable {
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private ChoiceBox<String> choiceBox;
    @FXML
    private Label signUpSuccessMsg;

    @FXML
    private Button backButton;
    private Stage stage;
    private final String[] option = {"Teacher", "Student"};

    @FXML
    void signUpButtonOnAction(ActionEvent event) throws IOException {
        if (!usernameField.getText().isBlank() && !passwordField.getText().isBlank() && !emailField.getText().isBlank() && !nameField.getText().isBlank())
            validateSignUp(event);
        else
            signUpSuccessMsg.setText("Please enter necessary information's");
    }

    @FXML
    void validateSignUp(ActionEvent event) throws IOException {
        String name = this.nameField.getText();
        String username = this.usernameField.getText();
        String password = this.passwordField.getText();
        String generatedSecuredPasswordHash = SCryptUtil.scrypt(password, 16, 16, 16);
        String email = this.emailField.getText();
        String role = this.choiceBox.getValue();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();

        try {
            PreparedStatement stmt = connectDb.prepareStatement("insert into useraccounts(Name,Username,Password,Email,Role) values(?,?,?,?,?);");
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, generatedSecuredPasswordHash);
            stmt.setString(4, email);
            stmt.setString(5, role);
            int status = stmt.executeUpdate();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (SQLException e) {
            signUpSuccessMsg.setText("Username already taken or invalid information");
        }

    }
    @FXML
    public void backButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(option);
    }
}