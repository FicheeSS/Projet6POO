package magasin;

import database.QueryDB;
import database.Transaction;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DBObject{
    protected long ID;
    protected String tableName;

    public DBObject(String tableName) {
        this.tableName = tableName;
    }

    public long getID() {
        return ID;
    }

    public void delete(Transaction transaction) {
        deletefromId(transaction, this.ID);

    }

    public void deletefromId(Transaction transaction, long id) {
        Connection conn = transaction.getdBi().getConnection();
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("DELETE FROM " + this.tableName + " WHERE id =" + id);
            preparedStatement.execute();
            transaction.succesfullMessage();
            transaction.setCreatedObj(this);
        } catch (SQLException e) {
            transaction.setEx(e);
            transaction.setMessage(e.getMessage());
            transaction.setLevel(Alert.AlertType.ERROR);

        }

    }

    public void query(Transaction tx, QueryDB qDB) {
        ArrayList<Object> out = new ArrayList<Object>();
        Connection conn = tx.getdBi().getConnection();
        ResultSet rs;
        try {
            String sql = qDB.construcQuery(this.tableName);
            rs = conn.prepareStatement(sql).executeQuery();
            tx.succesfullMessage();
            while (rs.next()) {
                out.add(rs.getObject(1));
            }
        } catch (SQLException e) {
            tx.setEx(e);
            tx.setMessage(e.getMessage());
            tx.setLevel(Alert.AlertType.ERROR);
        }
        tx.setCreatedObj(out);

    }

    public String arrayToDB(ArrayList<Long> l) {
        StringBuilder out = new StringBuilder();
        for (Long e : l) {
            out.append(e).append("^");
        }
        return out.toString();
    }


    public ArrayList<Long> DBtoArray(String in) {
        ArrayList<Long> out = new ArrayList<Long>();
        String[] split = in.split("\\^");
        for (String s : split) {
            out.add(Long.parseLong(s));
        }
        return out;
    }
}
