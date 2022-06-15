package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class HomepageController {
    public Button logoutButton;
    public Button reportButton;
    public Button changePassButton;
    @FXML
    private Label homepageLabel;
    private Stage stage;
    private String username;
    private String role;
    public void setData(String Username,String Role){
        username = Username;
        homepageLabel.setText("Welcome " + username);
        role = Role;
    }

    @FXML
    void courseButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Course.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CourseController coursePage = fxmlLoader.getController();
        coursePage.setData(String.valueOf(username),String.valueOf(role));
        coursePage.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void profileButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Profile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ProfileController profilePage = fxmlLoader.getController();
        profilePage.setData(String.valueOf(username),String.valueOf(role));
        profilePage.showProfile();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void changePassButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("ChangePass.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ChangePassController changePassPage = fxmlLoader.getController();
        changePassPage.setData(String.valueOf(username),String.valueOf(role));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void reportButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Report.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        ReportController reportPage = fxmlLoader.getController();
        reportPage.setData(String.valueOf(username),String.valueOf(role));
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void logoutButtonOnAction(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("You are about to logout");
        alert.setContentText("Do you want to logout?");
        if (alert.showAndWait().get() == ButtonType.OK) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2);
        }
    }

}

