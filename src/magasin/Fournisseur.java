package magasin;

import database.Transaction;
import javafx.scene.control.Alert;

import java.sql.*;

public class Fournisseur extends DBObject implements IdbInterface {
    String nomFournisseur;

    public Fournisseur(String nomFournisseur) {
        super("Fournisseur");
        this.nomFournisseur = nomFournisseur;
    }


    public Fournisseur() {
        super("Fournisseur");
    }

    @Override
    public void update(Transaction tx) {
        //remplacer les elements modifier dans le l'inscription sql
        Connection conn = tx.getdBi().getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                    "insert into " + this.tableName + "(nomFournisseur) values(?)");
            stmt.setString(1, nomFournisseur);
            stmt.executeUpdate();
            tx.succesfullMessage();
        } catch (SQLException e) {
            tx.setEx(e);
            tx.setMessage(e.getMessage());
            tx.setLevel(Alert.AlertType.ERROR);
        }
    }

    @Override
    public void load(Transaction transaction, long id) {
        //cherche l'inscription avec son id et copie les valeurs dans l'obj
        try {
            Connection conn = transaction.getdBi().getConnection();
            ResultSet rs = conn.prepareStatement("SELECT * FROM Fournisseur WHERE id =" + id).executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    this.nomFournisseur = rs.getString("nomFournisseur");
                }
                transaction.succesfullMessage();
            }
        } catch (SQLException e) {
            transaction.setEx(e);
            transaction.setMessage(e.getMessage());
            transaction.setLevel(Alert.AlertType.ERROR);
        }


    }

    @Override
    public void create(Transaction tx) {
        //creer l'incsrpition depuis les valeurs de l'object
        Connection conn = tx.getdBi().getConnection();
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(
                    "insert into " + this.tableName + "(nomFournisseur) values(?)",
                    Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, nomFournisseur);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                this.ID = rs.getInt(1);
            }
            tx.succesfullMessage();
        } catch (SQLException e) {
            tx.setEx(e);
            tx.setMessage(e.getMessage());
            tx.setLevel(Alert.AlertType.ERROR);
        }

    }

    @Override
    public String getDesc() {
        return nomFournisseur;
    }

    public String getNomFournisseur() {
        return nomFournisseur;
    }
}
