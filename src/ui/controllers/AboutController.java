package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Window;
import logic.ApplicationEvent;
import org.ini4j.Ini;
import ui.Main;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;

public class AboutController extends ShowHideDialog implements Initializable {

    @FXML
    public TextArea aboutText;

    String text = "Project 6 Poo\n" +
            "Made by Zackary et Théo\n" +
            "Build Information :\n" +
            "Version : XX.XX.XX \n" +
            "Using : Java.XX \n" +
            "\n" +
            "PCXX\n";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAppDispatch(ApplicationEvent.appWindows.CREATE_ABOUT);
        text = text.replaceAll("Java.XX", "Java " + System.getProperty("java.version"));
        text = text.replaceAll("XX.XX.XX", Main.MAJORVERSION + "." + Main.VERSION + "." + Main.BUILDNUMBER);
        text = text.replaceAll("PCXX", Main.hal.getProcessor().getProcessorIdentifier().getName() + "\n" + Main.hal.getMemory().toString() + "\n" + Main.si.getOperatingSystem().toString());
        aboutText.setText(text);
    }

    public void checkUpdate(){
        boolean error = false;
        try {
            InetAddress ip =InetAddress.getByName("www.github.com");
            if(ip.isReachable(160)){
                error = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            error = true;
        }
        if(error){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Mise a jour");
            alert.setContentText("Connexion indisponible");
            alert.showAndWait();
            return ;
        }
        Path tmpFile ;
        try {
            tmpFile = Files.createTempFile("temp",".ini");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        try {
            Files.copy(
                    //Some hardcoding never hurted anyone ...
                    new URL("https://raw.githubusercontent.com/FicheeSS/Projet6POO/c51af1d23221e97c8361219ae3dfe2cbc208daaf/config.ini").openStream(),
                    tmpFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Ini ini;
        try {
            ini = new Ini(new File(tmpFile.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        assert ini != null;
        if(Main.MAJORVERSION < Integer.parseInt(ini.get("Application Properties", "MajorVersion"))||
                Main.VERSION < Integer.parseInt(ini.get("Application Properties", "Version"))||
        Main.BUILDNUMBER < Integer.parseInt(ini.get("Application Properties", "BuildNumber"))){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise a jour");
            alert.setContentText("Une mise à jour est disponible");
            alert.showAndWait();
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Mise a jour");
            alert.setContentText("Votre programme est à jour");
            alert.showAndWait();
        }
    }
    @Override
    protected Window getWindow() {
        return aboutText.getScene().getWindow();
    }

    public void verifyUpdate(ActionEvent actionEvent) {
        checkUpdate();
    }
}
