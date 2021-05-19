package logic;

public class ApplicationEvent {

    public enum appWindows {
        CREATE_CLIENT,
        QUERRY_CLIENT,
        CREATE_PRODUIT, CREATE_EMPLOYE,
        CREATE_CLIENT_QUERY,
        CREATE_CLIENT_CHART, CREATE_PRODUIT_QUERY, CREATE_PRODUIT_ADDER, CREATE_CLIENT_COMMANDE_FILLER, CREATE_ABOUT, CREATE_COMMANDE_QUERY, CREATE_EMPLOYE_QUERY, CREATE_DEMO, CREATE_COMMANDE
    }

    public enum events {
        SHOW_WINDOW, NEW_CLIENT, NEW_PRODUIT, NEW_EMPLOYE, DELETED, ADDED_PRODUIT,
        NEW_COMMAND, FORCE_RELOAD, CREATE_COMMANDE, MESSAGE, SELECTED_CLIENT
    }

    public enum messageTypes{
        BIRTHDAY,STOCK,LIVRAISON
    }
}
