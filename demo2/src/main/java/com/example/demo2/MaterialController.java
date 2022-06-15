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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class MaterialController {
    private String username;
    private String role;
    private String courseName;
    private Stage stage;
    private int courseID;

    @FXML
    private GridPane grid;

    @FXML
    private Button addContentButton;

    @FXML
    private Button backButton;

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

    public void setData(int CourseID, String Username, String Role, String CourseName) {
        courseID = CourseID;
        username = Username;
        role = Role;
        courseName = CourseName;
        if (role.equals("Teacher"))
            addContentButton.setVisible(true);
    }

    private int column = 0;
    private int row = 1;

    public void addContentOnAction(ActionEvent event) throws IOException {
        FileChooser prob = new FileChooser();
        prob.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));

        File f1 = prob.showOpenDialog(null);
        if (f1 != null) {
            String problem = String.valueOf(f1);
            System.out.println(problem);
        }
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        PreparedStatement pst = null;
        byte[] pdfData = new byte[(int) f1.length()];// if f1 null
        DataInputStream dis = new DataInputStream(new FileInputStream(f1));
        dis.readFully(pdfData);  // read from file into byte[] array
        dis.close();

        try {
            String sql = "INSERT INTO materials(courseID, pdfname) VALUES (?, ?)";
            pst = connectDb.prepareStatement(sql);

            pst.setInt(1, courseID);
            pst.setBytes(2, pdfData);  // byte[] array
            pst.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        }

        ArrayList<Integer> pdfFiles = new ArrayList<>();
        String query = "select row_number() over(order by courseID) as num_row from materials where courseID = '" + courseID + "'";
        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()) {
                int materialsID = queryResult.getInt(1);
                pdfFiles.add(materialsID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        int i = pdfFiles.size() - 1;
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("MaterialTemplate.fxml"));
        try {
            AnchorPane newFile = fxmlloader.load();
            MaterialTemplateController materialTemplateController = fxmlloader.getController();
            materialTemplateController.setData(pdfFiles.get(i), courseName, courseID);

            if (column == 2) {
                column = 0;
                row++;
            }
            grid.add(newFile, column++, row);

            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_PREF_SIZE);

            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_PREF_SIZE);

            GridPane.setMargin(newFile, new Insets(10));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetch() {
        ArrayList<Integer> pdfFiles = new ArrayList<>();
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDb = connectNow.getConnection();
        String query = "select row_number() over(order by courseID) as num_row from materials where courseID = '" + courseID + "'";
        try {
            Statement statement = connectDb.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()) {
                int materialsID = queryResult.getInt(1);
                pdfFiles.add(materialsID);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < pdfFiles.size(); i++) {
            FXMLLoader fxmlloader = new FXMLLoader();
            fxmlloader.setLocation(getClass().getResource("MaterialTemplate.fxml"));
            try {
                AnchorPane newFile = fxmlloader.load();
                MaterialTemplateController materialTemplateController = fxmlloader.getController();
                materialTemplateController.setData(pdfFiles.get(i), courseName, courseID);

                if (column == 2) {
                    column = 0;
                    row++;
                }
                grid.add(newFile, column++, row);

                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(newFile, new Insets(10));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
