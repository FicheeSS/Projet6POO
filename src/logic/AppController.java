package logic;

import commons.Adresse;
import database.Transaction;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import magasin.*;
import ui.Main;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;

@SuppressWarnings("ALL")
public class AppController {

    protected void showError(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Erreur de saisie");
        //alert.setHeaderText("Erreur d'inscription dans la base de donnée");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }


    private void showDialog(Transaction tx, String name) {
        Alert alert = new Alert(tx.getLevel());
        alert.setTitle(name);
        alert.setHeaderText(tx.getMessage());

        Exception ex = tx.getEx();
        if (ex != null) {
            ex.printStackTrace();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);
            alert.getDialogPane().setExpandableContent(expContent);
        }
        alert.showAndWait();
    }


    public void createClient(String nom, String prenom, Adresse adresse, Date dateDeNaissance, String mail, String numerotel, boolean carteFidelite) {

        Transaction tx = Main.getAppM().createClient(nom, prenom, adresse, dateDeNaissance, mail, numerotel, carteFidelite);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            showDialog(tx, "Creation client");
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyNewClient((Client) tx.getCreatedObj());
            }
        }
    }

    public void createProduit(String typeArticle, String marque, String nomArticle, float prixArticle, boolean isSolde, float solde, long ID_fournisseur, long qty) {
        Transaction tx = Main.getAppM().createProduit(typeArticle, marque, nomArticle, prixArticle, isSolde, solde, ID_fournisseur);
        ArrayList<Produit> pds = searchProduits();
        for (Produit p : pds){
            if(p.getNomArticle() == nomArticle){
                showError("Un produit existe deja avec ce nom");
            }
        }
        if (tx.getLevel() != Alert.AlertType.NONE) {
            Main.getStock().setProduit((Produit) tx.getCreatedObj(), qty);
            Transaction stocktx = new Transaction(tx.getdBi());
            Main.getStock().create(stocktx);
            if (stocktx.getLevel() != Alert.AlertType.NONE && stocktx.getLevel() != Alert.AlertType.ERROR) {
                showDialog(tx, "Creation Produit");
                if (tx.getLevel() != Alert.AlertType.ERROR) {
                    notifyNewProduit((Produit) tx.getCreatedObj());
                }
            } else {
                showDialog(stocktx, "Stock");
            }
        }

    }


    public long searchFournisseur(String fourName) {
        Transaction tx = Main.getAppM().searchFournisseur(fourName);
        if (tx.getCreatedObj() != null) {
            @SuppressWarnings("unchecked") ArrayList<Long> results = (ArrayList<Long>) tx.getCreatedObj();
            if (results.size() != 1) {
                return -1;
            } else {
                return results.get(0);
            }
        } else {
            return -1;
        }
    }

    public long searchEmployeExist(int numEmployee) {
        Transaction tx = Main.getAppM().searchEmployeExist(numEmployee);
        if (tx.getCreatedObj() != null) {
            @SuppressWarnings("unchecked") ArrayList<Long> results = (ArrayList<Long>) tx.getCreatedObj();
            if (results.size() > 0) {
                return results.get(0);
            } else {
                return -1;
            }
        } else {
            return -1;
        }
    }

    public void createEmploye(String nomEmploye, int numEmploye, String typePoste) {
        Transaction tx = Main.getAppM().createEmploye(nomEmploye, numEmploye, typePoste);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            showDialog(tx, "Creation Employe");
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyNewEmploye((Employe) tx.getCreatedObj());
            }
        }
    }

    public void removeDBObject(DBObject o) {
        Transaction tx = Main.getAppM().deleteDBObject(o);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            showDialog(tx, "Suppression");
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyDeletedDBObject((DBObject) tx.getCreatedObj());
            }
        }

    }

    public ArrayList<Client> searchAllClient() {
        Transaction tx = Main.getAppM().searchAll("Client");
        ArrayList<Client> out = new ArrayList<Client>();
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<Long> objsid = (ArrayList<Long>) tx.getCreatedObj();
            for (long l : objsid) {
                Client loadedClient = new Client();
                loadedClient.load(tx, l);
                if (tx.getLevel() != Alert.AlertType.ERROR) {
                    out.add(loadedClient);
                } else {
                    System.err.println("Client avec id : " + l + " introuvable");
                }

            }
        }
        return out;
    }


    private void notifyDeletedDBObject(DBObject deletedObject) {
        Main.getAppEventDisp().notifyDeletedObj(deletedObject);
    }

    public void showWindow(ApplicationEvent.appWindows appWindow, boolean b) {
        Main.getAppEventDisp().showWindow(appWindow, b);
    }

    public void notifyNewClient(Client c) {
        Main.getAppEventDisp().notifyNewClient(c);
    }

    public void notifyNewProduit(Produit p) {
        Main.getAppEventDisp().notifyNewProduit(p);
    }

    public void notifyNewEmploye(Employe e) {
        Main.getAppEventDisp().notifyNewEmploye(e);
    }

    public void notifyNewCommande(Commande c) {
        Main.getAppEventDisp().notifyNewCommande(c);
    }

    public ArrayList<Produit> searchProduits() {
        Transaction tx = Main.getAppM().searchAll("Produit");
        ArrayList<Produit> out = new ArrayList<Produit>();
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<Long> objsid = (ArrayList<Long>) tx.getCreatedObj();
            for (long l : objsid) {
                Produit loadedClient = new Produit();
                loadedClient.load(tx, l);
                if (tx.getLevel() != Alert.AlertType.ERROR) {
                    out.add(loadedClient);
                } else {
                    System.err.println("Produit avec id : " + l + " introuvable");
                }

            }
        }
        return out;
    }

    public String searchFournisseurNumber(float idfournisseur) {
        Transaction tx = Main.getAppM().searchFournisseurNumber(idfournisseur);
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<String> name = (ArrayList<String>) tx.getCreatedObj();
            if (name.size() != 0) {
                return name.get(0);
            }
        }
        return "";
    }

    public long searchQtyOfProduit(float id) {
        Transaction tx = Main.getAppM().searchQtyOfProduit(id);
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            ArrayList<Long> results = new ArrayList<Long>();
            //noinspection unchecked
            results = (ArrayList<Long>) tx.getCreatedObj();
            if (results.size() > 0) {
                return results.get(0);
            }
        }
        return -1;

    }

    public void createCommande(ArrayList<Long> listearticle, float reduction, String typepaiement, Adresse adresselivr, Date datelivraison, long ID_client) {
        Transaction tx = Main.getAppM().createCommande(listearticle, reduction, typepaiement, adresselivr, datelivraison, ID_client);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            showDialog(tx, "Creation commande");
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyNewCommande((Commande) tx.getCreatedObj());
                for (long l : listearticle) {
                    Transaction tmp = Main.getAppM().removeOneOfProduct(l);
                    if (tmp.getLevel() == Alert.AlertType.NONE && tmp.getLevel() == Alert.AlertType.ERROR) {
                        System.err.println("The following error happen during the stock operation of " + l + " : " + tmp.getMessage());
                    }
                }
                notifyUpdatedStock(listearticle);
            }
        }
    }

    private void notifyUpdatedStock(ArrayList<Long> productsLong) {
        ArrayList<Long> done = new ArrayList<>();
        for (long l : productsLong) {
            if (!isContaining(done,l)) {
                Main.getAppEventDisp().notifyUpdatedStock((Produit) Main.getAppM().loadProduit(l).getCreatedObj());
                done.add(l);
            }
        }
    }

    private boolean isContaining(ArrayList<Long> done, long l) {
        for(long d :done){
            if (l == d){
                return  true;
            }
        }
        return false;
    }


    public void notifySelectedClient(Client c) {
        Main.getAppEventDisp().notifySelectedClient(c);
    }

    public Client searchClient(long id_client) {
        Transaction tx = Main.getAppM().loadClient(id_client);
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            Client c = (Client) tx.getCreatedObj();
            return c;
        } else {
            return null;

        }
    }


    public ArrayList<Commande> searchAllCommande() {
        Transaction tx = Main.getAppM().searchAll("Commande");
        ArrayList<Commande> out = new ArrayList<Commande>();
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<Long> objsid = (ArrayList<Long>) tx.getCreatedObj();
            for (long l : objsid) {
                Commande loadedCommande = new Commande();
                loadedCommande.load(tx, l);
                if (tx.getLevel() != Alert.AlertType.ERROR) {
                    out.add(loadedCommande);
                } else {
                    System.err.println("Commande avec id : " + l + " introuvable");
                }

            }
        }
        return out;
    }

    public ArrayList<Produit> getProduitListFromCommande(Commande c) {
        ArrayList<Produit> out = new ArrayList<>();
        for (long l : c.getListeArticle()) {
            Transaction tx = Main.getAppM().searchProduit(l);
            if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
                out.add((Produit) tx.getCreatedObj());
            } else {
                System.err.println("Produit avec id : " + l + " introuvable");
            }
        }
        return out;
    }

    public ArrayList<Employe> searchAllEmploye() {
        Transaction tx = Main.getAppM().searchAll("Employe");
        ArrayList<Employe> out = new ArrayList<Employe>();
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<Long> objsid = (ArrayList<Long>) tx.getCreatedObj();
            for (long l : objsid) {
                Employe employe = new Employe();
                employe.load(tx, l);
                if (tx.getLevel() != Alert.AlertType.ERROR) {
                    out.add(employe);
                } else {
                    System.err.println("Employe avec id : " + l + " introuvable");
                }

            }
        }
        return out;
    }

    public void updateClient(String prenom, String nom, Adresse a, Date datanaissance, String mail, String tel, boolean fidel) {
        Transaction tx = Main.getAppM().updateClient(prenom, nom, a, datanaissance, mail, tel, fidel);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyDeletedDBObject((DBObject) tx.getDeleteObj());
                notifyNewClient((Client) tx.getCreatedObj());
                showDialog(tx, "Update Client ");
            } else {
                showDialog(tx, "Client");
            }
        }
    }

    public void updateProduit(String typeArticle, String marque, String nom, float prix, boolean iss, float solde, long idFournissseur, long stock) {
        Transaction tx = Main.getAppM().updateProduit(typeArticle, marque, nom, prix, iss, solde, idFournissseur, stock);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyDeletedDBObject((DBObject) tx.getDeleteObj());
                notifyNewProduit((Produit) tx.getCreatedObj());
                showDialog(tx, "Update Produit ");
            } else {
                showDialog(tx, "Produit");
            }
        }
    }

    public int searchPhoneNumberClient(String tel) {
        Transaction tx = Main.getAppM().searchPhoneNumberClient(tel);
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<Long> results = (ArrayList<Long>) tx.getCreatedObj();
            return results.size();
        }
        showDialog(tx, "Recherche numéro de téléphone");
        return -1;
    }

    public void notifyLowStock(long id_produit) {
        ArrayList<Produit> results = searchProduits();
        for (Produit p : results) {
            if (p.getId() == id_produit) {
                Main.getAppEventDisp().notifyLowStock("Il reste moins de 6 :" + p.getDesc());
            }
        }
    }

    public void notifyDelivery(Commande c) {
        Main.getAppEventDisp().notifyDelivery("Livraison aujourd'hui de la commande de : " + searchClient(c.getID_client()).getDesc());
    }

    public void notifyBirthday(Client c) {
        Main.getAppEventDisp().notifyBirthday("Anniversaire de : " + c.getDesc());
    }

    public void createFournisseur(String fourname) {
        Transaction tx = Main.getAppM().createFournisseur(fourname);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            showDialog(tx, "Creation fournisseur");
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyNewFournisseur((Fournisseur) tx.getCreatedObj());
            }
        }
    }

    private void notifyNewFournisseur(Fournisseur createdObj) {
        Main.getAppEventDisp().notifyNewFournisseur(createdObj);
    }

    public ArrayList<Fournisseur> searchAllFournisseurs() {
        Transaction tx = Main.getAppM().searchAll("Fournisseur");
        ArrayList<Fournisseur> out = new ArrayList<Fournisseur>();
        if (tx.getLevel() != Alert.AlertType.NONE && tx.getLevel() != Alert.AlertType.ERROR) {
            @SuppressWarnings("unchecked") ArrayList<Long> objsid = (ArrayList<Long>) tx.getCreatedObj();
            for (long l : objsid) {
                Fournisseur Fournisseur = new Fournisseur();
                Fournisseur.load(tx, l);
                if (tx.getLevel() != Alert.AlertType.ERROR) {
                    out.add(Fournisseur);
                } else {
                    showDialog(tx,"Fournisseur avec id : " + l + " introuvable");
                }

            }
        }
        return out;
    }

    public void updateFournisseur(String fourname) {
        Transaction tx = Main.getAppM().updateFournisseur(fourname);
        if (tx.getLevel() != Alert.AlertType.NONE) {
            if (tx.getLevel() != Alert.AlertType.ERROR) {
                notifyDeletedDBObject((DBObject) tx.getDeleteObj());
                notifyNewFournisseur((Fournisseur) tx.getCreatedObj());
                showDialog(tx, "Update Fournisseur ");
            } else {
                showDialog(tx, "Fournisseur");
            }
        }
    }
}

