package com.example.demo2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class QandaController {
    private String username;
    private String role;
    private Stage stage;
    @FXML
    private VBox messageVbox;
    @FXML
    private Button sendButton;
    @FXML
    private TextField textBox;

    private int courseID;
    private String courseName;

    public void setData(String Username,String Role,int CourseID,String CourseName){
        username = Username;
        role = Role;
        courseID=CourseID;
        courseName=CourseName;
    }
    public void fetchData() throws SQLException {
        ArrayList<Pair<Pair<String,String>,String>>messageList = new ArrayList<>();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query ="SELECT Username,Role,Message FROM qanda WHERE CourseID = '"+courseID+"'";
        Statement statement = connectDb.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()) {
            String user = queryResult.getString(1);
            String userRole = queryResult.getString(2);
            String text = queryResult.getString(3);
            Pair<String,String> pair1 = new Pair(user,userRole);
            Pair<Pair<String,String>,String>pair2 = new Pair(pair1,text);
            messageList.add(pair2);
        }
        for (int i = 0; i < messageList.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("QandaTemplate.fxml"));
            try {
                AnchorPane newMessage = fxmlloader.load();
                QandaTemplateController controller = fxmlloader.getController();
                String var1 = messageList.get(i).getKey().getKey();
                String var2= messageList.get(i).getKey().getValue();
                String var3 = messageList.get(i).getValue();
                controller.setData(var1,var2,var3);
                messageVbox.getChildren().add(newMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void sendButtonOnAction(ActionEvent event) throws IOException, SQLException {
        String text = textBox.getText();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        PreparedStatement stmt = connectDb.prepareStatement("insert into qanda(Username,Role,Message,CourseID) values(?,?,?,?);");
        stmt.setString(1, username);
        stmt.setString(2,role);
        stmt.setString(3, text);
        stmt.setInt(4,courseID);
        int status = stmt.executeUpdate();
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("QandaTemplate.fxml"));
        AnchorPane newMessage = fxmlloader.load();
        QandaTemplateController controller = fxmlloader.getController();
        controller.setData(username,role,text);
        messageVbox.getChildren().add(newMessage);
        textBox.clear();
    }
    @FXML
    public void backButtonOnAction(ActionEvent event) throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("CoursePageTemplate.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        CoursePageTemplateController coursePageTemplateController = fxmlLoader.getController();
        coursePageTemplateController.setData(courseID,username,role,courseName);
        coursePageTemplateController.fetchData();
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        this.stage.setScene(scene);
        this.stage.show();
    }
}