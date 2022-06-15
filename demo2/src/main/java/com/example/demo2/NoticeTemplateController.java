package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class NoticeTemplateController {

    @FXML
    private Label nameLabel;

    @FXML
    private TextField noticeField;

    public void setData(String name,String notice) {
        nameLabel.setText(name + ", Lecturer");
        noticeField.setText(notice);
    }

}
