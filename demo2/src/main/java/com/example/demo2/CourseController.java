package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CourseController {
    private String username;
    private Stage stage;
    private String role;
    @FXML
    private GridPane grid;

    int column = 0;
    int row = 1;

    public void setData(String Username, String Role) {
        username = Username;
        role = Role;
        if (role.equals("Teacher"))
            addCourseButton.setVisible(true);
        else
            joinCourseButton.setVisible(true);
    }

    @FXML
    private Button addCourseButton;

    @FXML
    private Button joinCourseButton;

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

    @FXML
    public void addButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("AddCourse.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        AddCourseController addCourseController = fxmlLoader.getController();
        addCourseController.setData(username,role);
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();

    }

    @FXML
    public void joinButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("JoinCourse.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        JoinCourseController joinCoursecontroller = fxmlLoader.getController();
        joinCoursecontroller.setData(username,role);
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();

    }

    public void fetchData() {
        ArrayList<Pair<Integer,String>> courseDetails = new ArrayList<>();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query;
        if (role.equals("Student"))
            query = "SELECT courseID,courseName FROM studentcourse WHERE studentName = '" + username + "'";
        else
            query = "SELECT courseID,courseName FROM courses WHERE teacher = '" + username + "'";


        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()) {
                int courseID = queryResult.getInt(1);
                String courseName = queryResult.getString(2);
                Pair <Integer,String>p= new Pair<>(courseID,courseName);
                courseDetails.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < courseDetails.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("CourseTemplate.fxml"));
            try {
                AnchorPane newCourse = fxmlloader.load();
                CourseTemplateController controller = fxmlloader.getController();
                controller.setData(courseDetails.get(i).getKey(),courseDetails.get(i).getValue(),username, role);

                if (column == 2) {
                    column = 0;
                    row++;
                }
                grid.add(newCourse, column++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(newCourse, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}