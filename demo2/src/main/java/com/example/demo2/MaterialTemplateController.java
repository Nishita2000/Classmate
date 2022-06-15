package com.example.demo2;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

public class MaterialTemplateController {
    @FXML
    private Button pdfButton;

    private int materialsID;

    private String courseName;
    private int courseID;

    public void setData(int MaterialsID, String CourseName, int CourseID) {
        courseID = CourseID;
        materialsID = MaterialsID;
        courseName = CourseName;
        String temp = String.valueOf(materialsID);
        pdfButton.setText("Lecture " + temp);
    }

    @FXML
    public void pdfButtonOnAction() {
        String materialName = pdfButton.getText();
        materialName = materialName.substring(8, materialName.length());
        materialsID = Integer.parseInt(materialName);
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query = "select pdfname from (select row_number() over(order by courseID) as num_row,materialsID,pdfname from materials where courseID='" + courseID + "') as alias where num_row='" + materialsID + "'";
        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()) {
                Blob blob = queryResult.getBlob(1);
                InputStream is = blob.getBinaryStream();
                FileOutputStream fos = new FileOutputStream("//D://Materials//" + courseName + "_Lecture_" + materialsID + ".pdf");

                int b = 0;
                while ((b = is.read()) != -1) {
                    fos.write(b);
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
