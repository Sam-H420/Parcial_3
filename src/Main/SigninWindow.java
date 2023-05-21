package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

import java.sql.Connection;
import Connection.Databaseconnection;

public class SigninWindow {

    @FXML
    private Label alert_1;

    @FXML
    private Label alert_2;

    @FXML
    private Button button_signIn;

    @FXML
    private Hyperlink clean;

    @FXML
    private Hyperlink login_link;
    
    @FXML
    private TextField pass_prompt;

    @FXML
    protected TextField pass_prompt_visible;

    @FXML
    private TextField conPass_prompt;
    
    @FXML
    protected TextField conPass_prompt_visible;
    
    @FXML
    private TextField user_prompt;

    @FXML
    private CheckBox show_check;

    @FXML
    void onCleanClick(ActionEvent event) {
        user_prompt.setText("");
        pass_prompt.setText("");
        conPass_prompt.setText("");
        pass_prompt_visible.setText("");
        conPass_prompt_visible.setText("");
    }

    @FXML
    void onCheckClick(ActionEvent event) {
        
        if(show_check.isSelected()){
            pass_prompt.setDisable(true); 
            pass_prompt_visible.setText(pass_prompt.getText());
            pass_prompt.setVisible(false);

            conPass_prompt.setDisable(true); 
            conPass_prompt_visible.setText(conPass_prompt.getText());
            conPass_prompt.setVisible(false);

        }else{
            pass_prompt.setDisable(false);
            pass_prompt.setText(pass_prompt_visible.getText());
            pass_prompt.setVisible(true);

            conPass_prompt.setDisable(false);
            conPass_prompt.setText(conPass_prompt_visible.getText());
            conPass_prompt.setVisible(true);
        }
    }

    @FXML
    void onLoginClick(ActionEvent event)  throws IOException{
        Parent loginScene = FXMLLoader.load(getClass().getResource("loginWindow.fxml"));
        Scene loginViewScene = new Scene(loginScene);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        window.setScene(loginViewScene);
        window.centerOnScreen();
        window.show();
    }

    @FXML
    void onSigninButtonClick(ActionEvent event) {
        Databaseconnection connectNow = new Databaseconnection();
        Connection connectDB = connectNow.connect();       
        
        String addfields = "INSERT INTO UsersAcounts(Usuario,Contrasena) VALUES ('" + user_prompt.getText() + "', '" + pass_prompt.getText() + "')" ;
        String verifyRegister = "SELECT count(1) FROM UsersAcounts WHERE usuario = '" + user_prompt.getText() + "'";
        
        if (user_prompt.getText().isEmpty() || pass_prompt.getText().isEmpty() || conPass_prompt.getText().isEmpty()) {
            alert_1.setText("\nPlease fill all the fields.");
        }
        else if(!pass_prompt.getText().equals(conPass_prompt.getText())){
            alert_2.setText("Passwords don't match.");
        }
        else if(user_prompt.getText().length() >= 20){
            alert_1.setText("Username must be lesser\nthan 20 characters long.");
        }
        else{
            try{
                java.sql.Statement statement = connectDB.createStatement();
                java.sql.ResultSet queryResult = statement.executeQuery(verifyRegister);

                while(queryResult.next()){
                    if(queryResult.getInt(1) == 1){
                        alert_1.setText("Invalid username,\nplease try again with another one.");
                    }
                    else{
                        alert_1.setText("\nRegister completed.");
                        statement.executeUpdate(addfields);
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
}