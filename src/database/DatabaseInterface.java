package database;

import ch.vorburger.mariadb4j.DB;
import ch.vorburger.mariadb4j.DBConfigurationBuilder;
import magasin.Fournisseur;
import ui.Main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;


public class DatabaseInterface {
    Connection conn;

    public void lauchDatabase() {
        DBConfigurationBuilder configBuilder = DBConfigurationBuilder.newBuilder();
        configBuilder.setPort(3306); // OR, default: setPort(0); => autom. detect free port
        File tmp = Main.tempDirWithPrefix.toFile();
        tmp.deleteOnExit();
        configBuilder.setDataDir(tmp.getAbsolutePath()); // just an example
        DB db = null;
        String dbName = "test"; // never test
        try {
            db = DB.newEmbeddedDB(configBuilder.build());
            db.start();
//            db.createDB(dbName);
            this.conn = DriverManager.getConnection(configBuilder.getURL(dbName), "root", "");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        try {
            Statement stmt = conn.createStatement();
            String sqlFournisseur = "CREATE TABLE Fournisseur(nomFournisseur VARCHAR(100),id bigint auto_increment primary key)";
            stmt.executeUpdate(sqlFournisseur);
            String sqlEmploye = "CREATE TABLE Employe(nomEmploye VARCHAR(200) ,numEmploye bigint,typePoste VARCHAR(100),id bigint auto_increment primary key)";
            stmt.executeUpdate(sqlEmploye);
            String sqlClient = "CREATE TABLE Client(nom VARCHAR(100) , prenom VARCHAR(100) , adresse VARCHAR(100) ,dateDeNaissance DATE ,mail VARCHAR(100), numerotel VARCHAR(20),carteFidelite BOOL,pointFidelite INT(100),id bigint auto_increment primary key )";
            stmt.executeUpdate(sqlClient);
            String sqlProduit = "CREATE TABLE Produit(typeArticle VARCHAR(100),marque VARCHAR(100),nomArticle VARCHAR(100),prixArticle FLOAT,isSolde BOOLEAN, solde FLOAT, ID_fournisseur bigint ,id bigint auto_increment primary key,FOREIGN KEY(ID_fournisseur) REFERENCES  Fournisseur(id))";
            stmt.executeUpdate(sqlProduit);
            String sqlCommande = "CREATE TABLE Commande(listeArticle LONGTEXT, reduction FLOAT, typePaiement VARCHAR(20), adresseLivr VARCHAR(200),dateLivraison DATE,dateCreation DATE,ID_client bigint ,id bigint auto_increment primary key, FOREIGN KEY (ID_client) REFERENCES Client(id)) ";
            stmt.executeUpdate(sqlCommande);
            String sqlStock = "CREATE TABLE Stock(id_produit bigint , quantite bigint, FOREIGN KEY (id_produit) REFERENCES Produit(id))";
            stmt.executeUpdate(sqlStock);
            Fournisseur f = new Fournisseur("Inconnu");
            Transaction tx = new Transaction(this);
            f.create(tx);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return conn;
    }
}

