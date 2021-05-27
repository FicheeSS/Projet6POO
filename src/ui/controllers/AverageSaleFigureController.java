package ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import logic.ApplicationEvent;
import magasin.Commande;
import magasin.DBObject;
import ui.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AverageSaleFigureController implements Initializable {

    private ArrayList<Commande> CommandeList = new ArrayList<Commande>();
    @FXML
    private TextField onTextMoyen;


    private float calculteMoyenne() {
        float total = 0;
        for (Commande c : CommandeList) {
            total += c.getEffectiveTotalPrice();
        }
        return total / CommandeList.size();

    }

    private void updateMoyenne() {
        onTextMoyen.setText(String.valueOf(calculteMoyenne()));
    }

    private void searchAllCommand() {
        CommandeList = Main.getAppC().searchAllCommande();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.getAppEventDisp().addListener((ApplicationEvent.events event, Object... params) -> {
            switch (event) {
                case NEW_COMMAND:
                    Commande c = (Commande) params[0];
                    CommandeList.add(c);
                    updateMoyenne();
                    break;
                case DELETED:
                    DBObject dbo = (DBObject) params[0];
                    if (dbo.getClass().getSimpleName().equals("Commande")) {
                        Commande cmd = (Commande) dbo;
                        Commande tbr = null;
                        for (Commande cmds : CommandeList ){
                            if (cmds.getID() == cmd.getID()){
                                tbr = cmds;
                            }
                        }
                        CommandeList.remove(tbr);
                        updateMoyenne();
                    }
                    break;
                case FORCE_RELOAD:
                    searchAllCommand();
                    updateMoyenne();
                    break;
            }
        });
        searchAllCommand();
    }
}
