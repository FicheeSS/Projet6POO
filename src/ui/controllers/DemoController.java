package ui.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Window;
import logic.ApplicationEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class DemoController extends ShowHideDialog implements Initializable {
    @FXML
    public AnchorPane imagePane;
    @FXML
    public TextArea imgDescArea;
    @FXML
    public Button next;
    @FXML
    public Button back;
    @FXML
    public ImageView imageViewPane;
    ArrayList<Image> imageGal = new ArrayList<Image>();
    //ecrire la description des image qui defille ne pas oublier les \n pour sauter des lignes
    String[] textList = {
            "I-Fournisseur : L'ajout d'un fournisseur est obligatoire pour \n ajouter de " +
                "nouveaux produits," + " il se fait sous " +
                 "les etapes suivantes :  ", "Ensuite il suffit de taper le nom de votre nouveau fournisseur " +
                "puis appuyer" + " sur créer. ",
            "II-Client : La creation" + "se fait egalement en saisissant " +
                    "toutes les coordonées de " + "notre client \n du Nom à la date de naissance en " +
                    "passant par l'adresse.  " , " Apres avoir tout saisi on appuie sur Creer,\n après cela on " +
                         "aura donc" + " notre fiche client ajoutée à notre base de données. ",
            " III - Employé : La méthode reste inchangé pour Employé" +
                                 " également ", "Apres avoir saisi les informations de l'employé il suffira " +
                                    "d'appuyer sur creer. ",
            "IV- Commande : Ici nous pouvons passer nos differentes commandes", "si l'acheteur a une fiche client\n " +
                        "il suffira d'appuyer sur rechercher et ensuite selectionner le client voulu parmi la liste\n" +
                        "sinon on entre ses differentes coordonées ainsi que les informations de la commande\n " +
                        "comme le type de paiement, l'adresse et la date de livraison voulu \n" +
                        " , enfin on a un acées à la liste de produits, on " +
                        "ajoutera les produits voulus aux panier, on peut également supprimer,\najouter des produits" +
                        "du panier ", "Ici on peut remarquer la création d'une commande ! ",
            "V- Produit : Cette partie nous permets d'ajouter de nouveaux produits au stock.","Pour ce faire" +
                        " il suffira  d'entrer  les differentes informations de  produit,\n comme par exemple " +
                         "le fournisseur, le nom du produit, son type ...\n", "evidemment si "+
                        "ce dernier n'a pas été ajouté comme Fournisseur, l'ajout du produit sera impossible ",
                        "Sinon le produit s'ajoutera au stock avec sucées !",

            " I- Commande : Ici on peut rechercher toutes les commandes passées", " Le but est de verifier les informations" +
                        " d'une des commandes ou bien d'en annuler une.  ",
            "II- Employé : Là on peut aussi verifier la liste des employés afin de voir une des informations de " +
                    "chaque employé et on peut en supprimer un si l'on souhaite !", "ici la création d'un employé",

            "III- Produit : Ici on peut également verifier un ou plusieurs produits parmi tout ceux qui ont été " +
                            "ajouté au stock avec la possibilité d'en supprimer !  ", " Ici la création d'un produit !",



            "IV- Client : Enfin, ici on peut verifier toute la liste des clients, cette dernière sera très utile si\n " +
                    "l'on souhaite mettre à jour une ou plusieurs informations de n'importe quel client","Ici la création " +
            "d'un client !"



    };
    private int Index;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initAppDispatch(ApplicationEvent.appWindows.CREATE_DEMO);
        back.setDisable(true);
        try {
            //ajouter les images ici avec leur nom
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerFournisseur.jpg"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerFournisseur2.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerClient1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerClient2.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerEmploye1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerEmploye2.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerCommabde1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerCommande2.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerCommande3.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerProduit.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerProduit2.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerProduitErreur.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/CreerProduitReussit.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RechercherCommande.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RechercherCommande1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RecherchetEmploye1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RecherchetEmploye2.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RechercherProduit1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RecherchetEmploye1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RechercherClient1.png"))));
            imageGal.add(new Image(Objects.requireNonNull(DemoController.class.getResourceAsStream("./img/RechercherClient2.png"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
        imageViewPane.setPreserveRatio(true);
        imageViewPane.setFitHeight(500);
        Index = 0;
        imgDescArea.setText(textList[Index]);
        imageViewPane.setImage(imageGal.get(Index));
        assert imageGal.size() == textList.length;
    }

    @Override
    protected Window getWindow() {
        return next.getScene().getWindow();
    }

    public void onAdvance(ActionEvent actionEvent) {
        if (Index + 1 < imageGal.size()) {
            Index++;
            imgDescArea.setText(textList[Index]);
            imageViewPane.setImage(imageGal.get(Index));
            next.setDisable((Index + 1 == imageGal.size()));
        }
        if (Index > 0) {
            back.setDisable(false);
        }

    }

    public void onBack(ActionEvent actionEvent) {
        if (Index > 0) {
            Index--;
            imgDescArea.setText(textList[Index]);
            imageViewPane.setImage(imageGal.get(Index));
            back.setDisable((Index == 0));
        }
        if (!(Index + 1 == imageGal.size())) {
            next.setDisable(false);
        }

    }
}
