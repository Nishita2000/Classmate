package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class CoursePageTemplateController {

    private String username;
    private String role;
    private String courseName;
    private Stage stage;
    private int courseID;
    @FXML
    private Button backButton;

    @FXML
    private Button examButton;

    @FXML
    private Button discussionButton;
    @FXML
    private Label headingLabel;

    @FXML
    private Button noticeButton;

    @FXML
    private VBox postVBox;

    @FXML
    private Button submitButton;

    @FXML
    private Button videoButton;

    @FXML
    private Button materialButton;
    @FXML
    private Button peopleButton;
    @FXML
    private Label announcementLabel;

    @FXML
    private TextField postTextField;

    public void setData(int CourseID, String Username, String Role, String CourseName) {
        courseID = CourseID;
        username = Username;
        role = Role;
        courseName = CourseName;
        headingLabel.setText(courseName);
        if (role.equals("Teacher")) {
            postTextField.setVisible(true);
            submitButton.setVisible(true);
        } else {
            announcementLabel.setVisible(true);
        }
    }

    public void fetchData() throws SQLException {
        ArrayList<Pair<String, String>> noticeList = new ArrayList<>();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query = "SELECT Name,Notice FROM notice WHERE courseID='" + courseID + "'";
        Statement statement = connectDb.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()) {
            String name = queryResult.getString(1);
            String text = queryResult.getString(2);
            Pair<String, String> pair = new Pair(name, text);
            noticeList.add(pair);
        }
        for (int i = noticeList.size() - 1; i >= 0; i--) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("NoticeTemplate.fxml"));
            try {
                AnchorPane newMessage = fxmlloader.load();
                NoticeTemplateController noticeController = fxmlloader.getController();
                String var1 = noticeList.get(i).getKey();
                String var2 = noticeList.get(i).getValue();
                noticeController.setData(var1, var2);
                postVBox.getChildren().add(newMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void submitButtonOnAction() throws SQLException {
        String notice = postTextField.getText();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query = "SELECT Name FROM useraccounts WHERE Username = '" + username + "'";
        Statement statement = connectDb.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()) {
            String name = queryResult.getString(1);
            PreparedStatement stmt = connectDb.prepareStatement("insert into notice(Username,Name,Notice,courseID) values(?,?,?,?);");
            stmt.setString(1, username);
            stmt.setString(2, name);
            stmt.setString(3, notice);
            stmt.setInt(4, courseID);
            int status = stmt.executeUpdate();
            postVBox.getChildren().clear();
            fetchData();
            postTextField.clear();
        }
    }

    @FXML
    void discussionButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Qanda.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        QandaController qandaPage = fxmlLoader.getController();
        qandaPage.setData(String.valueOf(username), String.valueOf(role), courseID, courseName);
        qandaPage.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void materialButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Materials.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        MaterialController materialController = fxmlLoader.getController();
        materialController.setData(courseID, username, role, courseName);
        materialController.fetch();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void peopleButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("People.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        PeopleController peopleController = fxmlLoader.getController();
        peopleController.setData(courseID, username, role, courseName);
        peopleController.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Course.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CourseController coursePage = fxmlLoader.getController();
        coursePage.setData(String.valueOf(username), String.valueOf(role));
        coursePage.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}