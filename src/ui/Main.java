package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import logic.AppController;
import logic.AppModel;
import logic.ApplicationEvent;
import logic.ApplicationEventDispatcher;
import magasin.Client;
import magasin.Commande;
import magasin.Produit;
import magasin.Stock;
import org.ini4j.Ini;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import ui.controllers.loadSample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static javafx.application.Application.launch;

public class Main {

    private static final Stock stock = new Stock();
    private static final AppController appC = new AppController();
    private static final AppModel appM = new AppModel();
    private static final ApplicationEventDispatcher appEventDisp = new ApplicationEventDispatcher();
    public static int MAJORVERSION;
    public static int VERSION;
    public static int BUILDNUMBER;
    public static HardwareAbstractionLayer hal;
    public static SystemInfo si;
    public static boolean isDemo;
    public static  boolean loadOnStartup;
    public static Path tempDirWithPrefix = null;

    public static void setLoadStartup(boolean b){
        Ini ini = null;
        try {
            ini = new Ini(new File("config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert ini != null;
        ini.put("Application Config", "LoadOnStartup", b);
    }


    public static Stock getStock() {
        return stock;
    }

    public static ApplicationEventDispatcher getAppEventDisp() {
        return appEventDisp;
    }

    public static AppController getAppC() {
        return appC;
    }

    public static AppModel getAppM() {
        return appM;
    }

    public static void main(String[] args) {
        si = new SystemInfo();
        hal = si.getHardware();
        Ini ini = null;
        try {
            ini = new Ini(new File("config.ini"));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        if (si.getOperatingSystem().getFamily() == "MacOS" && !Boolean.parseBoolean(ini.get("Application Config", "forceMacOS"))) {
            System.err.println("OS non supporté");
            System.exit(1);
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        }
        String prefix = "project6";
        try {
            tempDirWithPrefix = Files.createTempDirectory(prefix);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Launching DB may take some time ...");
        appM.initDB();

        assert ini != null;
        MAJORVERSION = Integer.parseInt(ini.get("Application Properties", "MajorVersion"));
        VERSION = Integer.parseInt(ini.get("Application Properties", "Version"));
        BUILDNUMBER = Integer.parseInt(ini.get("Application Properties", "BuildNumber"));
        ini.put("Application Properties", "BuildNumber", BUILDNUMBER++);
        isDemo = Boolean.parseBoolean(ini.get("Application Config", "runDemo"));
        ini.put("Application Config", "runDemo", "false");
        loadOnStartup = Boolean.parseBoolean( ini.get("Application Properties", "LoadOnStartup"));
        if(!loadOnStartup){checkDailyNotify();}
        System.out.println("Now Launching fxml ...");
        launch(MainFXML.class, args);

    }

    public static void checkDailyNotify() {
        ArrayList<Commande> cms = appC.searchAllCommande();
        String pattern = "dd-MM-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        String currentDate = simpleDateFormat.format(new Date());
        for (Commande c : cms) {
            if (simpleDateFormat.format(c.getDateLivraison()).equals(currentDate)) {
                appC.notifyDelivery(c);
            }
        }
        ArrayList<Client> cls = appC.searchAllClient();
        for (Client c : cls) {
            if (simpleDateFormat.format(c.getDateDeNaissance()).equals(currentDate)) {
                appC.notifyBirthday(c);
            }
        }
    }
}







