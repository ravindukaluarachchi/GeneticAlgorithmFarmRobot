/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author ravklk
 */
public class FrmStartController implements Initializable {

    @FXML
    private Button btnStart;
    @FXML
    private CheckBox chkElitist;
    @FXML
    private ComboBox<String> cmbCrossOver;
    @FXML
    private TextField txtPopulationSize;
    @FXML
    private TextField txtNoOfRounds;
    @FXML
    private TextField txtSpeed;
    @FXML
    private TextField txtMutationPercentage;
    @FXML
    private ComboBox<String> cmbMutation;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtPopulationSize.setText("12");        
        txtNoOfRounds.setText("500");
        txtSpeed.setText("10");
        txtMutationPercentage.setText("10");
        
        cmbCrossOver.setItems(FXCollections.observableArrayList(
                "1 Point",
                "2 Point",
                "Edge"
        ));
        
        cmbCrossOver.getSelectionModel().select(0);
        
        cmbMutation.setItems(FXCollections.observableArrayList(
                "Swap",
                "Insert",
                "Scramble",
                "Inversion"
        ));
        
        cmbMutation.getSelectionModel().select(0);
    }    

    @FXML
    private void btnStartOnClick(ActionEvent event) {
    }
    
}
