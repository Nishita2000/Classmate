package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class PeopleController {
    private String username;
    private String courseName;
    private int courseID;
    private String role;
    private Stage stage;

    @FXML
    private VBox studentNameVBox;

    @FXML
    private Label teacherNameLabel;

    public void setData(int CourseID,String Username,String Role,String CourseName){
        courseID = CourseID;
        username = Username;
        role = Role;
        courseName=CourseName;

    }

    public void fetchData() throws SQLException {
        ArrayList<String> students = new ArrayList<>();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query1="Select teacher From courses Where courseID= '" + courseID + "'";
        Statement statement = connectDb.createStatement();
        ResultSet queryResult;
        try {
            queryResult = statement.executeQuery(query1);
            while (queryResult.next()) {
                String teacherName = queryResult.getString(1);
                teacherNameLabel.setText(teacherName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String query2="Select studentName From studentcourse Where courseID= '"+ courseID + "'";

        //Statement statement = connectDb.createStatement();
        queryResult = statement.executeQuery(query2);
        while (queryResult.next()) {
            String studentName = queryResult.getString(1);
            students.add(studentName);

        }
        for (int i = 0; i < students.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("PeopleTemplate.fxml"));
            try {
                AnchorPane newName = fxmlloader.load();
                PeopleTemplateController peopleTemplateController = fxmlloader.getController();
                peopleTemplateController.setData(students.get(i));
                studentNameVBox.getChildren().add(newName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CoursePageTemplate.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CoursePageTemplateController coursePageTemplateController = fxmlLoader.getController();
        coursePageTemplateController.setData(courseID, username, role, courseName);
        coursePageTemplateController.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }
}
