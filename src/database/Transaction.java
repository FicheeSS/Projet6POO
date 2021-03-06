package database;

import javafx.scene.control.Alert;

public class Transaction {
    private final DatabaseInterface DBi;
    private Alert.AlertType level;
    private String message;
    private Object createdObj;
    private Exception ex = null;
    private Object deleteObj;

    public Transaction(DatabaseInterface dbi) {
        this.DBi = dbi;
        this.message = "";
        this.level = Alert.AlertType.NONE;
    }

    public Object getDeleteObj() {
        return deleteObj;
    }

    public void setDeleteObj(Object deleteObj) {
        this.deleteObj = deleteObj;
    }

    public Exception getEx() {
        return ex;
    }

    public void setEx(Exception ex) {
        this.ex = ex;
    }

    public Object getCreatedObj() {
        return createdObj;
    }

    public void setCreatedObj(Object createdObj) {
        this.createdObj = createdObj;
    }

    public void succesfullMessage() {
        this.message = "Réussite !";
        this.level = Alert.AlertType.INFORMATION;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DatabaseInterface getdBi() {
        return this.DBi;
    }

    public Alert.AlertType getLevel() {
        return level;
    }

    public void setLevel(Alert.AlertType level) {
        this.level = level;
    }
}
