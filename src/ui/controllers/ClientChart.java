package ui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Window;
import logic.ApplicationEvent;
import magasin.Client;
import magasin.DBObject;
import ui.Main;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ClientChart extends ShowHideDialog implements Initializable {


    @FXML
    private final CategoryAxis xAxis = new CategoryAxis();
    @FXML
    private final NumberAxis yAxis = new NumberAxis();
    private final LineChart<String, Number> lineChart =
            new LineChart<String, Number>(xAxis, yAxis);
    @FXML
    private LineChart<?, ?> clientChart;
    private ArrayList<Client> clientList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAppDispatch(ApplicationEvent.appWindows.CREATE_CLIENT_CHART);
        clientList = Main.getAppC().searchAllClient();
        yAxis.setAutoRanging(false);
        yAxis.setLowerBound(0);
        yAxis.setUpperBound(10);
        xAxis.setLabel("Age");
        yAxis.setLabel("Nombre de client");
        lineChart.setTitle("Age des clients");
        showLineClient();
        Main.getAppEventDisp().addListener((ApplicationEvent.events event, Object... params) -> {
            switch (event) {
                case NEW_CLIENT:
                    Client c = (Client) params[0];
                    clientList.add(c);
                    showLineClient();
                    break;
                case DELETED:
                    DBObject dbo = (DBObject) params[0];
                    if (dbo.getClass().getSimpleName().equals("Client")) {
                        clientList.remove(dbo);
                        showLineClient();
                        break;
                    }
                case FORCE_RELOAD:
                    clientList = Main.getAppC().searchAllClient();
                    showLineClient();
                    break;
            }
        });
    }

    private int calculateYearAge(Date datedenaissance) {
        Date currentDate = new Date();
        return currentDate.getYear() - datedenaissance.getYear();

    }

    private int max(int[] list) {
        int max = 0;
        for (int i : list) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }


    private void showLineClient() {
        XYChart.Series data = new XYChart.Series();
        int[] agesParindex = new int[120];
        for (Client c : clientList) {
            int age = calculateYearAge(c.getDateDeNaissance());
            agesParindex[age]++;
        }
        yAxis.setUpperBound(max(agesParindex));
        for (int i = 0; i < 120; i++) {
            data.getData().add(new XYChart.Data(Integer.toString(i), agesParindex[i]));
        }
        clientChart.getData().add(data);
    }

    @Override
    protected Window getWindow() {
        return clientChart.getScene().getWindow();
    }
}
