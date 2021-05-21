package logic;

import commons.Adresse;
import database.DatabaseInterface;
import database.QueryDB;
import database.Transaction;
import javafx.scene.control.Alert;
import magasin.*;
import ui.Main;

import java.util.ArrayList;
import java.util.Date;

public class AppModel {

    private DatabaseInterface dBi;

    @Deprecated
    public DatabaseInterface getdBi() {
        return dBi;
    }

    public void initDB() {
        dBi = new DatabaseInterface();
        dBi.lauchDatabase();
    }

    public Transaction createClient(String prenom, String nom, Adresse adresse, Date dateDeNaissance, String mail, String numerotel, boolean carteFidelite) {
        Client c = new Client(nom, prenom, adresse, dateDeNaissance, mail, numerotel, carteFidelite);
        Transaction tx = new Transaction(dBi);
        c.create(tx);
        tx.setCreatedObj(c);
        return tx;
    }

    public Transaction createProduit(String typeArticle, String marque, String nomArticle, float prixArticle, boolean isSolde, float solde, long ID_fournisseur) {
        Produit p = new Produit(typeArticle, marque, nomArticle, prixArticle, isSolde, solde, ID_fournisseur);
        Transaction tx = new Transaction(dBi);
        p.create(tx);
        tx.setCreatedObj(p);
        return tx;
    }

    public Transaction searchFournisseur(String nomFournisseur) {
        Transaction tx = new Transaction(dBi);
        QueryDB qDB = new QueryDB("nomFournisseur", nomFournisseur, "", "id");
        Fournisseur f = new Fournisseur();
        f.query(tx, qDB);
        return tx;
    }

    public Transaction searchEmployeExist(int numEmployee) {
        Transaction tx = new Transaction(dBi);
        QueryDB qDB = new QueryDB("numEmploye", numEmployee, "", "id");
        Employe e = new Employe();
        e.query(tx, qDB);
        return tx;

    }

    public Transaction createEmploye(String nomEmploye, int numEmploye, String typePoste) {
        Employe e = new Employe(nomEmploye, numEmploye, typePoste);
        Transaction tx = new Transaction(dBi);
        e.create(tx);
        tx.setCreatedObj(e);
        return tx;
    }

    /*
     * Return Tx contenant un Arraylist de Long contenant les ids des différents objs trouvés
     * */
    public Transaction searchAll(String NomTable) {
        Transaction tx = new Transaction(dBi);
        QueryDB qDB = new QueryDB("", "", "", "id");
        DBObject obj = new DBObject(NomTable);
        obj.query(tx, qDB);
        return tx;
    }

    public Transaction deleteDBObject(DBObject o) {
        Transaction tx = new Transaction(dBi);
        o.delete(tx);
        return tx;
    }

    public Transaction searchFournisseurNumber(float idFournisseur) {
        Transaction tx = new Transaction(dBi);
        DBObject dbo = new DBObject("Fournisseur");
        dbo.query(tx, new QueryDB("id", idFournisseur, "", "nomFournisseur"));
        return tx;
    }

    public Transaction searchQtyOfProduit(float id) {
        Transaction tx = new Transaction(dBi);
        QueryDB qdb = new QueryDB("id_produit", id, "", "quantite");
        Main.getStock().query(tx, qdb);
        return tx;
    }

    public Transaction createCommande(ArrayList<Long> listearticle, float reduction, String typepaiement, Adresse adresselivr, Date datelivraison, long id_client) {
        Commande c = new Commande(listearticle, reduction, typepaiement, adresselivr, datelivraison, id_client);
        Transaction tx = new Transaction(dBi);
        c.create(tx);
        tx.setCreatedObj(c);
        return tx;
    }

    public Transaction removeOneOfProduct(long l) {
        Transaction tx = new Transaction(dBi);
        Main.getStock().removeOne(tx, l);
        return tx;
    }

    public Transaction loadClient(long id_client) {
        Transaction tx = new Transaction(dBi);
        Client c = new Client();
        c.load(tx, id_client);
        tx.setCreatedObj(c);
        return tx;
    }

    public Transaction searchProduit(long l) {
        Transaction tx = new Transaction(dBi);
        Produit p = new Produit();
        p.load(tx, l);
        tx.setCreatedObj(p);
        return tx;
    }

