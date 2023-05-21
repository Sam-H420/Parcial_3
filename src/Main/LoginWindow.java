package Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.sql.Connection;

import Connection.Databaseconnection;

public class LoginWindow{

    @FXML
    private Label alert_1;

    @FXML
    private Button button_login;

    @FXML
    private Hyperlink clean;

    @FXML
    private Label message_1;

    @FXML
    protected PasswordField pass_prompt;
    
    @FXML
    protected TextField pass_prompt_visible;

    @FXML
    private Hyperlink regist_link;

    @FXML
    protected TextField user_prompt;

    @FXML
    private CheckBox show_check;

    @FXML
    void onCleanClick(ActionEvent event) {
        user_prompt.setText("");
        pass_prompt.setText("");
        pass_prompt_visible.setText("");
    }

    @FXML
    void onCheckClick(ActionEvent event) {
        

        if(show_check.isSelected()){
            pass_prompt.setDisable(true); 
            pass_prompt_visible.setText(pass_prompt.getText());
            pass_prompt.setVisible(false);
                    

        }else{
            pass_prompt.setDisable(false);
            pass_prompt.setText(pass_prompt_visible.getText());
            pass_prompt.setVisible(true);

        }
    }


    @FXML
    void onRegistClick(ActionEvent event) throws IOException{
        Parent signinScene = FXMLLoader.load(getClass().getResource("signinWindow.fxml"));
        Scene signinViewScene = new Scene(signinScene);
        
        Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
        window.setScene(signinViewScene);
        window.centerOnScreen();
        window.show();
    }

    @FXML
    public void onLoginButtonClick(ActionEvent event) throws IOException{
        
        Databaseconnection connectNow = new Databaseconnection();
        Connection connectDB = connectNow.connect();
        String verifyLogin = "SELECT count(1) FROM UsersAcounts WHERE usuario = '" + user_prompt.getText() + "' AND contrasena = '" + pass_prompt.getText() + "'";

        try{
            java.sql.Statement statement = connectDB.createStatement();
            java.sql.ResultSet queryResult = statement.executeQuery(verifyLogin);

            while(queryResult.next()){
                if(queryResult.getInt(1) == 1){
                    alert_1.setText("Login success!");

                            Parent Mainwindow = FXMLLoader.load(getClass().getResource("mainWindow.fxml"));
                            Scene mainViewScene = new Scene(Mainwindow);
                            
                            Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
                            
                            window.setScene(mainViewScene);
                            window.centerOnScreen();
                            window.show();
                    

                }else{
                    alert_1.setText("Invalid login. Please try again.");
                } 
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

}