package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PeopleTemplateController {

    @FXML
    private Label studentNameLabel;

    public void setData(String StudentName){
        studentNameLabel.setText(StudentName);
    }

}
