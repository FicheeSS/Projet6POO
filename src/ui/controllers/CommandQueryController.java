package ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import logic.ApplicationEvent;
import magasin.Commande;
import magasin.DBObject;
import ui.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CommandQueryController extends QueryBaseController implements Initializable {

    @FXML
    private CommandeController PCommandeController;

    @Override
    protected Button getUpdateButton() {
        return null;
    }

    @Override
    protected Button getActionButton() {
        return PCommandeController.getActionButton();
    }

    @Override
    void setToInternPane(DBObject o) {
        PCommandeController.CommandeReadout((Commande) o);
    }

    private void deleteCurrentCommande() {
        if (currentSelectedObj != null) {
            Main.getAppC().removeDBObject(currentSelectedObj);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PCommandeController.setForReadout(false);
        PCommandeController.setStandalone(false);
        Button actionButton = PCommandeController.getActionButton();
        actionButton.setText("Supprimer");
        actionButton.setOnAction((ActionEvent) -> {
            deleteCurrentCommande();
        });
        actionButton.setDisable(true);
        initAppDispatch(ApplicationEvent.appWindows.CREATE_COMMANDE_QUERY);
        Main.getAppEventDisp().addListener((ApplicationEvent.events event, Object... params) -> {
            switch (event) {
                case NEW_COMMAND:
                    Commande c = (Commande) params[0];
                    dbObjects.add(c);
                    doList.add(c.getDesc());
                    break;
                case DELETED:
                    DBObject dbo = (DBObject) params[0];
                    if (dbo.getClass().getSimpleName().equals("Commande")) {
                        Commande cde = (Commande) dbo;
                        dbObjects.remove(cde);
                        doList.remove(cde.getDesc());
                        if (currentSelectedObj != null && currentSelectedObj.equals(cde)) {
                            PCommandeController.clean();
                        }
                    }
                    break;
                case FORCE_RELOAD:
                    initialSearch();
                    break;
            }
        });
        LElement.setItems(doList);
        initialSearch();
    }

    private void initialSearch() {
        ArrayList<Commande> results = new ArrayList<Commande>();
        results = Main.getAppC().searchAllCommande();
        dbObjects.addAll(results);
        for (Commande c : results) {
            doList.add(c.getDesc());
        }
    }
}
