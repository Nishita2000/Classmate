package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddCourseController {

    private String username;
    private Stage stage;
    private String role;


    public  void setData(String Username,String Role){
        username = Username;
        role = Role;
    }

    @FXML
    private Button addCourseButton;

    @FXML
    private Label addCourseMsg;

    @FXML
    private PasswordField classCodeField;

    @FXML
    private TextField courseNameField;

    @FXML
    public void addCourseButtonOnAction() {
        String courseName = this.courseNameField.getText();
        String classCode = this.classCodeField.getText();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        try {
            PreparedStatement stmt = connectDb.prepareStatement("insert into courses(teacher,courseName,classCode) values(?,?,?);");
            stmt.setString(1, username);
            stmt.setString(2, courseName);
            stmt.setString(3, classCode);
            int status = stmt.executeUpdate();
            addCourseMsg.setText("Course added successfully");
        } catch (SQLException e) {
            addCourseMsg.setText("This class code is already taken");
        }
    }

    @FXML
    public void backButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Course.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CourseController coursePage = fxmlLoader.getController();
        coursePage.setData(String.valueOf(username),String.valueOf(role));
        coursePage.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
    }
}
