package ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import logic.ApplicationEvent;
import magasin.Client;
import ui.Main;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientCommandFiling extends ShowHideDialog implements Initializable {


    @FXML
    private AnchorPane clientPane;

    @FXML
    private ClientQueryController PQueryClientController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAppDispatch(ApplicationEvent.appWindows.CREATE_CLIENT_COMMANDE_FILLER);
        Main.getAppEventDisp().addListener((ApplicationEvent.events event, Object... params) -> {
            if (event == ApplicationEvent.events.SELECTED_CLIENT) {
                hide();
            }
        });
        PQueryClientController.setStandalone(false);
        Button actionButton = PQueryClientController.getActionButton();
        actionButton.setText("Selectionner");
        actionButton.setOnAction((ActionEvent) -> {
            Main.getAppC().notifySelectedClient((Client) PQueryClientController.getCurrentSelectedObj());
        });
    }

    @Override
    protected Window getWindow() {
        return clientPane.getScene().getWindow();
    }
}
