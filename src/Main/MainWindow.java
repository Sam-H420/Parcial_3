package Main;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import Connection.Databaseconnection;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

public class MainWindow implements Initializable{

    @FXML
    private ChoiceBox<String> assign_cBox;
    private String[] assign = {"Informatics", "Physics", "Chemestry"};


    @FXML
    private ChoiceBox<String> gender_cBox;
    private String[] gender = {"Male", "Female"};

    @FXML
    private Hyperlink clean_hyp;

    @FXML
    private TextField grades_1_prompt;

    @FXML
    private TextField grades_2_prompt;

    @FXML
    private TextField grades_3_prompt;

    @FXML
    private TextField id_prompt;

    @FXML
    private TextField name_prompt;

    @FXML
    private Button refresh_button;

    @FXML
    private Button register_button;

    @FXML
    private TableView<tableModel> general_table;
    
    @FXML
    private TableColumn<tableModel, String> name_column;

    @FXML
    private TableColumn<tableModel, Integer> id_column;
    
    @FXML
    private TableColumn<tableModel, String> gender_column;
    
    @FXML
    private TableColumn<tableModel, Double> grades_column;

    @FXML
    private TableColumn<tableModel, String> status_column;

    @FXML
    private Label alert_1;

    @FXML
    private Label alert_2;

    @FXML
    private Label inf_grade;

    @FXML
    private Label phy_grade;

    @FXML
    private Label chem_grade;

    @FXML
    private ChoiceBox<String> def_choiceBox;

    @FXML
    private Label def_percent;

    @FXML
    private ChoiceBox<String> reg_choiceBox;

    @FXML
    private Label reg_percent;

    @FXML
    private ChoiceBox<String> exc_choiceBox;

    @FXML
    private Label exc_percent;

    @FXML
    private Hyperlink end_hyp;

    Databaseconnection connectNow = new Databaseconnection();
    Connection connectDB = connectNow.connect();

    @FXML
    void onCleanClick(ActionEvent event) {
        name_prompt.setText("");
        id_prompt.setText("");
        gender_cBox.setValue(null);
        grades_1_prompt.setText("");
        grades_2_prompt.setText("");
        grades_3_prompt.setText("");
        alert_1.setText("");
    }

    @FXML
    void onRefreshButtonClick(ActionEvent event) {
        refreshColumns();
        refreshstats();
    }

    @FXML
    void onEndhypClick(ActionEvent event) throws IOException {
        Parent loginScene = FXMLLoader.load(getClass().getResource("loginWindow.fxml"));
        Scene loginViewScene = new Scene(loginScene);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(loginViewScene);
        window.centerOnScreen();
        window.show();
    }


