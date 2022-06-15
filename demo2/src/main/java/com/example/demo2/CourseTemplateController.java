package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class CourseTemplateController {
    @FXML
    private Button buttonCourse;

    ///update
    private String username;
    private String role;
    private Stage stage;
    private String courseName;
    private int courseID;


    void setData(int CourseID,String CourseName, String Username,String Role) {///update
        courseID=CourseID;
        courseName=CourseName;
        buttonCourse.setText(courseName);
        username=Username;
        role=Role;
    }

    ///update
    @FXML
    public void courseTemplateButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CoursePageTemplate.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CoursePageTemplateController coursePageTemplateController=fxmlLoader.getController();
        coursePageTemplateController.setData(courseID,username,role,courseName);
        coursePageTemplateController.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
    }
}