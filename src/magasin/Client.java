package magasin;

import commons.Adresse;
import database.QueryDB;
import database.Transaction;
import javafx.scene.control.Alert;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;

public class Client extends DBObject implements IdbInterface {
    private String nom;
    private String prenom;
    private Adresse adresse;
    private Date dateDeNaissance;
    private String mail;
    private String numerotel;
    private boolean carteFidelite;
    private int pointFidelite;

    public Client(String nom, String prenom, Adresse adresse, Date dateDeNaissance, String mail, String numerotel, boolean cartefidelite) {
        super("Client");
        this.nom = nom;
        this.prenom = prenom;
        this.adresse = adresse;
        this.dateDeNaissance = dateDeNaissance;
        this.mail = mail;
        this.numerotel = numerotel;
        this.carteFidelite = cartefidelite;
        this.pointFidelite = 0;

    }


    //For query
    public Client() {
        super("Client");
    }

    @Test
    public long getID() {
        return ID;
    }

    @Override
    public void create(Transaction transaction) {
        //creer l'incsrpition depuis les valeurs de l'object
        Connection conn = transaction.getdBi().getConnection();
        PreparedStatement stmt = null;
        java.sql.Date d = new java.sql.Date(this.dateDeNaissance.getTime());
        try {
            String sql = "insert into " + this.tableName + "(nom, prenom, adresse,dateDeNaissance,mail,numerotel,carteFidelite,pointFidelite) values(?,?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, this.nom);
            stmt.setString(2, this.prenom);
            stmt.setString(3, this.adresse.toDB());
            stmt.setDate(4, d);
            stmt.setString(5, this.mail);
            stmt.setString(6, this.numerotel);
            stmt.setBoolean(7, this.carteFidelite);
            stmt.setInt(8, this.pointFidelite);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if (rs.next()) {
                this.ID = rs.getInt(1);
            }
            transaction.succesfullMessage();
        } catch (SQLException e) {
            transaction.setEx(e);
            transaction.setMessage(e.getMessage());
            transaction.setLevel(Alert.AlertType.ERROR);

        }

    }


    @Override
    public void update(Transaction transaction) {
        //remplacer les elements modifier dans le l'inscription sql
        Connection conn = transaction.getdBi().getConnection();
        PreparedStatement stmt = null;
        java.sql.Date d = new java.sql.Date(this.dateDeNaissance.getTime());
        try {
            String sql = "insert into " + this.tableName + "(nom, prenom, adresse,dateDeNaissance,mail,numerotel,carteFidelite,pointFidelite) values(?,?,?,?,?,?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, this.nom);
            stmt.setString(2, this.prenom);
            stmt.setString(3, this.adresse.toDB());
            stmt.setDate(4, d);
            stmt.setString(5, this.mail);
            stmt.setString(6, this.numerotel);
            stmt.setBoolean(7, this.carteFidelite);
            stmt.setInt(8, this.pointFidelite);
            stmt.executeUpdate();
            transaction.succesfullMessage();
        } catch (SQLException e) {
            transaction.setEx(e);
            transaction.setMessage(e.getMessage());
            transaction.setLevel(Alert.AlertType.ERROR);

        }

    }

    @Override
    public void load(Transaction transaction, long id) {
        //cherche l'inscription avec son id et copie les valeurs dans l'obj
        try {
            Connection conn = transaction.getdBi().getConnection();
            ResultSet rs = conn.prepareStatement("SELECT * FROM " + this.tableName + " WHERE id =" + id).executeQuery();
            if (rs != null) {
                while (rs.next()) {
                    this.nom = rs.getString("nom");
                    this.prenom = rs.getString("prenom");
                    this.adresse = new Adresse("", "", "", "");
                    this.adresse.fromDB(rs.getString("adresse"));
                    this.dateDeNaissance = rs.getDate("dateDeNaissance");
                    this.mail = rs.getString("mail");
                    this.numerotel = rs.getString("numerotel");
                    this.carteFidelite = rs.getBoolean("carteFidelite");
                    this.pointFidelite = rs.getInt("pointFidelite");
                    this.ID = rs.getLong("id");
                }
            }
            transaction.succesfullMessage();
        } catch (SQLException e) {
            transaction.setEx(e);
            transaction.setMessage(e.getMessage());
            transaction.setLevel(Alert.AlertType.ERROR);
        }

    }


    //GETER/SETTER+TOSTRING

    public Adresse getAdresse() {
        return adresse;
    }

    public void setAdresse(Adresse adresse) {
        this.adresse = adresse;
    }

    public String getAdresseString() {
        return this.adresse.toString();
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getNumerotel() {
        return numerotel;
    }

    public void setNumerotel(String numerotel) {
        this.numerotel = numerotel;
    }

    public boolean isCarteFidelite() {
        return carteFidelite;
    }

    public void setCarteFidelite(boolean carteFidelite) {
        this.carteFidelite = carteFidelite;
    }

    public int getPointFidelite() {
        return pointFidelite;
    }

    public void setPointFidelite(int pointFidelite) {
        this.pointFidelite = pointFidelite;
    }

    public Date getDateDeNaissance() {
        return this.dateDeNaissance;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return "Client : " + this.prenom + " " + this.nom + " habitant à " + this.adresse.toString() + " née le " + this.dateDeNaissance
                + "mail : " + this.mail + "n° tel " + this.numerotel + " avec " + this.pointFidelite + " point de fidelite";
    }

    public String getDesc() {
        return this.prenom + " " + this.nom;
    }
}