    @FXML
    void onRegisterButtonClick(ActionEvent event) {


        if(id_prompt.getText()==null || name_prompt.getText()==null || gender_cBox.getValue()==null || grades_1_prompt.getText()==null || grades_2_prompt.getText()==null || grades_3_prompt.getText()==null ){
            alert_1.setText("All fields must be filled");
        }

        else if( (isNumeric(id_prompt.getText()) == false) ||  (id_prompt.getText().length() > 3)){
            alert_1.setText("ID must be 3 digits long and a number");
        }
        else if(isNumeric(grades_1_prompt.getText()) == false || isNumeric(grades_2_prompt.getText()) == false  || isNumeric(grades_3_prompt.getText()) == false || Double.parseDouble(grades_1_prompt.getText()) < 0 || Double.parseDouble(grades_1_prompt.getText()) > 100 || Double.parseDouble(grades_2_prompt.getText()) < 0 || Double.parseDouble(grades_2_prompt.getText()) > 100 || Double.parseDouble(grades_3_prompt.getText()) < 0 || Double.parseDouble(grades_3_prompt.getText()) > 100){
            alert_2.setText("Grades must be a number \nbetween 0 and 100");
        }

        else{
            String verifyregister = "SELECT count(1) FROM Students WHERE id = '" + id_prompt.getText() + "'";
            String addstudent = "INSERT INTO Students(id,name,genero,Informatics,Physics,Chemestry) VALUES ('" + id_prompt.getText() + "', '" + name_prompt.getText() + "', '" + gender_cBox.getValue() + "', '" + grades_1_prompt.getText() + "', '" + grades_2_prompt.getText() + "', '" + grades_3_prompt.getText() +"')" ;

            try {
                java.sql.Statement statement = connectDB.createStatement();
                java.sql.ResultSet queryResult = statement.executeQuery(verifyregister);

                while(queryResult.next()) {
                    if(queryResult.getInt(1) == 1) {
                        System.out.println("Id already exists, please introduce another one");
                    } else {
                        System.out.println("Registering student");
                        statement.executeUpdate(addstudent);
                        refreshColumns();
                        refreshstats();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
            }
        }
    }

    ObservableList<tableModel> listview = FXCollections.observableArrayList();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        gender_cBox.getItems().addAll(gender);
        assign_cBox.getItems().addAll(assign);
        assign_cBox.setValue("Informatics");

        def_choiceBox.getItems().addAll(assign);
        def_choiceBox.setValue("Chemestry");

        reg_choiceBox.getItems().addAll(assign);
        reg_choiceBox.setValue("Physics");

        exc_choiceBox.getItems().addAll(assign);
        exc_choiceBox.setValue("Informatics");

        
        
        refreshColumns();
        refreshstats();
    }

    public void refreshstats(){
        try {
            java.sql.Statement statement = connectDB.createStatement();
            java.sql.ResultSet r = statement.executeQuery("SELECT avg(Informatics), avg(Chemestry), avg(Physics) FROM Students");
            
            while(r.next()) {
                inf_grade.setText(Math.round(r.getDouble("avg(Informatics)")*100d)/100d + "");
                phy_grade.setText(Math.round(r.getDouble("avg(Physics)")*100d)/100d + "");
                chem_grade.setText(Math.round(r.getDouble("avg(Chemestry)")*100d)/100d + "");
            }

            java.sql.ResultSet count = statement.executeQuery("SELECT COUNT(id) FROM Students;");
            int total = count.getInt("COUNT(id)");

            java.sql.ResultSet exc = statement.executeQuery("SELECT COUNT(" + exc_choiceBox.getValue() + ") FROM Students WHERE " + exc_choiceBox.getValue() + " >= 90;");
            double totalExc = exc.getInt("COUNT(" + exc_choiceBox.getValue() + ")");

            java.sql.ResultSet reg = statement.executeQuery("SELECT COUNT(" + reg_choiceBox.getValue() + ") FROM Students WHERE " + reg_choiceBox.getValue() + " > 60 AND " + reg_choiceBox.getValue() + " <=80;");
            double totalReg = reg.getInt("COUNT(" + reg_choiceBox.getValue() + ")");

            java.sql.ResultSet def = statement.executeQuery("SELECT COUNT(" + def_choiceBox.getValue() + ") FROM Students WHERE " + def_choiceBox.getValue() + " <= 30;");
            double totalDef = def.getInt("COUNT(" + def_choiceBox.getValue() + ")");

            exc_percent.setText(Math.round(((totalExc/total)*100)*100d) / 100d + "%");
            reg_percent.setText(Math.round(((totalReg/total)*100)*100d) / 100d + "%");
            def_percent.setText(Math.round(((totalDef/total)*100)*100d) / 100d + "%");
            
        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void refreshColumns(){
        listview.clear();

        id_column.setCellValueFactory(new PropertyValueFactory<>("id"));
        name_column.setCellValueFactory(new PropertyValueFactory<>("name"));
        gender_column.setCellValueFactory(new PropertyValueFactory<>("gender"));
        grades_column.setCellValueFactory(new PropertyValueFactory<>("grades"));
        status_column.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        try {

            java.sql.Statement statement = connectDB.createStatement();
            java.sql.ResultSet r = statement.executeQuery("SELECT * FROM Students ");
            while(r.next()) {
                listview.add(new tableModel(
                    r.getInt("id"), 
                    r.getString("name"), 
                    r.getString("genero"),
                    r.getDouble(assign_cBox.getValue())));
            }
            
            general_table.setItems(listview);
        
        }catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    private static boolean isNumeric(String cadena){
	try {
		Integer.parseInt(cadena);
		return true;
	} catch (NumberFormatException nfe){
		return false;
	}
}
}