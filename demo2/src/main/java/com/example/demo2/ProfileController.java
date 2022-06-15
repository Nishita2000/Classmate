package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;


public class ProfileController {
    private String username;
    private String email;
    private String role;
    @FXML
    private Label usernameField;
    @FXML
    private Label nameField;
    @FXML
    private Label emailField;
    @FXML
    private Label roleField;
    @FXML
    private Button backButton;
    private Stage stage;

    public void setData(String Username,String Role) {
        username = Username;
        role = Role;
    }

    public void showProfile() throws SQLException {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String result = "SELECT Name,Email,Role FROM useraccounts WHERE Username = '" + username + "'";
        Statement statement = connectDb.createStatement();
        ResultSet queryResult = statement.executeQuery(result);
        while (queryResult.next()) {
            String name = queryResult.getString("Name");
            nameField.setText(name);
            usernameField.setText(username);
            String email = queryResult.getString("Email");
            emailField.setText(email);
            String role = queryResult.getString("Role");
            roleField.setText(role);
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Homepage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        HomepageController homePage = fxmlLoader.getController();
        homePage.setData(username,role);
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
    }
}


