package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportController {
    @FXML
    private Button submitButton;
    @FXML
    private Button backButton;
    @FXML
    private Label myLabel;
    @FXML
    private TextArea reportTextArea;
    private Stage stage;
    private String username;
    private String role;

    public void setData(String Username,String Role) {
        username = Username;
        role = Role;
    }
    @FXML
    void submitButtonOnAction(ActionEvent event) throws SQLException {
        String issue = reportTextArea.getText();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        PreparedStatement stmt = connectDb.prepareStatement("insert into reporttable(User,Issue) values(?,?);");
        stmt.setString(1, username);
        stmt.setString(2, issue);
        int status = stmt.executeUpdate();
        reportTextArea.clear();
        myLabel.setText("Your report has been submitted");
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
