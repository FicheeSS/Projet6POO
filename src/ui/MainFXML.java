package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.*;
import logic.ApplicationEvent;
import ui.controllers.loadSample;

import java.util.Objects;

public class MainFXML extends Application {

    private static Scene primaryScene;
    private final Image APP_ICON = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/ui/comp.png")));

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ui/fxml/mainWindow.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        primaryStage.setTitle("Project 6");
        Rectangle2D screenBounds = Screen.getPrimary().getBounds();
        primaryScene = new Scene(root, (screenBounds.getWidth()>1284) ? 1284 :screenBounds.getWidth()  , (screenBounds.getHeight()>811) ? 811 :screenBounds.getHeight() -10);
        primaryStage.setScene(primaryScene);
        primaryStage.getIcons().add(APP_ICON);
        primaryStage.show();
//        primaryStage.setMaximized(true);

        createShowHideDialog("/ui/fxml/client.fxml", "Client", ApplicationEvent.appWindows.CREATE_CLIENT);
        createShowHideDialog("/ui/fxml/produit.fxml", "Produit", ApplicationEvent.appWindows.CREATE_PRODUIT);
        createShowHideDialog("/ui/fxml/employe.fxml", "Employe", ApplicationEvent.appWindows.CREATE_EMPLOYE);
        createShowHideDialog("/ui/fxml/queryClient.fxml", "Liste des clients", ApplicationEvent.appWindows.CREATE_CLIENT_QUERY);
//        createShowHideDialog("./fxml/chartClient.fxml", "Graphique des clients", ApplicationEvent.appWindows.CREATE_CLIENT_CHART);
        createShowHideDialog("/ui/fxml/stockGestion.fxml", "Gestion du stock", ApplicationEvent.appWindows.CREATE_PRODUIT_QUERY);
        createShowHideDialog("/ui/fxml/searchProduit.fxml", "Selection un produit a ajouter au panier", ApplicationEvent.appWindows.CREATE_PRODUIT_ADDER);
        createShowHideDialog("/ui/fxml/Commande.fxml", "Creation d'une commande", ApplicationEvent.appWindows.CREATE_COMMANDE);
        createShowHideDialog("/ui/fxml/about.fxml", "A propos", ApplicationEvent.appWindows.CREATE_ABOUT);
        createShowHideDialog("/ui/fxml/commandFiling.fxml", "Selectionner un client", ApplicationEvent.appWindows.CREATE_CLIENT_COMMANDE_FILLER);
        createShowHideDialog("/ui/fxml/commandQuery.fxml", "Liste des commandes", ApplicationEvent.appWindows.CREATE_COMMANDE_QUERY);
        createShowHideDialog("/ui/fxml/employeQuery.fxml", "Liste des employe", ApplicationEvent.appWindows.CREATE_EMPLOYE_QUERY);
        createShowHideDialog("/ui/fxml/fournisseur.fxml","Fournisseur",ApplicationEvent.appWindows.CREATE_FOURNISSEUR);
        createShowHideDialog("/ui/fxml/fournisseurQuery.fxml","Recherche fournisseur",ApplicationEvent.appWindows.CREATE_FOURNISSEUR_QUERY);
        if (Main.isDemo) {
            createShowHideDialog("/ui/fxml/demo.fxml", "Demo", ApplicationEvent.appWindows.CREATE_DEMO);
            Main.getAppC().showWindow(ApplicationEvent.appWindows.CREATE_DEMO, true);
        }
        if(Main.loadOnStartup){
            new loadSample(Main.getAppM().getdBi());
        }
    }

    private void createShowHideDialog(String fxmlRessource, String title, ApplicationEvent.appWindows appWindow) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlRessource));
        Parent root;
        try {
            root = loader.load();
        } catch (Exception ex) {
            System.err.println("The following error occur while loading fxml : " + ex.getMessage());
            ex.printStackTrace();
            return;
        }

        Scene dlgScene = new Scene(root);
        Stage stage = new Stage();
        stage.getIcons().add(APP_ICON);
        stage.setScene(dlgScene);
        stage.initStyle(StageStyle.DECORATED);
        stage.setTitle(title);
        stage.initModality(Modality.NONE);
        stage.initOwner(primaryScene.getWindow());
        stage.setOnCloseRequest((WindowEvent t) -> {
            Main.getAppC().showWindow(appWindow, false);
            t.consume();
        });
        stage.show();
        stage.centerOnScreen();
        Main.getAppC().showWindow(appWindow, false);

    }

}
