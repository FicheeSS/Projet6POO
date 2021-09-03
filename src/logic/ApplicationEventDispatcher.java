package logic;

import magasin.*;
import ui.Main;

import java.util.ArrayList;
import java.util.List;

public class ApplicationEventDispatcher {

    private final List<IApplicationEventDispatcherListener> listeners = new ArrayList<>();

    public void addListener(IApplicationEventDispatcherListener itf) {
        listeners.add(itf);
    }


    public void notifyAddedProduct(Produit p) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.ADDED_PRODUIT, p);
        });
    }

    public void notifyDeletedObj(DBObject o) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.DELETED, o);
        });
        notifySucces("Suppresion effectué de avec succes");
    }

    public void showWindow(ApplicationEvent.appWindows window, boolean bShow) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.SHOW_WINDOW, window, bShow);
        });
    }

    public void notifyNewClient(Client c) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.NEW_CLIENT, c);
        });
        notifySucces("Creation de : " + c.getDesc() + " effectué avec succes");
    }

    public void notifyNewProduit(Produit p) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.NEW_PRODUIT, p);
        });
        notifySucces("Creation de : " + p.getDesc() + " effectué avec succes");
    }

    public void notifyNewEmploye(Employe e) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.NEW_EMPLOYE, e);
        });
        notifySucces("Creation de : " + e.getDesc() + " effectué avec succes");
    }

    public void notifyNewCommande(Commande c) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.NEW_COMMAND, c);
        });
        notifySucces("Creation de la commande : " + c.getDesc() + " effectué avec succes");
    }

    public void notifyForceReload() {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.FORCE_RELOAD);
        });
        Main.checkDailyNotify();
    }

    public void notifySelectedClient(Client c) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.SELECTED_CLIENT, c);
        });
    }

    public void notifyLowStock(String s) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.MESSAGE, s, ApplicationEvent.messageTypes.STOCK);
        });
    }

    public void notifyDelivery(String s) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.MESSAGE, s, ApplicationEvent.messageTypes.LIVRAISON);
        });
    }

    public void notifyBirthday(String s) {
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.MESSAGE, s, ApplicationEvent.messageTypes.BIRTHDAY);
        });
    }
    private void notifySucces(String s){
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.MESSAGE, s, ApplicationEvent.messageTypes.SUCCES);
        });
    }
    public void notifyNewFournisseur(Fournisseur f){
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.NEW_FOURNISSEUR, f);
        });
        notifySucces("Nouveau founisseur : " + f.getDesc());
    }
    public void notifyUpdatedStock(Produit p ){
        String s = " Mise a jour du stock de " + p.getDesc() + " nouveau stock de :" + String.valueOf(Main.getAppC().searchQtyOfProduit(p.getId()));
        listeners.forEach((l) -> {
            l.dispatchEvent(ApplicationEvent.events.MESSAGE, s,ApplicationEvent.messageTypes.STOCK);
        });
    }
}