    private void setTxToInternalError(Transaction tx, String message) {
        tx.setMessage(message);
        tx.setEx(null);
        tx.setCreatedObj(null);
        tx.setLevel(Alert.AlertType.ERROR);
    }

    public Transaction updateClient(String prenom, String nom, Adresse a, Date datanaissance, String mail, String tel, boolean fidel) {
        Transaction tx = new Transaction(dBi);
        Client c = new Client(nom, prenom, a, datanaissance, mail, tel, fidel);
        QueryDB qDB = new QueryDB("numerotel", tel, "", "id");
        c.query(tx, qDB);
        if (tx.getLevel() != Alert.AlertType.ERROR) {
            ArrayList<Long> results = (ArrayList<Long>) tx.getCreatedObj();
            if (results.size() > 1) {
                setTxToInternalError(tx, "Something has gone terribly wrong multiple Client were found");
                return tx;
            }
            Client del = new Client();
            del.load(tx, results.get(0));
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Client was not loaded properly");
                return tx;
            }
            del.delete(tx);
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Client was not deleted properly");
                return tx;
            }
            tx.setDeleteObj(del);
            c.create(tx);
            tx.setCreatedObj(c);

        }
        return tx;
    }

    public Transaction updateProduit(String typeArticle, String marque, String nom, float prix, boolean iss, float solde, long idFournissseur, long stock) {
        Transaction tx = new Transaction(dBi);
        Produit p = new Produit(typeArticle, marque, nom, prix, iss, solde, idFournissseur);
        QueryDB qDB = new QueryDB("nomArticle", nom, "", "id");
        p.query(tx, qDB);
        if (tx.getLevel() != Alert.AlertType.ERROR) {
            ArrayList<Long> results = (ArrayList<Long>) tx.getCreatedObj();
            if (results.size() > 1) {
                setTxToInternalError(tx, "Something has gone terribly wrong multiple Produit were found");
                return tx;
            }
            Produit del = new Produit();
            del.load(tx, results.get(0));
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Produit was not loaded properly");
                return tx;
            }
            Stock stk = Main.getStock();
            stk.setProduit(del, 0);
            stk.delete(tx);
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the stock of the product was not deleted properly");
                return tx;
            }
            del.delete(tx);
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Produit was not deleted properly");
                return tx;
            }
            tx.setDeleteObj(del);
            p.create(tx);
            stk.setProduit(p, stock);
            stk.create(tx);
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the stock of the product was not created properly");
                return tx;
            }
            tx.setCreatedObj(p);


        }
        return tx;
    }

    public Transaction searchPhoneNumberClient(String tel) {
        Transaction tx = new Transaction(dBi);
        QueryDB qdb = new QueryDB("numerotel", tel, "", "id");
        Client c = new Client();
        c.query(tx, qdb);
        return tx;
    }

    public Transaction createFournisseur(String fourname) {
        Transaction tx = new Transaction(dBi);
        Fournisseur f = new Fournisseur(fourname);
        f.create(tx);
        tx.setCreatedObj(f);
        return tx;
    }

    public Transaction updateFournisseur(String fourname) {
        Transaction tx = new Transaction(dBi);
        Fournisseur p = new Fournisseur(fourname);
        QueryDB qDB = new QueryDB("nomFournisseur", fourname, "", "id");
        p.query(tx, qDB);
        if (tx.getLevel() != Alert.AlertType.ERROR) {
            ArrayList<Long> results = (ArrayList<Long>) tx.getCreatedObj();
            if (results.size() > 1) {
                setTxToInternalError(tx, "Something has gone terribly wrong multiple Fournisseur were found");
                return tx;
            }
            Fournisseur del = new Fournisseur();
            del.load(tx, results.get(0));
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Fournisseur was not loaded properly");
                return tx;
            }
            del.delete(tx);
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Fournisseur was not deleted properly");
                return tx;
            }
            tx.setDeleteObj(del);
            p.create(tx);
            if (tx.getLevel() == Alert.AlertType.ERROR) {
                setTxToInternalError(tx, "Something has gone terribly wrong the Fournisseur was not created properly");
                return tx;
            }
            tx.setCreatedObj(p);


        }
        return tx;
    }

    public Transaction loadProduit(Long aLong) {
        Transaction tx = new Transaction(dBi);
        Produit c = new Produit();
        c.load(tx, aLong);
        tx.setCreatedObj(c);
        return tx;
    }
}
