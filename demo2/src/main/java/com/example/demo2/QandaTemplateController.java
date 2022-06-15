package com.example.demo2;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.Objects;

public class QandaTemplateController {
    private String role;
    @FXML
    private TextField qnaTextField;

    @FXML
    private Label usernameLabel;
    public void setData(String user,String userRole,String text){
        usernameLabel.setText(user);
        qnaTextField.setText(text);
        role = userRole;
        if(role.equals("Student")){
            usernameLabel.setLayoutX(195.0);
            usernameLabel.setAlignment(Pos.CENTER_RIGHT);
            qnaTextField.setLayoutX(107.0);
            qnaTextField.setStyle("-fx-background-color: #649E94;");
        }
        else {
            usernameLabel.setLayoutX(6.0);
            usernameLabel.setAlignment(Pos.CENTER_LEFT);
            qnaTextField.setLayoutX(1.0);
            qnaTextField.setStyle("-fx-background-color: #A2E8DC;");
        }
    }
}