package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import logic.ApplicationEvent;
import magasin.DBObject;
import magasin.Produit;
import ui.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class queryProduitController extends QueryBaseController {

    @FXML
    public AnchorPane PProduit;
    @FXML
    private Button updateButton;
    @FXML
    private produitController PProduitController;

    @Override
    protected Button getUpdateButton() {
        return updateButton;
    }

    public Button getActionButton() {
        return PProduitController.getActionButton();
    }

    public Produit getCurrentSelectedProduit() {
        return (Produit) currentSelectedObj;
    }

    void setforadd(boolean b) {
        updateButton.setVisible(b);
    }

    @Override
    void setToInternPane(DBObject o) {
        PProduitController.produitReadout((Produit) o);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAppDispatch(ApplicationEvent.appWindows.CREATE_PRODUIT_QUERY);
        PProduitController.setForProduitRead(false);
        PProduitController.setStandalone(false);
        Button actionButton = PProduitController.getActionButton();
        actionButton.setOnAction((ActionEvent) -> {
            removeCurrentObj();
        });
        updateButton.setDisable(true);
        actionButton.setText("Supprimer");
        launchInitialSearch();
        actionButton.setDisable(true);
        Main.getAppEventDisp().addListener((ApplicationEvent.events event, Object... params) -> {
            switch (event) {
                case NEW_PRODUIT:
                    Produit p = (Produit) params[0];
                    doList.add(affichProduit(p));
                    dbObjects.add(p);
                    break;
                case DELETED:
                    DBObject dbo = (DBObject) params[0];
                    if (dbo.getClass().getSimpleName().equals("Produit")) {
                        doList.remove(affichProduit((Produit) dbo));
                        dbObjects.remove(dbo);
                        if (currentSelectedObj != null && currentSelectedObj.equals(dbo)) {
                            PProduitController.clean();
                        }
                    }
                    break;
                case FORCE_RELOAD:
                    launchInitialSearch();
            }
        });
        LElement.setItems(doList);
    }

    private void launchInitialSearch() {
        ArrayList<Produit> results = new ArrayList<Produit>();
        results = Main.getAppC().searchProduits();
        dbObjects.addAll(results);
        for (DBObject p : dbObjects) {
            doList.add(affichProduit((Produit) p));
        }
    }

    private String affichProduit(Produit p) {
        return p.getNomArticle();
    }

    public void update(ActionEvent actionEvent) {
        PProduitController.buttonClicked(null);
        currentSelectedObj = null;
    }
}
