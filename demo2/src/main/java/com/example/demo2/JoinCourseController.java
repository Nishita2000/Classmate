package com.example.demo2;

import com.lambdaworks.crypto.SCryptUtil;
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
import java.sql.*;

public class JoinCourseController {
    private String username;
    private Stage stage;
    private String role;

    public void setData(String Username,String Role){
        username = Username;
        role = Role;
    }

    @FXML
    private Button backButton;

    @FXML
    private TextField courseNameField;

    @FXML
    private PasswordField classCodeField;

    @FXML
    private Button joinButton;
    @FXML
    private Label invalidCodeMsg;

    @FXML
    public void joinCourseButtonOnAction() {
        String courseName = this.courseNameField.getText();
        String classCode = this.classCodeField.getText();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query = "SELECT count(1),courseID FROM courses WHERE courseName = '" + courseName + "' AND classCode = '" + classCode + "'";
        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()) {
                if (queryResult.getInt(1) == 1) {
                    int courseId = queryResult.getInt(2);
                    System.out.println(courseId);
                    PreparedStatement stmt = connectDb.prepareStatement("insert into studentcourse(courseID,studentName,courseName) values(?,?,?);");
                    stmt.setInt(1, courseId);
                    stmt.setString(2, username);
                    stmt.setString(3,courseName);
                    int status = stmt.executeUpdate();
                    invalidCodeMsg.setText("Joined successfully");
                } else {
                    invalidCodeMsg.setText("Invalid class code");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
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
