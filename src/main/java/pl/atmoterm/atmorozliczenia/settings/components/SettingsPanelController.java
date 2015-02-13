
package pl.atmoterm.atmorozliczenia.settings.components;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import pl.atmoterm.atmorozliczenia.settings.services.SettingsManager;

public class SettingsPanelController implements Initializable {

    @FXML
    private TextField clientId;
    
    @FXML
    private TextField clientSecret;
    
    @FXML
    private TextField person;
    
    @FXML
    private TextField position;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        clientId.setText(SettingsManager.get().getGoogleClientId());
        clientSecret.setText(SettingsManager.get().getGoogleClientSecret());
        person.setText(SettingsManager.get().getExportPerson());
        position.setText(SettingsManager.get().getExportPosition());
    }   
    
    @FXML
    private void handleSave(ActionEvent event) {
        SettingsManager.get().setGoogleClientId(clientId.getText());
        SettingsManager.get().setGoogleClientSecret(clientSecret.getText());
        SettingsManager.get().setExportPerson(person.getText());
        SettingsManager.get().setExportPosition(position.getText());
        SettingsManager.get().save();
        
        ((Node)event.getSource()).getScene().getWindow().hide();
    }
}
